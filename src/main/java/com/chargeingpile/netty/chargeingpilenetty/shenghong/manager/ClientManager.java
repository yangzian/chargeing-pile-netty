package com.chargeingpile.netty.chargeingpilenetty.shenghong.manager;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;


import io.netty.channel.ChannelHandlerContext;

public class ClientManager {
    //private static final Logger logger = LogManager.getLogger(ClientManager.class);

    // 保存所有的客户端连接
    public static ConcurrentHashMap<String, ClientConnection> clientMap =
                new ConcurrentHashMap<String, ClientConnection>();

    /**
     * 获取客户端连接
     * 
     * @param ip
     *            ip地址
     * @return
     */
    public static ClientConnection getClientConnection(String ip, String pileCode) {
        String id = ip + "_" + pileCode;
        // System.out.println("getClientConnection - key =  "+id);
        ClientConnection conn = clientMap.get(id);
        if (conn != null)
            return conn;
        else {
//            logger.error("ClientConenction not found in allClientMap, ip = "
//                + ip + ", pileCode = " + pileCode);

            //System.out.println("ClientConenction not found in allClientMap, ip = "+ ip + ", pileCode = " + pileCode);

        }
        return null;
    }

    /**
     * 获取客户端连接
     * 
     * @param ctx
     * @return
     */
    public static ClientConnection getClientConnection(ChannelHandlerContext ctx, String pileCode) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        return getClientConnection(ip, pileCode);
    }

    /**
     * @param ctx
     * @param pileCode
     *            桩编码
     */
    public static ClientConnection addClientConnection(ChannelHandlerContext ctx, String pileCode) {

        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        String id = ip + "_" + pileCode;
        // System.out.println("addClientConnection - key ="+id);
        ClientConnection conn;
        if (clientMap.containsKey(id)) {
            conn = clientMap.get(id);
            conn.setFirst(false);
            // System.out.println("addClientConnection containsKey " + ip);
        } else {
            conn = new ClientConnection(id, ctx);
            conn.setPile_code(pileCode);
            if (clientMap.put(id, conn) != null) {
              //  System.out.println(" addClientConnection true --> " + clientMap.size());
            }

        }
        return conn;
    }

    public static void removeClientConnection(ChannelHandlerContext ctx, String pileCode) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        String id = ip + "_" + pileCode;

        if (clientMap.remove(id) != null) {
          //  System.out.println(" removeClientConnection true --> " + clientMap.size());
        } else {
            //logger.error(id + " is not exist in allClientMap");

           // System.out.println(id + " is not exist in allClientMap");
        }
       // logger.info("Client disconnected," + id);

        //System.out.println("Client disconnected," + id);
    }

    public static void clear() {
        clientMap.clear();
    }
}
