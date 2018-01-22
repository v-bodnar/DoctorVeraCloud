package ua.kiev.doctorvera.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.kiev.doctorvera.entities.MessageLog;
import ua.kiev.doctorvera.entities.TransactionLog;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MessageLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.TransactionLogFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.utils.SMSGateway;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static ua.kiev.doctorvera.entities.TransactionLog.Status.CANCELED;

/**
 * Created by volodymyr.bodnar on 1/10/2016.
 */
@Singleton
public class SMSService implements SMSServiceLocal{

    private final static Logger LOG = Logger.getLogger(SMSService.class.getName());
    private static final String LOGIN = Config.getInstance().getString("SMS_GATEWAY_LOGIN");
    private static final String PASS = Config.getInstance().getString("SMS_GATEWAY_PASSWORD");
    private static final String FROM = Config.getInstance().getString("SMS_GATEWAY_ALPHA_NAME");
    private static final String SMS_SEND_URL = Config.getInstance().getString("SMS_GATEWAY");
    private static final String SMS_STATUS_REQUEST_URL = Config.getInstance().getString("SMS_GATEWAY_STATUS_REQUEST_URL");

    @EJB
    private TransactionLogFacadeLocal transactionLogFacade;

    @EJB
    private MessageLogFacadeLocal messageLogFacade;

    @EJB
    private UsersFacadeLocal usersFacade;



