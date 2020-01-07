package com.chargeingpile.netty.chargeingpilenetty.shenghong.decoder;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 解码器<br>
 * 解决粘包问题
 * 
 * @author cj
 *
 */
public class MyDecoder extends MessageToMessageDecoder<byte[]> {

    private static final Logger log = LoggerFactory.getLogger(MyDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        try {

            log.info("decode --> " + BytesUtil.bytesToHexString2(msg));

            // 区分桩类型
            /*if (SWUtils.isShuoWei(msg)) {
                // 硕维桩
                unpacking(msg, out);

            } */
            if (SHUtils.isShengHong(msg)) {
                // 盛宏桩
                unpackSH(msg, out);
            }else {
                log.info( " unkown proctrol 未知协议类型" );
//                out.add(msg);
            }


            /*if (ZyzhDataUtils.isZyzhData(msg)) {
                unpackZyzh(msg, out);
            } else {
                log.info( " unkown proctrol 未知协议类型" );
//                out.add(msg);
            }*/
        } catch (Exception e) {
            log.error(" decode Exception : " + e.toString());
        }
    }

    /**
     * @param msg
     * @param out
     */
    /*private void unpackZyzh(byte[] msg, List<Object> out) {
        // 先处理数据，才能获取实际长度
        msg = ZyzhDataUtils.decodeData(msg);
        // log.info("unpackZyzh --> " + BytesUtil.bytesToHexString2(msg));
        if (msg != null && msg.length > 0) {

            // 一个整包的长度
            int len = BytesUtil.toInt2(msg, 1);
            if (msg.length <= len) { // 一个包
                out.add(msg);
                
            } else {
                // 1.取出一个包的数据
                byte[] array = new byte[len];
                System.arraycopy(msg, 0, array, 0, len);
                out.add(array);

                // 2. 多余的数据继续分包
                int other = msg.length - len;
                array = new byte[other];
                System.arraycopy(msg, len, array, 0, array.length);
                unpackZyzh(array, out);
            }
        }
    }*/

    /**
     * 盛宏拆包
     */
    private void unpackSH(byte[] msg, List<Object> out) {
        // 一个整包的长度
        int len = BytesUtil.toInt2(msg, 2);
        if (msg.length <= len) { // 一个整包
            out.add(msg);

        } else {
            // 1.取出一个包的数据
            byte[] array = new byte[len];
            System.arraycopy(msg, 0, array, 0, len);
            out.add(array);

            // 2. 多余的数据继续分包
            int other = msg.length - len;
            array = new byte[other];
            System.arraycopy(msg, len, array, 0, array.length);
            unpackSH(array, out);

        }

    }

    /**
     * 硕维拆包
     */
    private void unpacking(byte[] msg, List<Object> out) {
        if (msg[0] == 0x18) {
            // 一个整包的长度
            int len = BytesUtil.toInt2(msg, 1) + 3;
            if (msg.length <= len) { // 一个整包
                out.add(msg);
            } else {
                // 1.
                byte[] array = new byte[len];
                System.arraycopy(msg, 0, array, 0, len);
                out.add(array);

                // 2. 多余的数据继续分包
                int other = msg.length - len;
                array = new byte[other];
                System.arraycopy(msg, len, array, 0, array.length);
                unpacking(array, out);

            }

        } else {
            out.add(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.toString();

        log.info("channelActive -->  RamoteAddress : " + ip + " connected ");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(" exceptionCaught : " + cause.toString() + " ctx = " 
                    + ctx.channel().toString() );
        ctx.close();
    }

}
