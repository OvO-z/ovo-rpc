package github.ovo.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @author QAQ
 * @date 2021/9/22
 */

public class CustomScanner extends ClassPathBeanDefinitionScanner {
    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }

    public CustomScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annoType) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annoType));
    }
}