    /**
     * Send one SMS to Gateway, type - single
     * It checks whether user is agreed to receive sms and if he has phoneNumber
     * If there is no user with the given phone number, current user will be written as a recipient
     */
    @Override
    public boolean sendSMS(String phone, String sms, TransactionLog transactionLog) {
        List<Users> usersList = usersFacade.findByPhoneNumberMobile(phone);
        Users recipient = null;
        if(usersList != null && usersList.size() == 1){
            recipient = usersList.get(0);
            if(recipient.getPhoneNumberMobile() == null || recipient.isForeigner() || !recipient.isMessagingAccepted()){
                LOG.info("User does not have mobile phone, or is foreigner or, did not accept messaging!");
                return false;
            }

        }

        final String xmlMessage = "<message><service id='single' source='" + FROM + "'/><to>" + phone + "</to><body content-type='text/plain'>" + sms + "</body></message>";

        //parsing xml result
        String response = postRequestToGateway(xmlMessage, SMS_SEND_URL);
        if (response == null) return false;
        Document doc = loadXMLFromString(response);
        NodeList nl = doc.getElementsByTagName("status");
        Element statusNode = (Element) nl.item(0);
        Node state = doc.getElementsByTagName("state").item(0);

        MessageLog messageLog = new MessageLog();
        List<MessageLog> messageLogs = new LinkedList<>();
        messageLogs.add(messageLog);
        messageLog.setStatus(convertMessageStatus(state.getTextContent()));
        messageLog.setuId(statusNode.getAttribute("id"));
        transactionLog.setuId(statusNode.getAttribute("id"));
        try {
            messageLog.setDateCreated(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(statusNode.getAttribute("date")));
        } catch (ParseException e) {
            LOG.severe("Error in parsing date: " + statusNode.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
        }
        messageLog.setDetails(((Element)state).getAttribute("error"));
        messageLog.setUserCreated(transactionLog.getMessageTemplate().getUserCreated());
        messageLog.setRecipient(recipient == null ? transactionLog.getUserCreated() : recipient);
        messageLog.setTransaction(transactionLog);
        messageLogFacade.create(messageLog);
        transactionLog.setMessageLogs(messageLogs);
        transactionLogFacade.checkTransactionStatus(transactionLog, false);

        LOG.info("SMS has been sent! Recipient: "
                + recipient == null ? phone : recipient.getFirstName() + " " + recipient.getLastName() + " " + recipient.getPhoneNumberMobile()
                + ", tracking id: " + messageLog.getuId() + " Status: " + messageLog.getStatus());
        return true;
    }

    /**
     * Send one SMS to Gateway, type - single
     * It checks whether user is agreed to receive sms and if he has phoneNumber
     * If there is no user with the given phone number, current user will be written as a recipient
     */
    @Override
    public boolean sendSMS(Users recipient, String sms, TransactionLog transactionLog) {
        if (recipient.getPhoneNumberMobile() == null || recipient.isForeigner() || !recipient.isMessagingAccepted()){
            LOG.info("User does not have mobile phone, or is foreigner or, did not accept messaging!");
            transactionLog.setStatus(CANCELED);
            transactionLogFacade.edit(transactionLog);
            return false;
        }

        final String xmlMessage = "<message><service id='single' source='" + FROM + "'/><to>" + recipient.getPhoneNumberMobile() + "</to><body content-type='text/plain'>" + sms + "</body></message>";

        //parsing xml result
        String response = postRequestToGateway(xmlMessage, SMS_SEND_URL);
        if (response == null) return false;
        Document doc = loadXMLFromString(response);
        NodeList nl = doc.getElementsByTagName("status");
        Element statusNode = (Element) nl.item(0);
        Node state = doc.getElementsByTagName("state").item(0);

        MessageLog messageLog = new MessageLog();
        List<MessageLog> messageLogs = new LinkedList<>();
        messageLogs.add(messageLog);
        messageLog.setStatus(convertMessageStatus(state.getTextContent()));
        messageLog.setuId(statusNode.getAttribute("id"));
        try {
            messageLog.setDateCreated(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US).parse(statusNode.getAttribute("date")));
        } catch (ParseException e) {
            messageLog.setDateCreated(new Date());
            LOG.severe("Error in parsing date: " + statusNode.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
        }
        messageLog.setDetails(((Element) state).getAttribute("error"));
        messageLog.setUserCreated(transactionLog.getMessageTemplate().getUserCreated());
        messageLog.setRecipient(recipient);
        messageLog.setTransaction(transactionLog);
        messageLogFacade.create(messageLog);
        transactionLog.setMessageLogs(messageLogs);
        transactionLogFacade.checkTransactionStatus(transactionLog, false);

        LOG.info("SMS has been sent! Recipient: "
                + recipient.getLastName() + " " + recipient.getPhoneNumberMobile()
                + ", tracking id: " + messageLog.getuId() + " Status: " + messageLog.getStatus());
        return true;
    }

    /**
     * Send multiple SMS with the same content to Gateway, type - bulk
     *  It checks if each user has mobile phone and agreed for receiving messages
     */
    @Override
    public boolean sendSMS(List<Users> users, String message, TransactionLog transactionLog) {
        String xmlMessage = "<message><service id='bulk' validity='+1 day' desc='"
                + transactionLog.getMessageTemplate().getName() + "' source='"
                + FROM + "' uniq_key='" + transactionLog.getId() + "'/>";
        for (Users user : users) {
            if(user.getPhoneNumberMobile() == null || user.isForeigner() || !user.isMessagingAccepted()){
                LOG.info("User " + user.getFirstName() + " " + user.getLastName() + " does not have mobile phone, or is foreigner or, did not accept messaging!");
                continue;
            }

            xmlMessage += "<to>" + user.getPhoneNumberMobile() + "</to>";
        }

        xmlMessage += "<body content-type='text/plain'>" + message + "</body>" +
                        "</message>";

        //parsing xml result
        String response = postRequestToGateway(xmlMessage, SMS_SEND_URL);
        if (response == null) return false;
        Document doc = loadXMLFromString(response);
        List<MessageLog> messageLogs = parseIndividualOrBulkSmsGatewayResponse(doc, transactionLog);

        //Setting recipient to results,
        int i = 0;
        for (Users user : users) {
            messageLogs.get(i).setRecipient(user);
            i++;
        }

        messageLogFacade.create(messageLogs);
        transactionLog.setMessageLogs(messageLogs);
        transactionLogFacade.checkTransactionStatus(transactionLog, false);

        LOG.info("SMS group has been sent! Delivery name: "
                + transactionLog.getMessageTemplate().getName() + ", tracking id: "
                + transactionLog.getuId() + " Status: " + transactionLog.getStatus());
        return true;
    }

    /**
     * Send multiple SMS with different content for each number to Gateway, type - individual
     * It checks if each user has mobile phone and agreed for receiving messages
     */
    @Override
    public boolean sendSMS(LinkedHashMap<Users, String> messages, TransactionLog transactionLog) {
        String xmlMessage = "<message><service id='individual' validity='+1 day' desc='"
                + transactionLog.getMessageTemplate().getName() + "' source='"
                + FROM + "' uniq_key='" + transactionLog.getId() + "'/>";
        for (Map.Entry<Users, String> entry : messages.entrySet()) {
            if(entry.getKey().getPhoneNumberMobile() == null || entry.getKey().isForeigner() || !entry.getKey().isMessagingAccepted()){
                LOG.info("User " + entry.getKey().getFirstName() + " " + entry.getKey().getLastName() + " does not have mobile phone, or is foreigner or, did not accept messaging!");
                continue;
            }
            xmlMessage += "<to>" + entry.getKey().getPhoneNumberMobile() + "</to>";
            xmlMessage += "<body content-type='text/plain'>" + entry.getValue() + "</body>";
        }

        xmlMessage += "</message>";

        //parsing xml result
        String response = postRequestToGateway(xmlMessage, SMS_SEND_URL);
        if (response == null) return false;
        Document doc = loadXMLFromString(response);
        List<MessageLog> messageLogs = parseIndividualOrBulkSmsGatewayResponse(doc, transactionLog);

        //Setting recipient to results,
        int i = 0;
        for (Map.Entry<Users, String> entry : messages.entrySet()) {
            messageLogs.get(i).setRecipient(entry.getKey());
            i++;
        }
        messageLogFacade.create(messageLogs);
        transactionLog = transactionLogFacade.edit(transactionLog);
        transactionLogFacade.checkTransactionStatus(transactionLog, false);

        LOG.info("SMS group has been sent! Delivery name: "
                + transactionLog.getMessageTemplate().getName() + ", tracking id: "
                + transactionLog.getuId() + " Status: " + transactionLog.getStatus());
        return true;
    }

    /**
     * Sends request to SMS Gateway and retrieves message/transactions status
     * @param transactionLog transaction log for which status has to be refreshed
     */
    @Override
    public void checkSmsStatus(TransactionLog transactionLog){
        String xmlMessage;
        boolean singleMessage = transactionLog.getMessageLogs().size() == 1;

        if(singleMessage){
            if(((List<MessageLog>)transactionLog.getMessageLogs()).get(0).getStatus() == MessageLog.Status.CANCELED){
                transactionLog.setStatus(CANCELED);
                transactionLogFacade.edit(transactionLog);
            }
            if(((List<MessageLog>)transactionLog.getMessageLogs()).get(0).getuId() == null || ((List<MessageLog>)transactionLog.getMessageLogs()).get(0).getuId().isEmpty()){
                return;
            }
            xmlMessage = "<request id='" + ((List<MessageLog>)transactionLog.getMessageLogs()).get(0).getuId() + "'>status</request>";
        }else{
            if(transactionLog.getuId() == null || transactionLog.getuId().isEmpty()){
                return;
            }
            xmlMessage = "<request groupid='" + transactionLog.getuId() + "'>status</request>";
        }

        //parsing xml result
        LOG.info("Requesting " + (singleMessage ? "single message status" : "message group status"));
        String response = postRequestToGateway(xmlMessage, SMS_STATUS_REQUEST_URL);
        Document doc = loadXMLFromString(response);
        if (singleMessage){
            parseSingleMessageStatusRequest(transactionLog,((List<MessageLog>) transactionLog.getMessageLogs()).get(0), doc);
        }else{
            parseGroupStatusRequest(transactionLog, doc);
        }
    }

    private void parseSingleMessageStatusRequest(TransactionLog transactionLog, MessageLog messageLog,  Document doc){
        if (doc == null){
            LOG.severe("No document for parsing");
            return;
        }
        NodeList statuses = doc.getElementsByTagName("status");
        NodeList states = doc.getElementsByTagName("state");

        Element statusNode = (Element) statuses.item(0); //root element
        Date dateUpdated = null;
        try {
            dateUpdated = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(statusNode.getAttribute("date"));
        } catch (ParseException e) {
            LOG.severe("Error in parsing date: " + statusNode.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
        }
        if(dateUpdated != null) {
            transactionLog.setDateCreated(dateUpdated);
            messageLog.setDateCreated(dateUpdated);
        }
        LOG.info("Message id: " + messageLog.getuId() + " status: " + states.item(0).getTextContent());
        messageLog.setStatus(convertMessageStatus(states.item(0).getTextContent()));
        messageLogFacade.edit(messageLog);
        transactionLog.setStatus(convertTransactionStatus(states.item(0).getTextContent()));
        transactionLogFacade.edit(transactionLog);
    }

    private void parseGroupStatusRequest(TransactionLog transactionLog, Document doc){
        Element status = (Element)doc.getElementsByTagName("status").item(0);
        Element total = (Element)doc.getElementsByTagName("total").item(0);
        Element queued = (Element)doc.getElementsByTagName("queued").item(0);
        Element accepted = (Element)doc.getElementsByTagName("accepted").item(0);
        Element enroute = (Element)doc.getElementsByTagName("enroute").item(0);
        Element delivered = (Element)doc.getElementsByTagName("delivered").item(0);
        Element expired = (Element)doc.getElementsByTagName("expired").item(0);
        Element undeliverable = (Element)doc.getElementsByTagName("undeliverable").item(0);
        Element unknown = (Element)doc.getElementsByTagName("unknown").item(0);

        LOG.info("Message group id: " + status.getAttribute("groupid")
                + " name: " + status.getAttribute("desc")
                + " state: " + status.getAttribute("state")
                + " reports: " + status.getAttribute("reports")
                + "");
        LOG.info("Total - " + total == null ? "" : total.getTextContent());
        LOG.info("Queued - " + queued == null ? "" : queued.getTextContent());
        LOG.info("Accepted - " + accepted == null ? "" : accepted.getTextContent());
        LOG.info("Enroute - " + enroute == null ? "" : enroute.getTextContent());
        LOG.info("Delivered - " + delivered == null ? "" : delivered.getTextContent());
        LOG.info("Expired - " + expired == null ? "" : expired.getTextContent());
        LOG.info("Undeliverable - " + undeliverable == null ? "" : undeliverable.getTextContent());
        LOG.info("Unknown - " + unknown == null ? "" : unknown.getTextContent());

        Date dateUpdated = null;
        try {
            dateUpdated = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(status.getAttribute("date"));
        } catch (ParseException e) {
            LOG.severe("Error in parsing date: " + status.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
        }
        if(dateUpdated != null) {
            transactionLog.setDateCreated(dateUpdated);
        }

        transactionLog.setStatus(convertTransactionStatus(status.getAttribute("reports")));
        transactionLogFacade.edit(transactionLog);

        LOG.info("Requesting single message status for message group: " + transactionLog.getMessageTemplate().getName());
        for(MessageLog messageLog : transactionLog.getMessageLogs()){
            if(messageLog.getStatus() == MessageLog.Status.SEND_ERROR
                    || messageLog.getStatus() == MessageLog.Status.DELIVERED
                    || messageLog.getStatus() == MessageLog.Status.CANCELED
                    || messageLog.getStatus() == MessageLog.Status.DELIVERY_ERROR) {
                LOG.info("Message id: " + messageLog.getuId() + " status: " + messageLog.getStatus() + " Message status is final. ");
                continue;
            }
            String messageLogRequest = "<request id='" + messageLog.getuId() + "'>status</request>";
            String response = postRequestToGateway(messageLogRequest, SMS_STATUS_REQUEST_URL);
            Document messageLogDoc = loadXMLFromString(response);
            parseSingleMessageStatusRequest(transactionLog, messageLog, messageLogDoc);
        }
    }


    /**
     * Methods extracts xml nodes from sms gateway response
     * and put its content to message and transaction logs
     * !!! Works only for sending type individual or bulk !!!
     *
     * @param doc xml document for parsing
     * @param transactionLog transactionLog to which this send operation belongs
     * @return list of MessageLogs filled with data but not persisted, !!! PERSIST IT
     */
    private List<MessageLog> parseIndividualOrBulkSmsGatewayResponse(Document doc, TransactionLog transactionLog){
        NodeList nl = doc.getElementsByTagName("status");
        NodeList ids = doc.getElementsByTagName("id");
        NodeList states = doc.getElementsByTagName("state");

        Element statusNode = (Element) nl.item(0); //root element
        transactionLog.setuId(statusNode.getAttribute("groupid"));
        try {
            transactionLog.setDateCreated(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(statusNode.getAttribute("date")));
        } catch (ParseException e) {
            LOG.severe("Error in parsing date: " + statusNode.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
        }

        List<MessageLog> result = new ArrayList<>();
        for(int i = 0; i < ids.getLength(); i++){
            MessageLog messageLog = new MessageLog();

            Node messageId = ids.item(i);
            messageLog.setuId(messageId.getTextContent());

            messageLog.setUserCreated(transactionLog.getMessageTemplate().getUserCreated());

            Node messageState = states.item(i);
            messageLog.setStatus(convertMessageStatus(messageState.getTextContent()));
            if (messageState.getNodeType() == Node.ELEMENT_NODE) {
                Element messageStateElement = (Element)messageState;
                messageLog.setDetails(messageStateElement.getAttribute("error"));
            }
            try {
                messageLog.setDateCreated(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US).parse(statusNode.getAttribute("date")));
            } catch (ParseException e) {
                LOG.severe("Error in parsing date: " + statusNode.getAttribute("date") + "for format 'EEE, d MMM yyyy HH:mm:ss Z'.");
            }
            messageLog.setTransaction(transactionLog);
            result.add(messageLog);
        }
        return result;
    }

    /**
     * Sends post request on smsGateway and receives responce
     * @param xml xml formatted request
     * @return string with xml formatted response
     */
    private String postRequestToGateway(String xml, String url) {

        String response = null;
        try(CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(url);

            StringEntity entity = new StringEntity(xml, "UTF-8");
            entity.setContentType("text/xml");
            entity.setChunked(true);
            httpPost.setEntity(entity);
            httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(LOGIN, PASS), "UTF-8", false));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            LOG.info("Connection status: " + (httpResponse.getStatusLine().getStatusCode()));
            LOG.info("Sending SMS: " + (httpResponse.getStatusLine().getStatusCode() == 200));
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                LOG.severe("Connection error");
                return null;
            }
            response = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            LOG.severe("" + e.getMessage());
        }
        return response;
    }

