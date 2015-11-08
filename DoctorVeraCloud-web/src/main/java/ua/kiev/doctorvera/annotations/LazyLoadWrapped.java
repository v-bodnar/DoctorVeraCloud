package ua.kiev.doctorvera.annotations;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Created by volodymyr.bodnar on 02.11.2015.
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LazyLoadWrapped {

}
