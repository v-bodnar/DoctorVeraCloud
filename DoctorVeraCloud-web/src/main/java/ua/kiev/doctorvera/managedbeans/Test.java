package ua.kiev.doctorvera.managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Test {

	public String name = "temp";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void save(){
		//FacesMessage message = new FacesMessage("Succesfuly updated");
		//FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