    /**
     * Method generates XML document
     */
    private Document loadXMLFromString(String xml) {
        Document doc = null;
        if (xml == null){
            LOG.severe("No document for parsing");
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            return null;
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
            return null;
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    private MessageLog.Status convertMessageStatus(String stringStatus){
        stringStatus = stringStatus.toLowerCase();
        switch (stringStatus){
            case("waiting"):
                return MessageLog.Status.WAITING;
            case("sending"):
                return MessageLog.Status.SENDING;
            case("sent"):
                return MessageLog.Status.SENT;
            case("paused"):
                return MessageLog.Status.PAUSED;
            case ("delivered"):
                return MessageLog.Status.DELIVERED;
            case("canceled"):
                return MessageLog.Status.CANCELED;
            default:
                return MessageLog.Status.NEW;
        }
    }

    private TransactionLog.Status convertTransactionStatus(String stringStatus){
        stringStatus = stringStatus.toLowerCase();
        switch (stringStatus){
            case("waiting"):
            case("sending"):
                return TransactionLog.Status.PROGRESS;
            case("sent"):
                return TransactionLog.Status.SENT;
            case ("delivered"):
            case("completed"):
                return TransactionLog.Status.DELIVERED;
            case("paused"):
                return TransactionLog.Status.PAUSED;
            case("canceled"):
                return CANCELED;
            default:
                return TransactionLog.Status.PROGRESS;
        }
    }
}
