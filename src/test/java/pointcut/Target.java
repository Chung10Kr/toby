package pointcut;

/*
 * 스프링은 아주 간단하고 효과적인 방법으로 포인트컷의 클래스와 메소드를 선정하는 알고리즘을 작성할수 있는 방법을 제공한다.
 * 정규식이나 JSP,EL과 비슷한 일종의 표현식 언어를 사용해서 작성할 수 있도록 하는 방법이고
 * 이것을 포인트컷 표현식 이라고 부른다.
 * 
 * 포인트컷 표현식
 * AspectJExpressionPointcut은 클래스와 메소드의 선정 알고리즘을 포인트컷 표현식을 이용해 한 번에 지정할 수 있게 해준다.
 * 포인트컷 표현식은 자바의 RegEx클래스가 지원하는 정규식처럼 간단한 문자열로 복잡한 선정조건을 쉽게 만들어낼 수 있는 강력한 표현식을 지원한다.
 * 
 * 사실 스프링이 사용하는 포인트컷 표현식은 AspectJ라는 유명 프레임워크에서 제공하는 것을 일부 가져와 확장해서 사용하기 때문에 AspectJ포인트컷 표현식이라고도 한다.
 */

// 포인트 컷의 선정 후보가 될 여러 개의 메소드를 가진 Target 클래스
public class Target implements TargetInterface{

    @Override
    public void hello() {
    }

    @Override
    public void hello(String a) {
    }

    @Override
    public int minus(int a, int b) throws RuntimeException {
        return 0;
    }

    @Override
    public int plus(int a, int b) {
        return 0;
    }

    public void method(){
    }
}
