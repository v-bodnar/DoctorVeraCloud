package ua.kiev.doctorvera.converters;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.util.TimeZone;

/**
 * Created by volodymyr.bodnar on 4/25/2016.
 */
@FacesConverter("defaultTimeConverter")
public class DefaultTimeConverter extends DateTimeConverter {

    public DefaultTimeConverter() {
        setType("time");
        setPattern("HH:mm");
        setTimeZone(TimeZone.getDefault());
    }

}