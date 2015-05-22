package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "roomsConverter")
public class RoomsConverter implements Converter{

	@EJB
	private RoomsFacadeLocal roomsFacade;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        return roomsFacade.find(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        return ((Rooms)value).getId().toString();
    }
}