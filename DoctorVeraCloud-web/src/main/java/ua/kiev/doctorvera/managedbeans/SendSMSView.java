package ua.kiev.doctorvera.managedbeans;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ua.kiev.doctorvera.utils.SMSGateway;
import ua.kiev.doctorvera.utils.Service;
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="sendSMSView")
@SessionScoped
public class SendSMSView {
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	
	@EJB
	private SMSGateway smsGateway;
	
	private String phone;
	
	private String text;
	
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
		phone = Service.stripPhone(phone);
		ArrayList<String> result = smsGateway.send(phone, text);
		LOG.info("SMS sent, tracking id: " + result.get(0) + " Status: " + result.get(2));
		final String successMessage = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		final String successTitle = Message.getInstance().getMessage(Message.Validator.VALIDATOR_SUCCESS_TITLE);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
