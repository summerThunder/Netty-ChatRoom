package com.cml.myCommon.core.codc;

import java.util.List;

import com.cml.myCommon.core.codc.ConstantValue;
import com.cml.myCommon.core.model.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 数据包解码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号      |  命令号    |   长度     |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节 
 * 命令号2字节
 * 长度4字节(数据部分占有字节数量)
 * 
 *
 *
 */
public class RequestDecoder extends ByteToMessageDecoder{
	/**
	 * 数据包基本长度
	 */
	public static int BASE_LENTH=4+2+2+4;

	
	//注意netty4是ByteBuf
	//decode里out是一个list,每个元素是一个自定义的Request
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
		while(true) {
			//外层循环循环处理多个消息体
			if(buffer.readableBytes()>=BASE_LENTH) {
				//第一个可读数据包的起始位置
				int beginIndex;
				//内层循环循环检验一个消息体是否含包头
				while(true) {
					//记下初始读标记，方便后面还原（本质是Channel的pos)
					//Returns the readerIndex of this buffer.
					beginIndex=buffer.readerIndex();
					//标记初始读游标位置
					buffer.markReaderIndex();
					//如果包头正确，跳出
					//不会出现只有包头的情况，因为有个baselenth判断
					if(buffer.readInt()==ConstantValue.HEADER_FLAG) {
						break;
					}
					//未读到包头标识先回到之前的标记点，再略过一个字节
					buffer.resetReaderIndex();
					buffer.readByte();
					
					//不满足基础长度
					if(buffer.readableBytes() < BASE_LENTH){
						return ;
					}
					
				}
				//内部死循环跳出，说明包头正确
				short module=buffer.readShort();
				short cmd=buffer.readShort();
				
				//读取数据长度
				int lenth=buffer.readInt();
				//数据长度小于0，非法数据，关闭channel
				if(lenth<0) {
					ctx.channel().close();
				}
				
				//数据包还没到齐，返回还没有读包头的坐标
				if(buffer.readableBytes()<lenth) {
					buffer.readerIndex(beginIndex);
					return;
				}
				
				//读数据部分
				byte[] data=new byte[lenth];
				buffer.readBytes(data);
				
			  
				Request message=new Request();
				message.setModule(module);
				message.setCmd(cmd);
				message.setData(data);
				
				//解析出消息对象，继续往下面的handler传递
				//out是list类型
				out.add(message);
				
			}else {//如果比baselenth短跳出外层循环
				break;
			}
		}
		//跳出外层循环，数据不完整，等待完整的数据包
		return ;
	}
}
