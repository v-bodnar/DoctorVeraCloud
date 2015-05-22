package ua.kiev.doctorvera.managedbeans;

import ua.kiev.doctorvera.entities.Address;
import ua.kiev.doctorvera.facadeLocal.AddressFacadeLocal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.HashSet;

public class AddressView {
	
	@EJB
	private AddressFacadeLocal addressFacade;
	
	private ArrayList<Address> addressList;
	
	private ArrayList<String> countries;
	
	private ArrayList<String> regions;
	
	private ArrayList<String> cities;	
	
	private ArrayList<String> addresses;
	
	private ArrayList<String> indexes;
	
	@PostConstruct
	public void init(){
		addressList = (ArrayList<Address>)addressFacade.findAll();
		
		HashSet<String> countriesSet = new HashSet<String>();
		HashSet<String> regionsSet = new HashSet<String>();
		HashSet<String> citiesSet = new HashSet<String>();
		HashSet<String> addressesSet = new HashSet<String>();
		HashSet<String> indexesSet = new HashSet<String>();
			
		for(Address addr : addressList){
			countriesSet.add(addr.getCountry());
			regionsSet.add(addr.getRegion());
			citiesSet.add(addr.getCity());
			addressesSet.add(addr.getAddress());
			indexesSet.add(""+addr.getPostIndex());
		}
		
		countries = new ArrayList<String>(countriesSet);
		regions = new ArrayList<String>(regionsSet);
		cities = new ArrayList<String>(citiesSet);
		addresses = new ArrayList<String>(addressesSet);
		indexes = new ArrayList<String>(indexesSet);
		
		
	}

	public ArrayList<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<Address> addressList) {
		this.addressList = addressList;
	}

	public ArrayList<String> getCountries() {
		return countries;
	}

	public ArrayList<String> getCountries(String value) {
		return countries;
	}
	
	public void setCountries(ArrayList<String> countries) {
		this.countries = countries;
	}

	public ArrayList<String> getRegions() {
		return regions;
	}
	
	public ArrayList<String> getRegions(String value) {
		return regions;
	}

	public void setRegions(ArrayList<String> regions) {
		this.regions = regions;
	}

	public ArrayList<String> getCities() {
		return cities;
	}
	public ArrayList<String> getCities(String value) {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public ArrayList<String> getAddresses() {
		return addresses;
	}
	public ArrayList<String> getAddresses(String value) {
		return addresses;
	}

	public void setAddresses(ArrayList<String> addresses) {
		this.addresses = addresses;
	}

	public ArrayList<String> getIndexes() {
		return indexes;
	}
	public ArrayList<String> getIndexes(String value) {
		return indexes;
	}

	public void setIndexes(ArrayList<String> indexes) {
		this.indexes = indexes;
	}
	
}
