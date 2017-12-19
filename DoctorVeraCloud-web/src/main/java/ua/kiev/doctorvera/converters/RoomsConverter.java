package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.Rooms;
import ua.kiev.doctorvera.facadeLocal.RoomsFacadeLocal;
import ua.kiev.doctorvera.resources.Message;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "roomsConverter")
public class RoomsConverter implements Converter {

    @EJB
    private RoomsFacadeLocal roomsFacade = (RoomsFacadeLocal) new InitialContext().lookup("java:global/DoctorVeraCloud-ear-0.0.1-SNAPSHOT/DoctorVeraCloud-ejb-0.0.1-SNAPSHOT/RoomsFacade");

    public RoomsConverter() throws NamingException {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals(Message.getMessage("APPLICATION_SELECT_ONE"))){
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