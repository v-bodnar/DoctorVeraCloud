package ua.kiev.doctorvera.security;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.views.SessionParams;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 21.09.2015.
 */
@Secure
@Interceptor
public class SecurityInterceptor implements Serializable{
    @Inject
    SessionParams sessionParams;

    @Inject
    SecurityUtils securityUtils;

    public SecurityInterceptor(){}

    @AroundInvoke
    public Object checkPermissions(InvocationContext invocationContext) throws Exception{
        SecurityPolicy policy = invocationContext.getMethod().getAnnotation(Secure.class).value();
        if(!securityUtils.checkPermissions(policy)){
            showAccessViolationMessage(policy);
            return null;
        }else{
            return invocationContext.proceed();
        }
    }

    private void showAccessViolationMessage(SecurityPolicy policy) throws SecurityException{
        Users currentUser = sessionParams.getAuthorizedUser();
        final String message = Message.getMessage("SECURITY_VIOLATION_MESSAGE_TITLE");
        final String messageTitle = Message.getMessage("SECURITY_VIOLATION_MESSAGE_START") +
                " " +currentUser.getFirstName() + " " + currentUser.getLastName() + " " +
                Message.getMessage("SECURITY_VIOLATION_MESSAGE_END") + " " + policy.name();
        Message.showMessage(messageTitle, message);

    }
}