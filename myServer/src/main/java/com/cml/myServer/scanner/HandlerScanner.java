package com.cml.myServer.scanner;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.cml.myCommon.core.annotion.SocketModule;
import com.cml.myServer.scanner.Invoker;
import com.cml.myServer.scanner.InvokerHolder;
import com.cml.myCommon.core.annotion.SocketCommand;

public class HandlerScanner implements BeanPostProcessor{
    
	//原来就是return bean
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}
    
	//为什么要加一个after主要是为了根据这个bean的接口来添加InvokerHolder
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
        Class<? extends Object> clazz = bean.getClass();
		
        //可能有多个接口
		Class<?>[] interfaces = clazz.getInterfaces();
		if(interfaces != null && interfaces.length > 0){
			//扫描类的所有接口父类
			for (Class<?> interFace : interfaces) {
				//判断是否为handler接口类,通过getAnnotation方式
				//socketModule是类层次上的
				SocketModule socketModule = interFace.getAnnotation(SocketModule.class);
				if (socketModule == null) {
					continue;
				}
				
				//存在socketModule
				//找到socketModule后找出命令方法
				Method[] methods=interFace.getMethods();
				if(methods!=null&&methods.length>0) {
					for(Method method : methods) {			
						SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
					    if(socketCommand==null) {
					    	continue;
					    }
					    
					    final short module = socketModule.module();
						final short cmd = socketCommand.cmd();
						
					    if(InvokerHolder.getInvoker(module, cmd)==null) {
					    	//Invoker.valueOf先method后target
					    	InvokerHolder.addInvoker(module, cmd, Invoker.valueOf(method, bean));
					    }else {
					    	System.out.println("重复命令:"+"module:"+module +" "+"cmd：" + cmd);
					    }
					    
					}
				}
			}
			}
		 return bean;
			
		}
	}

