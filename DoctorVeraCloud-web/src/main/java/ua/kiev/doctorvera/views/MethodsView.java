package ua.kiev.doctorvera.views;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import ua.kiev.doctorvera.entities.MethodTypes;
import ua.kiev.doctorvera.entities.Methods;
import ua.kiev.doctorvera.entities.Prices;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.MethodTypesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.MethodsFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.PricesFacadeLocal;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Config;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

@Named(value="methodsView")
@ViewScoped
public class MethodsView implements Serializable {
	
	private final static Logger LOG = Logger.getLogger(MethodsView.class.getName());
	private final static Integer DOCTORS_TYPE_ID = Integer.parseInt(Config.getInstance().getProperty("DOCTORS_USER_GROUP_ID"));
	
	
	//Facade for CRUD operations with Users
	@EJB
	private UsersFacadeLocal usersFacade;
	
	@EJB
	private MethodsFacadeLocal methodsFacade;
	
	@EJB
	private PricesFacadeLocal pricesFacade;
	
	@EJB
	private MethodTypesFacadeLocal methodTypesFacade;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;
	
	private List<Methods> allMethods;
	
	private List<MethodTypes> allMethodTypes;
	
	private Methods selectedMethod;
	
	private Prices price;
	
	private List<Prices> prisesHistory;
	
	private Map<Methods, Prices> currentPrice;
	
	//Model for picklist PrimeFaces widget
	private DualListModel<Users> doctorsDualListModel;
	
	public MethodsView(){}
	
	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
		allMethods = methodsFacade.findAll();
		allMethodTypes = methodTypesFacade.findAll();
		this.price = new Prices();
		this.selectedMethod = new Methods();
		currentPrice = new HashMap<Methods, Prices>();
		
		for (Methods meth : allMethods)
			currentPrice.put(meth, pricesFacade.findLastPrice(meth));
		
