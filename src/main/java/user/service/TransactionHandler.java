package user.service;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler{

    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;

    public void setTarget(Object target) {
        this.target = target;
    }
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if( method.getName().startsWith(pattern)){
            return invokeInTransactino(method, args);
        }
        return method.invoke(target, args);

    }
    public Object invokeInTransactino(Method method, Object[] args) throws Throwable{
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try{
            Object ret = method.invoke(target, args);
			this.transactionManager.commit(status);
            return ret;
		}catch(InvocationTargetException e){
			this.transactionManager.rollback(status);
            throw e.getTargetException();
		}
    }

    
}
