package com.cml.myCommon.core.serial;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cml.myCommon.core.serial.BufferFactory;
import com.cml.myCommon.core.serial.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

//提供一个类用于读写，内部有两个buf供使用
//需要自定义，因为有协议
public abstract class Serializer {
   public static final Charset CHARSET = Charset.forName("UTF-8");
   
   //抽象类中的属性和希望实现的方法一般写作protected
   
   
   protected ByteBuf writeBuffer;
   protected ByteBuf readBuffer;
   
   /**
	 * 反序列化具体实现
	 */
   //其实有约束是从readBuffer里读
	protected abstract void read();
	
	/**
	 * 序列化具体实现
	 */
	protected abstract void write();
	
	/**
	 * 从byte数组获取数据
	 * @param bytes	读取的数组
	 */
	
	//在具体方法中有调有抽象方法
	
	public Serializer readFromBytes(byte[] bytes) {
		readBuffer=BufferFactory.getBuffer(bytes);
		read();
		readBuffer.clear();
		
		//工厂函数生产完马上回收，clear并没有回收
		ReferenceCountUtil.release(readBuffer);
		return this;
	}
	
	public void readFromBuffer(ByteBuf readBuffer) {
		this.readBuffer=readBuffer;
		//因为read只针对类中的这块内存写
		read();
	}
	
	/**
	 * 写入本地buff
	 * @return
	 */
	//相比写入目标buff,少一个参数
	public ByteBuf writeToLocalBuff(){
		writeBuffer = BufferFactory.getBuffer();
		write();
		return writeBuffer;
	}
	
	/**
	 * 写入目标buff
	 * @param buffer
	 * @return
	 */
	public ByteBuf writeToTargetBuff(ByteBuf buffer){
		writeBuffer = buffer;
		write();
		return writeBuffer;
	}
	
	public byte[] getBytes() {
		writeToLocalBuff();
		byte[] bytes = null;
		if (writeBuffer.writerIndex() == 0) {
			bytes = new byte[0];
		} else {
			bytes = new byte[writeBuffer.writerIndex()];
			writeBuffer.readBytes(bytes);
		}
		writeBuffer.clear();
		//释放buffer
		ReferenceCountUtil.release(writeBuffer);
		return bytes;
	}

	
	public byte readByte() {
		return readBuffer.readByte();
	}

	public short readShort() {
		return readBuffer.readShort();
	}

	public int readInt() {
		return readBuffer.readInt();
	}

	public long readLong() {
		return readBuffer.readLong();
	}

	public float readFloat() {
		return readBuffer.readFloat();
	}

	public double readDouble() {
		return readBuffer.readDouble();
	}
	
	public String readString() {
		int size=readBuffer.readShort();
		if (size <= 0) {
			return "";
		}
		byte[] bytes = new byte[size];
		readBuffer.readBytes(bytes);

		return new String(bytes, CHARSET);
		
	}
	
	//先读列表长度，再调用读对象的
	public <T> List<T> readList(Class<T> clz){
		List<T> list = new ArrayList<>();
		int size=readBuffer.readShort();
		for(int i=0;i<size;i++) {
			list.add(read(clz));
		}
		return list;
	}
	
	//读map，一个键，一个值
	public <K,V> Map<K,V> readMap(Class<K> keyClz,Class<V> valueClz){
		Map<K,V> map=new HashMap<>();
		int size=readBuffer.readShort();
		for(int i=0;i<size;i++) {
			K key=read(keyClz);
			V value = read(valueClz);
			map.put(key, value);
		}
		return map;
	}
	
	
	//返回值是泛型的写法
	public <T> T read(Class<T> clz) {
		Object t = null;
		//int和Integer类文件不一样
		if(clz==int.class||clz==Integer.class) {
			t=this.readInt();
		}else if (clz == byte.class || clz == Byte.class){
			t = this.readByte();
		} else if (clz == short.class || clz == Short.class){
			t = this.readShort();
		} else if (clz == long.class || clz == Long.class){
			t = this.readLong();
		} else if (clz == float.class || clz == Float.class){
			t = readFloat();
		} else if (clz == double.class || clz == Double.class){
			t = readDouble();
		} else if (clz == String.class ){
			t = readString();
		}else if(Serializer.class.isAssignableFrom(clz)) {
			try {
				byte hasObject=this.readBuffer.readByte();
				if(hasObject==1) {
					//temp的两个buffer还是空的
					Serializer temp=(Serializer)clz.newInstance();
					temp.readFromBuffer(this.readBuffer);
					t=temp;
				}else {
					t=null;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}		
		}else {
			throw new RuntimeException(String.format("不支持类型:[%s]", clz));
		}
		return (T)t;
	}
	
	public Serializer writeByte(Byte value) {
		writeBuffer.writeByte(value);
		return this;
	}

	public Serializer writeShort(Short value) {
		writeBuffer.writeShort(value);
		return this;
	}

	public Serializer writeInt(Integer value) {
		writeBuffer.writeInt(value);
		return this;
	}

	public Serializer writeLong(Long value) {
		writeBuffer.writeLong(value);
		return this;
	}

	public Serializer writeFloat(Float value) {
		writeBuffer.writeFloat(value);
		return this;
	}

	public Serializer writeDouble(Double value) {
		writeBuffer.writeDouble(value);
		return this;
	}
	private <T> boolean isEmpty(Collection<T> c) {
		return c == null || c.size() == 0;
	}
	public <K,V> boolean isEmpty(Map<K,V> c) {
		return c == null || c.size() == 0;
	}
	
	public <T> Serializer writeList(List<T> list) {
		if (isEmpty(list)) {
			writeBuffer.writeShort((short) 0);
			return this;
		}
		writeBuffer.writeShort((short) list.size());
		for (T item : list) {
			writeObject(item);
		}
		return this;
	}
	public Serializer writeString(String value) {
		if (value == null || value.isEmpty()) {
			writeShort((short) 0);
			return this;
		}

		byte data[] = value.getBytes(CHARSET);
		short len = (short) data.length;
		writeBuffer.writeShort(len);
		writeBuffer.writeBytes(data);
		return this;
	}
	
	public <K,V> Serializer writeMap(Map<K, V> map) {
		if (isEmpty(map)) {
			writeBuffer.writeShort((short) 0);
			return this;
		}
		writeBuffer.writeShort((short) map.size());
		for (Entry<K, V> entry : map.entrySet()) {
			writeObject(entry.getKey());
			writeObject(entry.getValue());
		}
		return this;
	}
	
	public Serializer writeObject(Object object) {
		if(object == null){
			writeByte((byte)0);
		}else{
			if (object instanceof Integer) {
				writeInt((int) object);
				return this;
			}

			if (object instanceof Long) {
				writeLong((long) object);
				return this;
			}

			if (object instanceof Short) {
				writeShort((short) object);
				return this;
			}

			if (object instanceof Byte) {
				writeByte((byte) object);
				return this;
			}

			if (object instanceof String) {
				String value = (String) object;
				writeString(value);
				return this;
			}
			if (object instanceof Serializer) {
				writeByte((byte)1);
				Serializer value = (Serializer) object;
				value.writeToTargetBuff(writeBuffer);
				return this;
			}
			
			throw new RuntimeException("不可序列化的类型:" + object.getClass());
		}
		
		return this;
	}

	
	
	
	
	
	
	
	
}
