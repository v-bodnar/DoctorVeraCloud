package ua.kiev.doctorvera.annotations;

import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

/**
 * Created by volodymyr.bodnar on 02.11.2015.
 */
@LazyLoadWrapped
@Interceptor
public class LazyLoadWrapperInterceptor implements Serializable {
    @EJB
    UsersFacadeLocal usersFacade;

    public LazyLoadWrapperInterceptor(){}

    @AroundInvoke
    public Object openConnection(InvocationContext invocationContext) throws Exception{
        System.out.println("before invocation of test");
        EntityTransaction transaction = usersFacade.getEntityManager().getTransaction();
        transaction.begin();
        Object object = invocationContext.proceed();
        System.out.println("after invocation of test");
        transaction.commit();
        return object;
    }
}
