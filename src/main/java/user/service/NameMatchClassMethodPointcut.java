package user.service;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut{

    public void setMappedClassName(String mappedName) {
        this.setClassFilter(new SimpleClassFilter(mappedName));
    }

    static class SimpleClassFilter implements ClassFilter{

        String mappedName;

        private SimpleClassFilter(String mappedName){
            this.mappedName = mappedName;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            return PatternMatchUtils.simpleMatch(mappedName,
                clazz.getSimpleName()
            );
        }
        
    }
    
}
