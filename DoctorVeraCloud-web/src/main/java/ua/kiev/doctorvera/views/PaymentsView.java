package ua.kiev.doctorvera.views;

import com.itextpdf.text.*;
import org.joda.time.DateTime;
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
import java.util.ArrayList;
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

	private List<Payments> filteredPayments = new ArrayList<>();

	private Payments newPayment;

	private Payments selectedPayment;

	private Boolean isPositive;

	private Date dateStart;

	private Date dateEnd;

	private float totalSum = 0f;

	public PaymentsView(){}

	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allPayments = paymentsFacade.findByDate(DateTime.now().withDayOfMonth(1).toDate(), new Date());
		newPayment = new Payments();
		isPositive = new Boolean(true);
		for(Payments payment : allPayments){
			totalSum += payment.getTotal();
		}
	}

	public void searchPayments(){
		allPayments = paymentsFacade.findByDate(dateStart, dateEnd);
		totalSum = 0f;
		filteredPayments.clear();
		filteredPayments.addAll(allPayments);
		for(Payments payment : filteredPayments){
			totalSum += payment.getTotal();
		}
	}

	public void createPayment() {
		if (!isPositive) newPayment.setTotal(-newPayment.getTotal());
		newPayment.setDateTime(new Date());
		newPayment.setDateCreated(new Date());
		newPayment.setUserCreated(authorizedUser);
		paymentsFacade.create(newPayment);
		allPayments.add(newPayment);
		totalSum = 0f;
		for(Payments payment : filteredPayments){
			totalSum += payment.getTotal();
		}
		final String successMessage = Message.getMessage("PAYMENTS_CREATED");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addPaymentDialog').hide();");
		LOG.info(successTitle);
	}


	public StreamedContent printPayment(Payments payment) throws IOException, DocumentException {
		TemplateProcessor processUtil = new TemplateProcessor();
		processUtil.setCurrentUser(authorizedUser);
		processUtil.setPayment(payment);
		if(payment.getSchedule() == null) {
			FileRepository usersForm;
			if(isPositive){
				usersForm = fileRepositoryFacade.find(Integer.parseInt(Config.getMessage("INCOME_FORM_TEMPLATE")));
			}else{
				usersForm = fileRepositoryFacade.find(Integer.parseInt(Config.getMessage("OUTCOME_FORM_TEMPLATE")));
			}
			processUtil.setUser(payment.getRecipient());
			processUtil.setCurrentUser(payment.getUserCreated());
			return processUtil.processTemplate(usersForm);
		}else {
			FileRepository usersForm = fileRepositoryFacade.find(Integer.parseInt(Config.getMessage("INCOME_FORM_TEMPLATE")));
			processUtil.setUser(payment.getSchedule().getPatient());
			processUtil.setSchedule(payment.getSchedule());
			return processUtil.processTemplate(usersForm);
		}
	}

	public StreamedContent createAndPrintPayment() throws IOException, DocumentException {
		createPayment();
		return printPayment(newPayment);
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

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public float getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(float totalSum) {
		this.totalSum = totalSum;
	}

	public List<Payments> getFilteredPayments() {
		totalSum = 0f;
		for(Payments payment : filteredPayments){
			totalSum += payment.getTotal();
		}
		return filteredPayments;
	}

	public void setFilteredPayments(List<Payments> filteredPayments) {
		this.filteredPayments = filteredPayments;
	}

	public void initNewPayment() {
		this.newPayment = new Payments();
	}
}
