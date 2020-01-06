package com.chargeingpile.netty.chargeingpilenetty.shenghong;


import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyChargeHandler;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.handle.HandleName;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StartCharger;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.StopCharger;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
public class SHCmd {
	
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
		if (ctx == null ) {
			return false;
		}
		int flag = 0;
		if (data.getCharType().equals(StartCharger.ORDER)) {
			flag = 0;
		}else{
			flag = 1;
		}

		NettyChargeHandler handler = (NettyChargeHandler) ctx.channel().pipeline().get(HandleName.HANDLE_CHARGE) ;
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
		if (ctx == null ) {
			return false;
		}
		
		ChannelFuture future = ctx.writeAndFlush(data.getMsgByte(2));
		int flag = 0;
		if (data.getAddr().equals(StopCharger.ADDR_ORDER)) {
			flag = 0;
		}else{
			flag = 1;
		}

		NettyChargeHandler handler = (NettyChargeHandler) ctx.channel().pipeline().get(HandleName.HANDLE_CHARGE) ;
		handler.setFlag(flag);
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {

					System.out.println("停止充电-----中");
				}
				
			}
		});
		
		return true;
	}
}
