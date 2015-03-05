package ua.kiev.doctorvera.managedbeans;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.PaymentsFacadeLocal;
import ua.kiev.doctorvera.web.resources.Message;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

@ManagedBean(name="paymentsView")
@ViewScoped
public class PaymentsView {
	
	private final static Logger LOG = Logger.getLogger(PaymentsView.class.getName());
	
	@EJB
	private PaymentsFacadeLocal paymentsFacade;
	
    @ManagedProperty(value="#{userLoginView.authorizedUser}")
	private Users authorizedUser;
    
    private List<Payments> allPayments;
	
	private Payments newPayment;
	
	private Payments selectedPayment;
	
	private Boolean isPositive;
	
	public PaymentsView(){}
	
	@PostConstruct
	public void init(){
		allPayments = paymentsFacade.findAll();
		newPayment = new Payments();
		isPositive = new Boolean(true);
	}
	
	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}
	
    public List<Payments> getAllPayments() {
		return allPayments;
	}

	public void setAllPayments(List<Payments> allPayments) {
		this.allPayments = allPayments;
	}

	public Payments getNewPayment() {
		return newPayment;
	}

	public void setNewPayment(Payments newPayment) {
		this.newPayment = newPayment;
	}

	public Payments getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(Payments selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public Boolean getIsPositive() {
		return isPositive;
	}

	public void setIsPositive(Boolean isPositive) {
		this.isPositive = isPositive;
	}
	
	public void initNewPayment() {
		this.newPayment = new Payments();
	}

	public void createPayment() {
		if (!isPositive) newPayment.setTotal(-newPayment.getTotal());
    	newPayment.setDataTime(new Date());
		newPayment.setDateCreated(new Date());
		newPayment.setUserCreated(authorizedUser);
		paymentsFacade.create(newPayment);
		allPayments.add(newPayment);
		final String successMessage = Message.getInstance().getMessage(Message.Payments.PAYMENTS_CREATED);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addPaymentDialog').hide();");
		LOG.info(successTitle);
	}
    
    public void printPayment() {
        try {
        	SimpleDateFormat sf = new SimpleDateFormat("dd.MMMMM.yyyy");
            Document document = new Document(PageSize.A5);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("c://testpdf.pdf"));
            document.open();
            document.addAuthor("Doctor Vera");
            document.addCreator("Doctor Vera Cloud");
            document.addSubject("Order");
            document.addCreationDate();
            document.addTitle("Order");

            //FontFactory.register("\\resources\\fonts\\ARIALUNI.ttf","Arial Unicode MS");
            //FontFactory.register("/resources/fonts/ARIALUNI.ttf","Arial Unicode MS");
            FontFactory.register("C:\\Windows\\Fonts\\ARIALUNI.ttf","Arial Unicode MS");
            //FontFactory.register("C:/Windows/Fonts/ARIALUNI.ttf","Arial Unicode MS");
            Set<String> fonts = FontFactory.getRegisteredFonts();
            for (String font : fonts)
            	LOG.info(font);
            Font f = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.add(new Paragraph("Incoming Order " + newPayment.getId(),f));
            document.add(new Paragraph("Кассир: " + newPayment.getUserCreated().getFirstName() + " " + newPayment.getUserCreated().getLastName(),f));
            document.add(new Paragraph("Сумма: " + newPayment.getTotal(),f));
            document.add(new Paragraph("Описание: " + newPayment.getDescription(),f));
            document.add(new Paragraph("Дата: " + sf.format(newPayment.getDateCreated()),f));

            document.close();
            pdfWriter.close();
            LOG.info("PDF document for order #" + newPayment.getId() + " created");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
    }
    
	public void createAndPrintPayment(){
		createPayment();
		printPayment();
	}

	private String createHtmlOrderFromPayment(Payments payment){
		
        //TODO use locale from settings
        SimpleDateFormat sf = new SimpleDateFormat("dd.MMMMM.yyyy");
        String str;
        if (newPayment.getTotal() > 0)
        	str = "<html><head></head><body><h2>Incoming order: "+ newPayment.getId() + "</h2>" +
        		"Кассир: " + newPayment.getUserCreated().getFirstName() + " " + 
        		newPayment.getUserCreated().getLastName()+ "</BR>" +
        		"Сумма: " + newPayment.getTotal() + "</BR>" +
        		"Описание: " + newPayment.getDescription() + "</BR>" +
        		"Дата: " + sf.format(newPayment.getDateCreated()) + "</BR>"
        		+"</body></html>";
        else
        	str = "<html><head></head><body><h1>Incoming order: "+ newPayment.getId() + "</h1>" +
            	"Кассир: " + newPayment.getUserCreated().getFirstName() + " " + 
            	newPayment.getUserCreated().getLastName()+ "</BR>" +
            	"Сумма: " + newPayment.getTotal() + "</BR>" +
            	"Описание: " + newPayment.getDescription() + "</BR>" +
            	"Дата: " + sf.format(newPayment.getDateCreated()) + "</BR>"
            	+"</body></html>";
        return str;
	}
}
