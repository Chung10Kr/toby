package proxy;

import java.lang.reflect.Proxy;

import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.lenient;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import net.sf.cglib.proxy.MethodProxy;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class DynamicProxyTest {
    

	public static void main(String[] args) {
		JUnitCore.main("proxy.DynamicProxyTest");
	}

    @Test
    public void simpleProxy(){
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{Hello.class},
            new UppercaseHandler(new HelloTarget())
        );

        assertThat( proxiedHello.sayHello("CRLEE"), is("HELLO CRLEE"));
		assertThat( proxiedHello.sayHi("CRLEE"), is("HI CRLEE"));
		assertThat( proxiedHello.sayThankYou("CRLEE"), is("THANK YOU CRLEE"));
    }

    @Test
    public void proxyFactoryBean(){
        ProxyFactoryBean pFactoryBean = new ProxyFactoryBean();
        pFactoryBean.setTarget(new HelloTarget()); // 타킷 설정
        pFactoryBean.addAdvice(new UppercaseAdvice()); // 부가 기능을 담은 어드바이스 추가, 여러개 추가 가능
        
        Hello proxiedHello = (Hello) pFactoryBean.getObject(); // FactoryBNean이므로 getObjecty()로 생성된 프록시를 가져온다.
        assertThat( proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat( proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat( proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));

    }

	static class UppercaseAdvice implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
	}


    @Test
    public void pointCutAdvisor(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget( new HelloTarget() );

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*"); // 이름 비교조건 설정 sayH로 시작하는 모든 메소드 선택

        pfBean.addAdvisors(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        // 그냥 어드바이스도 등록하고 포인트컷도 등록하면 될 것을 왜 굳이 별개의 오브젝트(어드바이저)로 묶어서 등록할까?
        // 여러개의 어드바이스를 등록할수있는데 그럼 이 포인트컷은 어떤 어드바이스에 대한 포인트컷인지 애매해지기 때문에
        // 어드바이저 = 어드바이스(부가기능) + 포인트컷(메소드 선정 알고리즘)
        
        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat( proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat( proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat( proxiedHello.sayThankYou("Toby"), is("Thank You Toby"));

    }
}
