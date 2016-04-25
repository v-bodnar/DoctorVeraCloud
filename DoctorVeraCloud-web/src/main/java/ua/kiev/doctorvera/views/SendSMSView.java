package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.*;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MessageTemplateFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.services.SMSServiceLocal;
import ua.kiev.doctorvera.utils.SMSGateway;
import ua.kiev.doctorvera.utils.Utils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Named(value="sendSMSView")
@SessionScoped
public class SendSMSView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());

	@EJB
	private MessageTemplateFacadeLocal messageTemplateFacade;

	@EJB
	private TransactionLogFacadeLocal transactionLogFacade;

	@EJB
	private MessageLogFacadeLocal messageLogFacade;

	@EJB
	private UsersFacadeLocal usersFacade;

	@EJB
	private SMSServiceLocal smsService;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;

	//private final SMSGateway smsGateway = new SMSGateway();
	
	private String phone;
	
	private String text;

	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if (phone.length()==13)
			this.phone = phone.substring(3,13);
		else 
			this.phone = phone;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void sendSMS(){
		phone = Utils.stripPhone(phone);

		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setDateCreated(new Date());
		messageTemplate.setUserCreated(authorizedUser);
		messageTemplate.setContent(text);
		messageTemplate.setType(MessageTemplate.Type.SMS);
		messageTemplate.setSystem(false);
		messageTemplate.setName(Message.getMessage("MDS_INDIVIDUAL_SMS_NAME") + ": " + phone);
		messageTemplate.setDeleted(true);
		messageTemplateFacade.create(messageTemplate);

		TransactionLog transactionLog = new TransactionLog();
		transactionLog.setUserCreated(authorizedUser);
		transactionLog.setMessageTemplate(messageTemplate);
		transactionLog.setDateCreated(new Date());
		transactionLog.setStatus(TransactionLog.Status.PROGRESS);
		transactionLog.setDetails(Message.getMessage("MDS_INDIVIDUAL_SMS_NAME") + ": " + phone);
		transactionLogFacade.create(transactionLog);

		if(smsService.sendSMS(phone, text, transactionLog)){
			Message.showMessage(Message.getMessage("VALIDATOR_SUCCESS_TITLE"), Message.getMessage("VALIDATOR_SUCCESS_TITLE"));
		}else {
			Message.showMessage(Message.getMessage("VALIDATOR_ERROR_TITLE"), Message.getMessage("VALIDATOR_ERROR_TITLE"));
		}


	}

}
