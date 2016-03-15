package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import ua.kiev.doctorvera.jms.JMSMessengerLocal;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 3/1/2016.
 */
@Named(value="testView")
@ApplicationScoped
public class TestView implements Serializable {

//    @EJB
//    JMSMessengerLocal jmsMessenger;
//
//    private String message;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public void sendMessage() throws JMSException {
//        jmsMessenger.sendMessage("adsfafafdasfasfd adfafafasdfasdfdsfaf");
//    }
//
//    public void showMessage() throws JMSException {
//        message = jmsMessenger.receiveMessage();
//        RequestContext.getCurrentInstance().execute("PF('notificationBar').hide()");
//    }
}
