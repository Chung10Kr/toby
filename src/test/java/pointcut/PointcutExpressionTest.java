package pointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutExpressionTest {


	public static void main(String[] args) {
		JUnitCore.main("pointcut.PointcutExpressionTest");
	}

    /*
     * AspectJ 포인트컷 표현식은 포인트컷 지시자를 이용해 작성한다.
     * 지시자 중 가장 대표적으로 사용되는 것은 execution()이다.
     * execution([접근제한자] 타입패턴 [타입패턴.]이름패턴 (타입패턴|"..",...) )
     * ex)
     * 
     */

    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException, SecurityException{

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* minus(int,int))");
        //execution( int minus(int,int)) -> return int , minus라는 메소드 이름, 두개의 int 파라미터를 가진 모든 메소드를 선정하는 포인트컷 표현식
        //execution( * minus(int,int)) -> return 아무거나 , minus라는 메소드 이름, 두개의 int 파라미터를 가진 모든 메소드를 선정하는 포인트컷 표현식
        //execution( * minus(..)) -> return 아무거나 , minus라는 메소드 이름, 파라미터 갯수 상관없는 모든 메소드를 선정하는 포인트컷 표현식
		
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
				   pointcut.getMethodMatcher().matches(
					  Target.class.getMethod("minus", int.class, int.class), null), is(true));
                      // is(true) -> 포인트컷 통과
		
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
				   pointcut.getMethodMatcher().matches(
					  Target.class.getMethod("plus", int.class, int.class), null), is(false));
                      // is(false) -> 메소드 매처에서 실패

		assertThat(pointcut.getClassFilter().matches(Bean.class) &&
				pointcut.getMethodMatcher().matches(
						Target.class.getMethod("method"), null), is(false));
                        // is(false) -> 클래스 필터에서 실패

    }

    public void pointcutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		
        // 포인트컷의 클래스 필터, 메소드 매처 두가지를 동시에 만족하는지 확인함.
		assertThat(pointcut.getClassFilter().matches(clazz) 
				   && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null),
				   is(expected));
	}
	
    public void tagetClassPointcutMatches(String expression, boolean... expected) throws Exception {
		pointcutMatches(expression, expected[0], Target.class, "hello");
		pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
		pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
		pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
		pointcutMatches(expression, expected[4], Target.class, "method");
		pointcutMatches(expression, expected[5], Bean.class, "method");
	}

    @Test
	public void pointcut() throws Exception {

        //모든 리턴, 모튼 메서드, 파라미터 제한 없음
		tagetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);

        //모든 리턴, hello메서드, 파라미터 제한 없음
		tagetClassPointcutMatches("execution(* hello(..))", true, true, false, false, false, false);

        //모든 리턴, hello메서드, 파라미터 없음
		tagetClassPointcutMatches("execution(* hello())", true, false, false, false, false, false);

        //모든 리턴, hello메서드, 1개 스트링 파라미터
		tagetClassPointcutMatches("execution(* hello(String))", false, true, false, false, false, false);

		//모든 리턴, meth*로 시작하는 메서드, 파라미터 제한 상관 No
		tagetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true, true);

		//모든 리턴, 모든 메섣, 2개 int 파라미터
		tagetClassPointcutMatches("execution(* *(int,int))", false, false, true, true, false, false);

		//모든 리턴, 모든 메섣, 모든 파라미터
		tagetClassPointcutMatches("execution(* *())", true, false, false, false, true, true);
		
		//모든 리턴, Target클래스의 메서드, 모든 파라미터
		tagetClassPointcutMatches("execution(* pointcut.Target.*(..))", true, true, true, true, true, false);

		//모든 리턴, pointcut 아래의 모든 클래스, 모든 파라미터
		tagetClassPointcutMatches("execution(* pointcut.*.*(..))", true, true, true, true, true, true);

		//모든 리턴, pointcut 아래의 모든 클래스, 모든 파라미터
		tagetClassPointcutMatches("execution(* pointcut..*.*(..))", true, true, true, true, true, true);

		//모든 리턴, com 아래의 모든 클래스, 모든 파라미터
		tagetClassPointcutMatches("execution(* com..*.*(..))", false, false, false, false, false, false);
		
		tagetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
		tagetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
		tagetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
		tagetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
		tagetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);

		//메소드와 클래스와는 상관없이 예외 선언만 확인해서 메소드를 선정하는 포인트컷
		//Runtime으로 시작하는 어떤 예외라도 던진다면 만족함
		tagetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true);
		
		tagetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false);
		tagetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true);
        
	}
}
