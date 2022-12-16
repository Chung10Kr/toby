package proxy;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.lenient;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class ReflectionTest {

	public static void main(String[] args) {
		JUnitCore.main("proxy.ReflectionTest");
	}
	/* 리플렉션(투영,반사) : 객체를 통해 클래스으 ㅣ정보를 분석해 내는 프로그램 기법
	 * 
	 * 객체의 메모리만 알고 있고, 객체의 형에 대해서 모를때
	 * 리플렉션으로 형은 알고 있지만 형변환은 할 수 없는 상태에서 객체의 메서드를 호출 할 수 있다.
	 */
	@Test
	public void reflectTest() throws Exception{

		// 클래스 정보 얻기 1
		Class clazz1 = Class.forName("proxy.Car");
		// 클래스 정보 얻기 2
		Class clazz2 = Car.class;


		//getDeclaredMethods 메소드 리스트 조회
		//getMethods 를 사용하면 상속된 메소드에 대한 정보를 얻을 수 있다.
		Method methods[] = clazz1.getDeclaredMethods(); 

		for(Method method : methods){
			System.out.println("==========");
			System.out.printf("name = %s \n" , method.getName());
			System.out.printf("decl class = %s \n" , method.getDeclaringClass());
			Class pvecs[] = method.getParameterTypes();
			for( Class pvec : pvecs){
				System.out.printf("param  = %s\n" , pvec );
			}

			Class evecs[] = method.getExceptionTypes();
			for( Class evec : evecs){
				System.out.printf("exec  = %s\n" , evec );
			}
			
			System.out.printf("return Type  = %s\n" , method.getReturnType() );
			System.out.println("==========\n");
		};

		

		//constructors 찾기
		Constructor constructors[] = clazz1.getDeclaredConstructors();
		for(Constructor constructor : constructors){
			System.out.println("*******************");
			System.out.printf("name  = %s\n" , constructor.getName() );
			System.out.printf("name  = %s\n" , constructor.getDeclaringClass() );
			System.out.println("*******************\n");

			Class pvecs[] = constructor.getParameterTypes();
			for(Class pvec : pvecs){
				System.out.printf("param  = %s\n" , pvec );
			}

			Class evecs[] = constructor.getExceptionTypes();
			for(Class evec : evecs){
				System.out.printf("exec  = %s\n" , evec );
			}
		}


		Field fields[] = clazz1.getDeclaredFields();
		for(Field field : fields){
			System.out.println("#################");
			System.out.printf("name  = %s\n" , field.getName() );
			System.out.printf("decl class  = %s\n" , field.getDeclaringClass() );
			System.out.printf("type  = %s\n" , field.getType() );
			int mod = field.getModifiers();
			System.out.printf("modifiers  = %s\n" , Modifier.toString(mod));
			System.out.println("#################\n");
		}

		// 메서드 실행
		Car car = new Car();
		Method stopMethod = clazz1.getMethod("stop");
		stopMethod.invoke(car);

		Class parType[] = new Class[2];
		parType[0] = String.class;
		parType[1] = Integer.TYPE;

		Method fixMethod = clazz1.getMethod("fix", parType);

		Object args[] = new Object[2];
		args[0] = "benz";
		args[1] = 3;
		fixMethod.invoke(car,args);
		Method hiMethod = clazz1.getMethod("HI");	
		hiMethod.invoke(car);

		//필드값 바꾸기
		Field field = clazz1.getField("doors");
		Car car2 = new Car();
		System.out.println(car2.doors);
		field.setInt(car2,99);
		System.out.println(car2.doors);
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

