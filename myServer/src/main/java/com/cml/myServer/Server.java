package com.cml.myServer;

import org.springframework.stereotype.Component;

import com.cml.myCommon.core.codc.RequestDecoder;
import com.cml.myCommon.core.codc.ResponseEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//server要做成一个成员，在启动时就加载，并被启动类用getBean的方式实例化并使用
//并没有用到Controller，Controller层只接受应用层请求

@Component
public class Server {
	public void start() {
		// TODO Auto-generated method stub
	    //服务类
		ServerBootstrap b=new ServerBootstrap();

		//boss和worker
	    EventLoopGroup bossGroup=new NioEventLoopGroup();
	    EventLoopGroup workerGroup=new NioEventLoopGroup();

	    try {
	    	// 设置循环线程组事例
	        b.group(bossGroup, workerGroup);
	        
	        // 设置channel工厂
 			b.channel(NioServerSocketChannel.class);

 			// 设置管道
 			b.childHandler(new ChannelInitializer<SocketChannel>() {
 				@Override
 				public void initChannel(SocketChannel ch) throws Exception {
 					ch.pipeline().addLast(new RequestDecoder());
 					ch.pipeline().addLast(new ResponseEncoder());
 					ch.pipeline().addLast(new ServerHandler());
 				}
 			});
 			//SO_BACKLOG三次握手的阻塞队列大小
 			b.option(ChannelOption.SO_BACKLOG, 2048);// 链接缓冲池队列大小
            
 			//绑定端口
 			//bind返回一个ChannelFuture,
 			//ChannelFuture方法sync,Waits for this future until it is done
 			//也返回一个CF,sync阻塞
 			b.bind(10101).sync();
 			System.out.println("start!!!");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	}

    
    	
    	
    
}
