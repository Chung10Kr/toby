package FactoryBean;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/FacotryBeanTest-applicationContext.xml")
public class FacotryBeanTest {

	@Autowired
	ApplicationContext context;

	public static void main(String[] args) {
		JUnitCore.main("FactoryBean.FacotryBeanTest");
	}

	@Test
	public void getMessageFromFactoryBean() throws Exception{
		Object message = context.getBean("message");
		assertThat( message, is(Message.class) );
		assertThat( ((Message)message).getText() , is("Factory Bean") );
	}

}

