package com.cml.server.scanner;

import java.util.HashMap;
import java.util.Map;

import com.cml.server.scanner.Invoker;

//执行器管理器
public class InvokerHolder {
	 /**命令调用器*/
	//<module,<cmd,invoker>三元组
    private static Map<Short, Map<Short, Invoker>> invokers = new HashMap<>();
    
    /**
     * 添加命令调用
     * @param module
     * @param cmd
     * @param invoker
     */
    //module?cmd? 玩家模块。。。
    public static void addInvoker(short module,short cmd,Invoker invoker) {
    	Map<Short,Invoker> map=invokers.get(module);
    	//如果对应module不存在，创建一个module的空map
    	if(map==null) {
    		map = new HashMap<>();
    		invokers.put(module, map);
    	}
    	map.put(cmd, invoker);
    }
    
    /**
     * 获取命令调用
     * @param module
     * @param cmd
     * @param invoker
     */
    public static Invoker getInvoker(short module, short cmd){
    	Map<Short, Invoker> map = invokers.get(module);
    	if(map != null){
    		return map.get(cmd);
    	}
    	return null;
    }
    
    
    
    
}
