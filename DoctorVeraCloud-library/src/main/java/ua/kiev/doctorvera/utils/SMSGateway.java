package ua.kiev.doctorvera.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Logger;

/*
 * This class send data to SMS Gateway. It can send SMS and get SMS state. Also it can check Internet connection
 * @author      Volodymyr Bodnar
 * @version     %I%, %G%
 * @since       1.0
 */
public class SMSGateway{
    private final String LOGIN = "cloud";
    private final String PASS = "88Zcy7nmhzeoutmn";
    private final String FROM = "Doctor Vera";
    private final String SMS_SEND_URL = "https://api.life.com.ua/ip2sms/";
    //private final String SMS_SATE_URL = "https://api.life.com.ua/ip2sms-request/";
    
    private final static Logger LOG = Logger.getLogger(SMSGateway.class.getName());
    
    public SMSGateway(){  
    }     
    	
	/*
	 * Checking if Gateway is reachable
	 */
    /*
    private  Boolean checkGatewayAuth(String login, String password, String alpha){
    	if(login==null || password==null  || alpha==null) return false;
    	final String MESSAGE = "<message><service id='single' source='"+alpha+"'/><to>1234</to><body content-type='text/plain'>test</body></message>";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            StringEntity entity = new StringEntity(MESSAGE, "UTF-8");
            entity.setContentType("text/xml");
            entity.setChunked(true);
            
            HttpPost httpPost = new HttpPost(SMS_SEND_URL);
            httpPost.setEntity(entity);
            httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(login, password), "UTF-8", false));

            HttpResponse response = httpclient.execute(httpPost);
            LOG.info( "Checking if Login, Password, Alpha Name are right:" + (response.getStatusLine().getStatusCode() == 200));
            if(EntityUtils.toString(response.getEntity()).contains("id=")) return true;
        } catch (Exception e) {
            LOG.severe( e.getMessage());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return false;
    }
    */
    
	/**
	 * Send SMS to Gateway
	 */
    @SuppressWarnings("deprecation")
	public  ArrayList<String> send(String phone, String sms) {
        ArrayList<String> result = new ArrayList<String>();
        final String MESSAGE = "<message><service id='single' source='" + FROM + "'/><to>" + phone + "</to><body content-type='text/plain'>" + sms + "</body></message>";
        
        @SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
        String xml = null;
        try {
            HttpPost httpPost = new HttpPost(SMS_SEND_URL);

            StringEntity entity = new StringEntity(MESSAGE, "UTF-8");
            entity.setContentType("text/xml");
            entity.setChunked(true);
            httpPost.setEntity(entity);
            httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(LOGIN, PASS), "UTF-8", false));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            LOG.info( "Sending SMS: " + (response.getStatusLine().getStatusCode()==200));
            xml = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            LOG.severe( ""+e.getStackTrace());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        //parsing xml result
        Document doc = loadXMLFromString(xml);
        NodeList nl = doc.getElementsByTagName("status");
        Element status = (Element) nl.item(0);

        result.add(0, status.getAttribute("id").toString()); //tracking id at position 0
        result.add(1, status.getAttribute("date").toString()); //date at position 1
        result.add(2, getElementValue(status.getFirstChild())); //state at position 2
        return result;
    }

	/*
	 * Check SMS State
	 */
    /*
    private  SMS checkState(SMS sms) {

        //ArrayList<String> result = new ArrayList<String>();
        final String MESSAGE = "<request id='" + sms.getTrackingId() + "'>status</request>";
        HttpClient httpclient = new DefaultHttpClient();
        String xml = null;
        try {
            HttpPost httpPost = new HttpPost(SMS_SATE_URL);

            StringEntity entity = new StringEntity(MESSAGE, "UTF-8");
            entity.setContentType("text/xml");
            entity.setChunked(true);

            httpPost.setEntity(entity);
            httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(LOGIN, PASS), "UTF-8", false));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            
            xml = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            LOG.severe( e.getMessage());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        //parsing xml result
        Document doc = loadXMLFromString(xml);
        NodeList nl = doc.getElementsByTagName("status");
        Element status = (Element) nl.item(0);
        sms.setStateString(getElementValue(status.getFirstChild())); //state at position 2
        //LOG.info(Utils.getElementValue(status.getFirstChild()));
        //LOG.info( "Checking SMS id = " + sms.getTrackingId() + " state: " + Utils.getElementValue(status.getFirstChild()));
        return sms;
    }
 */
	// Method generates XML document
	public static Document loadXMLFromString(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			LOG.severe( e.getMessage());
			return null;
		} catch (SAXException e) {
			LOG.severe( e.getMessage());
			return null;
		} catch (IOException e) {
			LOG.severe( e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	// Method retrieves nodes from XML
	public static final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}
    
    /*
     * This will update state of each SMS with state - 3
     *//*
	private  Boolean updateSMSState(Activity activity){
		
		// Initializing DB Object
		SMS_DAO smsDao = new SMS_DAO(activity);

		// Array with Objects
		List<SMS> smsList = smsDao.getAllSMS((byte) 3);
		
		if(smsList.isEmpty()) LOG.info( "No SMS for status update!");
		else {
	        if(!isNetworkConnected(activity) && !isInternetAvailable())Toast.makeText(activity, R.string.internet_connection_error, Toast.LENGTH_LONG).show(); 
	        else{
				for (SMS sms : smsList)
					smsDao.updateSMS(checkState(sms));
				LOG.info( "SMS status updated!");
				return true;
	        }
		}
		return false;
		
	}
	
	/*
     * Checks My License from doctorvera.kiev.ua
     */
    /*
	private Boolean isLicenseActive(String login){
        HttpClient httpclient = new DefaultHttpClient();
        String xml = null;
        try {
            HttpPost httpPost = new HttpPost("http://www.doctorvera.kiev.ua/DrVeraSMSLic.php");
            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("id", ID));
            params.add(new BasicNameValuePair("login", login));
            
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            
            LOG.info( "Requesting My License: " + (response.getStatusLine().getStatusCode()==200));
            xml = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            LOG.severe( e.getMessage());
            return false;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        //parsing xml result
        Document doc = Utils.loadXMLFromString(xml);
        NodeList nl = doc.getElementsByTagName("license");
        Element license = (Element) nl.item(0);
        LOG.info( "My License status is: "+license.getTextContent());
        return Boolean.valueOf(license.getTextContent());
	}

*/
	
}
