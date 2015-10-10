package ua.kiev.doctorvera.security;

/**
 * Created by volodymyr.bodnar on 25.09.2015.
 */

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    @Nonbinding
    SecurityPolicy value() default SecurityPolicy.EMPTY;
}
