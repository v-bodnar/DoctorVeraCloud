package ua.kiev.doctorvera.managedbeans;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.context.RequestContext;
 
public class CropperView {
        
    public void viewCropper() {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("minHeight", 450);
        options.put("closeOnEscape", true);
        
        RequestContext.getCurrentInstance().openDialog("crop_image", options, null);
    }
}