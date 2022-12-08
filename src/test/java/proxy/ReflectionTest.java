package proxy;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class ReflectionTest {

	public static void main(String[] args) {
		JUnitCore.main("proxy.ReflectionTest");
	}

	@Test
	public void invokeMethod() throws Exception{
		String name = "Spring";

		Method lengMethod = String.class.getMethod("length");
		assertThat( (Integer) lengMethod.invoke(name) , is(6));
		assertThat( name.charAt(0) , is('S') );

		Method charAMethod = String.class.getMethod("charAt",int.class);
		assertThat( (Character) charAMethod.invoke(name,0), is('S') );
		
	}

	@Test
	public void simpleProxy(){
		Hello hello = new HelloTarget();
		assertThat( hello.sayHello("CRLEE"), is("Hello CRLEE"));
		assertThat( hello.sayHi("CRLEE"), is("Hi CRLEE"));
		assertThat( hello.sayThankYou("CRLEE"), is("Thank You CRLEE"));

		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		assertThat( proxiedHello.sayHello("CRLEE"), is("HELLO CRLEE"));
		assertThat( proxiedHello.sayHi("CRLEE"), is("HI CRLEE"));
		assertThat( proxiedHello.sayThankYou("CRLEE"), is("THANK YOU CRLEE"));
	}

	@Test
	public void dynamicProxy(){
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(
			getClass().getClassLoader(), //동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더
			new Class[]{Hello.class}, // 구현할 인터파에스
			new UppercaseHandler(new HelloTarget()) // 부가 기능과 위임 코드를 담은 InvocationHandler
			);
		assertThat( proxiedHello.sayHello("CRLEE"), is("HELLO CRLEE"));
		assertThat( proxiedHello.sayHi("CRLEE"), is("HI CRLEE"));
		assertThat( proxiedHello.sayThankYou("CRLEE"), is("THANK YOU CRLEE"));
	}
}

