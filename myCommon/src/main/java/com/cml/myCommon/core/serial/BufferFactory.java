package com.cml.myCommon.core.serial;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;


public class BufferFactory {
   //nio的ByteOrder可以决定大小端
	
	public static ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;
	
	//内存分配器ByteBufAllocator
	private static ByteBufAllocator bufAllocator=PooledByteBufAllocator.DEFAULT;
	
	/**
	 * 获取一个buffer
	 * 
	 * @return
	 */
	public static ByteBuf getBuffer() {
	    ByteBuf buffer=bufAllocator.heapBuffer();
	    buffer=buffer.order(BYTE_ORDER);
	    return buffer;
	}
	
	public static ByteBuf getBuffer(byte[] bytes) {
		ByteBuf buffer = bufAllocator.heapBuffer();
		buffer = buffer.order(BYTE_ORDER);
		buffer.writeBytes(bytes);
		return buffer;
	}

}
