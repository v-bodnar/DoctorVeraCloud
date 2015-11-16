package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "roomsConverter")
public class RoomsConverter implements Converter {

    @EJB
    private RoomsFacadeLocal roomsFacade;
    private static final String SELECTOR_DEFAULT = Message.getMessage("APPLICATION_SELECT_ONE");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals(SELECTOR_DEFAULT)){
            return roomsFacade.find(Integer.parseInt(value));
        }else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null){
            return ((Rooms) value).getId().toString();
        }else {
            return "";
        }

    }
}