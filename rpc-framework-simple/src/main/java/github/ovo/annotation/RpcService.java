package github.ovo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author QAQ
 * @date 2021/9/21
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Component
public @interface RpcService {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";
}
