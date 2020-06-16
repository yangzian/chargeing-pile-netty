package com.chargeingpile.netty.chargeingpilenetty.shenghong;


import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyChargeHandler;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServer;
import com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl.ChargingServiceImpl;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.handle.HandleName;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StartCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StopCharger;
import com.chargeingpile.netty.chargeingpilenetty.util.ApplicationContextUtils;
import com.chargeingpile.netty.chargeingpilenetty.util.EhcacheUtil;
import io.netty.channel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetSocketAddress;

public class SHCmd {



	private ApplicationContext applicationContext= ApplicationContextUtils.getApplicationContext();

	ChargingServiceImpl chargingService = applicationContext.getBean(ChargingServiceImpl.class);



	
	/**
	 * 发送消息序列号
	 */
	private static int index = 1;
   // private final static Logger log = LogManager.getLogger(SHCmd.class);
	
	
	/**
	 * 开启充电/预约
	 * @param ctx
	 * @param data
	 * @return
	 */
	public static boolean startCharge(final ChannelHandlerContext ctx, StartCharger data) {

		EhcacheUtil ehcache = EhcacheUtil.getInstance();


		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		ChannelId channelId = ctx.channel().id(); // 获取连接通道唯一标识通道号


		if (ctx == null ) {
			return false;
		}
		int flag = 0;
		if (data.getCharType().equals(StartCharger.ORDER)) {
			flag = 0;
		}else{
			flag = 1;
		}

		//NettyChargeHandler handler = (NettyChargeHandler) ctx.channel().pipeline().get(HandleName.HANDLE_CHARGE) ;
		System.out.println("sta====ctx========="+ctx);
		System.out.println("sta====ctx.channel()========="+ctx.channel());
		System.out.println("sta====ctx.channel().pipeline()========="+ctx.channel().pipeline());
		System.out.println("sta====ctx.channel().pipeline().get()=========="+ctx.channel().pipeline().get("charge"));

		NettyChargeHandler handler = null;

		//handler = (NettyChargeHandler) ctx.channel().pipeline().get("charge") ;

		handler = (NettyChargeHandler)ehcache.get(clientIp+channelId);

		System.out.println("handler======="+handler);


		//handler = null;

		if (handler == null){

			ctx.channel().pipeline().remove("charge");

			ctx.channel().pipeline().addLast("charge",new NettyChargeHandler());

			handler = (NettyChargeHandler) ctx.channel().pipeline().get("charge") ;

			ehcache.put(clientIp+channelId,ctx.channel().pipeline().get("charge"));

		}


		System.out.println("sta====handler========="+handler);

		handler.setFlag(flag);

		ChannelFuture future = ctx.writeAndFlush(data.getMsgByte(index));
		index += 2;
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
				    //log.info("盛宏桩 发送开启充电命令成功 --->");
					System.out.println("盛宏桩 发送开启充电命令成功 --->");
				}

			}
		});
		
		return true;
	}


	/**
	 * 停止充电/取消预约
	 * @param ctx
	 * @param data
	 * @return
	 */
	public static boolean stopCharge(final ChannelHandlerContext ctx, StopCharger data) {



		EhcacheUtil ehcache = EhcacheUtil.getInstance();
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		ChannelId channelId = ctx.channel().id(); // 获取连接通道唯一标识通道号

		if (ctx == null ) {
			return false;
		}

		int flag = 0;
		if (data.getAddr().equals(StopCharger.ADDR_ORDER)) {
			flag = 0;
		}else{
			flag = 1;
		}

		//NettyChargeHandler handler = (NettyChargeHandler)ctx.channel().pipeline().get(HandleName.HANDLE_CHARGE) ;
		System.out.println("stop===ctx========="+ctx);
		System.out.println("stop===ctx.channel()========="+ctx.channel());
		System.out.println("stop===ctx.channel().pipeline()========="+ctx.channel().pipeline());
		System.out.println("stop===ctx.channel().pipeline().get()========="+ctx.channel().pipeline().get("charge"));
		NettyChargeHandler handler =null;

		//handler = (NettyChargeHandler) ctx.channel().pipeline().get("charge");

		handler = (NettyChargeHandler)ehcache.get(clientIp+channelId);

		System.out.println("stop===handler===="+handler);


/*

		if (handler != null){
			ehcache.put(clientIp+channelId,ctx.channel().pipeline().get("charge"));
		}else{
			handler = (NettyChargeHandler)ehcache.get(clientIp+channelId);
		}
*/

		ChannelFuture future =
				ctx.writeAndFlush(data.getMsgByte(2));

		handler.setFlag(flag);
		/*future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {

					System.out.println("停止充电-----中");
				}
				
			}
		});*/
		
		return true;
	}
}
