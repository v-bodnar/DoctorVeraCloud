package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.services.SMSServiceLocal;
import ua.kiev.doctorvera.utils.SMSGateway;
import ua.kiev.doctorvera.utils.Utils;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

@Named(value="sendSMSView")
@SessionScoped
public class SendSMSView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());

	private final SMSGateway smsGateway = new SMSGateway();
	
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
		phone = Utils.stripPhone(phone);
		Map<SMSGateway.SMSResultConstants, String> result = smsGateway.send(phone, text);
		LOG.info("SMS sent, tracking id: " + result.get(SMSGateway.SMSResultConstants.UUID) + " Status: " + result.get(SMSGateway.SMSResultConstants.STATUS));
		final String successMessage = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		final String successTitle = Message.getMessage("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}

}
