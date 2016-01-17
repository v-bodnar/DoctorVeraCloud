package ua.kiev.doctorvera.converters;

import ua.kiev.doctorvera.entities.MessageScheduler;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dayOfWeekConverter")
public class DayOfWeekConverter extends EnumConverter {
    public DayOfWeekConverter() {
        super(MessageScheduler.DayOfWeek.class);
    }
}