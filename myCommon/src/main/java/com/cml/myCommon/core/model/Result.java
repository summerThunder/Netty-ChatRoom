package com.cml.myCommon.core.model;

import com.cml.myCommon.core.model.Result;
import com.cml.myCommon.core.model.ResultCode;
import com.cml.myCommon.core.serial.Serializer;

//自定义了一个Serializer
//用泛型形成一种包装类的效果
public class Result<T extends Serializer>{
   private int resultCode;
   
   private T content;
   
   public static <T extends Serializer> Result<T> SUCCESS(T content) {
	   Result<T> result = new Result<T>();
		result.resultCode = ResultCode.SUCCESS;
		result.content = content;
		return result;
   }
   public static <T extends Serializer> Result<T> SUCCESS(){
		Result<T> result = new Result<T>();
		result.resultCode = ResultCode.SUCCESS;
		return result;
	}
   
   public static <T extends Serializer> Result<T> ERROR(int resultCode){
		Result<T> result = new Result<T>();
		result.resultCode = resultCode;
		return result;
	}
   public static <T extends Serializer> Result<T> valueOf(int resultCode, T content){
	   Result<T> result=new Result<>();
	   result.resultCode=resultCode;
	   result.content=content;
	   return result;
   }
   public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
	
	public boolean isSuccess(){
		return this.resultCode == ResultCode.SUCCESS;
	}

   
   
   
}
