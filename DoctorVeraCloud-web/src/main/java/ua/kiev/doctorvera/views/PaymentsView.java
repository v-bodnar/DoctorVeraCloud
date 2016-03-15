package ua.kiev.doctorvera.views;

import com.itextpdf.text.*;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import ua.kiev.doctorvera.entities.FileRepository;
import ua.kiev.doctorvera.entities.Payments;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.FileRepositoryFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.PaymentsFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.TemplateProcessor;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named(value="paymentsView")
@ViewScoped
public class PaymentsView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(PaymentsView.class.getName());

	@EJB
	private PaymentsFacadeLocal paymentsFacade;
	@EJB
	private FileRepositoryFacadeLocal fileRepositoryFacade;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;
    
    private List<Payments> allPayments;
	
	private Payments newPayment;
	
	private Payments selectedPayment;
	
	private Boolean isPositive;
	
	public PaymentsView(){}
	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
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
		final String successMessage = Message.getMessage("PAYMENTS_CREATED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addPaymentDialog').hide();");
		LOG.info(successTitle);
	}
    
    public StreamedContent printPayment() throws IOException, DocumentException {
		TemplateProcessor processUtil = new TemplateProcessor();
		processUtil.setCurrentUser(authorizedUser);
		processUtil.setPayment(newPayment);
		if(newPayment.getSchedule() == null) {
			FileRepository usersForm = fileRepositoryFacade.find(Integer.parseInt(Config.getMessage("OUTCOME_FORM_TEMPLATE")));
			processUtil.setUser(newPayment.getRecipient());
			return processUtil.processTemplate(usersForm);
		}else {
			FileRepository usersForm = fileRepositoryFacade.find(Integer.parseInt(Config.getMessage("INCOME_FORM_TEMPLATE")));
			processUtil.setUser(newPayment.getSchedule().getPatient());
			processUtil.setSchedule(newPayment.getSchedule());
			return processUtil.processTemplate(usersForm);
		}
	}
    
	public StreamedContent createAndPrintPayment() throws IOException, DocumentException {
		createPayment();
		return printPayment();
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
