package com.cml.server.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//执行器成员对象和Method
public class Invoker {
   
	private Method method;
	
	private Object target;
	
	//自定义的一个初始化
    public static Invoker valueOf(Method method,Object target) {
		// TODO Auto-generated method stub
        Invoker invoker=new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
	}
    
    /**
	 * 执行
	 * @param paramValues
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
    public Object invoke(Object... paramValues){
		try {
			//invoke(Object obj, Object... args)
			return method.invoke(target, paramValues);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
    
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
    
    
}