		constructPickList();
	}
	
	public void constructPickList(){
		if (selectedMethod != null && selectedMethod.getId() != null){
			List<Users> allUsers = usersFacade.findByGroup(DOCTORS_TYPE_ID);
			List<Users> targetUsers = usersFacade.findByMethod(selectedMethod);
			for(Users user : targetUsers){
				allUsers.remove(user);
			}
			doctorsDualListModel = new DualListModel<Users>(allUsers, targetUsers);
		} else
			doctorsDualListModel = new DualListModel<Users>(new ArrayList<Users>(), new ArrayList<Users>());
	}

	public Users getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(Users authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public List<Methods> getAllMethods() {
		return allMethods;
	}

	public void setAllMethods(List<Methods> allMethods) {
		this.allMethods = allMethods;
	}

	public Methods getSelectedMethod() {
		return selectedMethod;
	}

	public void setSelectedMethod(Methods selectedMethod) {
		this.selectedMethod = selectedMethod;
	}

	public void initNewMethod() {
		resetSelectedMethod();
		resetPrice();
	}

	public List<MethodTypes> getAllMethodTypes() {
		return allMethodTypes;
	}

	public void setAllMethodTypes(List<MethodTypes> allMethodTypes) {
		this.allMethodTypes = allMethodTypes;
	}

	public Prices getPrice() {
		return price;
	}

	public void setPrice(Prices price) {
		this.price = price;
	}

	public List<Prices> getPrisesHistory() {
		return prisesHistory;
	}

	public void setPrisesHistory(List<Prices> prisesHistory) {
		this.prisesHistory = prisesHistory;
	}

	public Map<Methods, Prices> getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Map<Methods, Prices> currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public DualListModel<Users> getDoctorsDualListModel() {
		return doctorsDualListModel;
	}

	public void setDoctorsDualListModel(DualListModel<Users> doctorsDualListModel) {
		this.doctorsDualListModel = doctorsDualListModel;
	}

	public void deleteSelectedMethod(){
		//Deleting MethodType if this Method is the last 
		if(methodsFacade.findByType(selectedMethod.getMethodType()).size() == 1)
			methodTypesFacade.remove(selectedMethod.getMethodType());
		
		methodsFacade.remove(selectedMethod);
		allMethods.remove(selectedMethod);
		LOG.info("Method " + selectedMethod.getShortName() + " with id: " + selectedMethod.getId() + "removed");
		final String successMessage = Message.getInstance().getString("METHODS_DELETED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}	
	
    public void saveSelectedMethod() {
		selectedMethod.setDateCreated(new Date());
		selectedMethod.setUserCreated(authorizedUser);
		methodsFacade.edit(selectedMethod);
        cleanMethodTypes();
		LOG.info("Changed method " + selectedMethod.getShortName() + " with id: " + selectedMethod.getId());
		final String successMessage = Message.getInstance().getString("METHODS_EDITED");
		final String successTitle = Message.getInstance().getString("VALIDATOR_SUCCESS_TITLE");
		RequestContext.getCurrentInstance().update("methodsForm:methodsTable");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addMethodDialog').hide();");
    }
    
	public void createNewMethod(){
        selectedMethod.setDateCreated(new Date());
        selectedMethod.setUserCreated(authorizedUser);
		methodsFacade.create(selectedMethod);
		allMethods.add(selectedMethod);
		price.setDateCreated(new Date());
		price.setUserCreated(authorizedUser);
		price.setMethod(selectedMethod);
		price.setDateTime(new Date());
		pricesFacade.create(price);
		LOG.info("Created new method: " + selectedMethod.getShortName());
		final String successTitle = Message.getInstance().getString("APPLICATION_SAVED");
		final String successMessage = Message.getInstance().getString("METHODS_SAVED");
		RequestContext.getCurrentInstance().update("methodsForm:methodsTable");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addMethodDialog').hide();");
	}
	
    public void createNewPrice(){
		price.setDateCreated(new Date());
		price.setUserCreated(authorizedUser);
		price.setMethod(selectedMethod);
		pricesFacade.create(price);
		LOG.info("Price " + price.getTotal() + " for method " + selectedMethod.getShortName() + " changed, changes will take place " + price.getDateTime());
		final String successTitle = Message.getInstance().getString("APPLICATION_SAVED");
		final String successMessage = Message.getInstance().getString("METHODS_PRICE_CREATED");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
		RequestContext.getCurrentInstance().execute("PF('addPriceDialog').hide();");
    }
    
	//This method controls onTransfer event in the Pick List
	public void onTransfer(TransferEvent event){

		
		//All Users from the right picker
		List<Users> targetList = doctorsDualListModel.getTarget();
		
		//Indicates to add new method to user transfered or to remove method from user
		Boolean addFlag = false;
		
		//Checking transfer direction
		if(targetList != null && targetList.contains(event.getItems().get(0))) 
			addFlag = true; //Means that user transfered from left picker to right picker
			
		//Constructing success message
		final String successTitle = Message.getInstance().getString("METHODS_SAVED");
		String successMessage;
		if(targetList != null && targetList.contains(event.getItems().get(0)))
			successMessage = Message.getInstance().getString("METHODS_ADD_SUCCESS_START");
		else
			successMessage = Message.getInstance().getString("METHODS_REMOVE_SUCCESS_START");
		
		//Iterating each transfered user
		for(Object userObject : event.getItems()){
			Users userTransferred = usersFacade.initializeLazyEntity((Users)userObject);
			//Constructing success message
			successMessage += userTransferred.getFirstName() + " " + userTransferred.getLastName() + ", ";
			
			if(addFlag){
				//Add method to user transfered
				//usersFacade.addMethod(userTransferred, selectedMethod, authorizedUser);
				userTransferred.getMethods().add(selectedMethod);

				//Setting time and user that made changes
				userTransferred.setUserCreated(authorizedUser);
				selectedMethod.setUserCreated(authorizedUser);
				userTransferred.setDateCreated(new Date());
				selectedMethod.setDateCreated(new Date());
				usersFacade.edit(userTransferred);
			}else{
				//Remove method from user transfered
				//usersFacade.removeMethod(userTransferred, selectedMethod);
				userTransferred.getMethods().remove(selectedMethod);
				//Setting time and user that made changes
				userTransferred.setUserCreated(authorizedUser);
				selectedMethod.setUserCreated(authorizedUser);
				userTransferred.setDateCreated(new Date());
				selectedMethod.setDateCreated(new Date());
				usersFacade.edit(userTransferred);
				//methodsFacade.edit(selectedMethod);
			}
		}
		
		//Constructing success message
		if(addFlag)
			successMessage += " " + Message.getInstance().getString("METHODS_ADD_SUCCESS_END") + " " + selectedMethod.getShortName();
		else
			successMessage += " " + Message.getInstance().getString("METHODS_REMOVE_SUCCESS_END") + " " + selectedMethod.getShortName();
		
		LOG.info(successMessage);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successTitle, successMessage ));
	}
	
    private void resetSelectedMethod() {
    	selectedMethod = new Methods();
    }
    
    public void resetPrice() {
    	price = new Prices();
    	prisesHistory = pricesFacade.findByMethod(selectedMethod);
    }

    private void cleanMethodTypes(){
        for(MethodTypes type : methodTypesFacade.findAll())

        if(methodsFacade.findByType(type).size() == 0)
            methodTypesFacade.remove(type);
    }
    

}
