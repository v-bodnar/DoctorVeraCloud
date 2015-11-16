package ua.kiev.doctorvera.views;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named(value = "addressView")
@ViewScoped
public class AddressView implements Serializable {
	
	@EJB
	private AddressFacadeLocal addressFacade;
	
	private ArrayList<Address> addressList;

	//This lists will be used for autocomplete
	//Sets because I don't want duplicates
	private Set<String> countries = new HashSet<>();
	private Set<String> regions = new HashSet<>();
	private Set<String> cities = new HashSet<>();
	private Set<String> addresses = new HashSet<>();
	private Set<String> indexes = new HashSet<>();
	
	@PostConstruct
	public void init(){
		addressList = (ArrayList<Address>)addressFacade.findAll();
		for(Address addr : addressList){
			countries.add(addr.getCountry());
			regions.add(addr.getRegion());
			cities.add(addr.getCity());
			addresses.add(addr.getAddress());
			indexes.add("" + addr.getPostIndex());
		}
	}

	public ArrayList<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<Address> addressList) {
		this.addressList = addressList;
	}

	public List<String> completeCountries(String letters) {
		if (letters.isEmpty()) return null;
		List<String> result = new ArrayList<>();
		for(String name : countries){
			if(name != null && name.toLowerCase().startsWith(letters.toLowerCase())) {
				result.add(name);
			}
		}
		return result;
	}

	public List<String> completeRegions(String letters) {
		if (letters.isEmpty()) return null;
		List<String> result = new ArrayList<>();
		for(String name : regions){
			if(name != null && name.toLowerCase().startsWith(letters.toLowerCase())) {
				result.add(name);
			}
		}
		return result;
	}

	public List<String> completeCities(String letters) {
		if (letters.isEmpty()) return null;
		List<String> result = new ArrayList<>();
		for(String name : cities){
			if(name != null && name.toLowerCase().startsWith(letters.toLowerCase())) {
				result.add(name);
			}
		}
		return result;
	}

	public List<String> completeAddresses(String letters) {
		if (letters.isEmpty()) return null;
		List<String> result = new ArrayList<>();
		for(String name : addresses){
			if(name != null && name.toLowerCase().startsWith(letters.toLowerCase())) {
				result.add(name);
			}
		}
		return result;
	}

	public List<String> completeIndexes(String letters) {
		if (letters.isEmpty()) return null;
		List<String> result = new ArrayList<>();
		for(String name : indexes){
			if(name != null && name.toLowerCase().startsWith(letters.toLowerCase())) {
				result.add(name);
			}
		}
		return result;
	}

}
