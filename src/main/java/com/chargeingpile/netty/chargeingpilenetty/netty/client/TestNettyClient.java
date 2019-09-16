package com.chargeingpile.netty.chargeingpilenetty.netty.client;


/**
 * 模拟多客户端发送
 */
public class TestNettyClient {


    public static void main(String[] args) {

        //开启10条线程，每条线程就相当于一个客户端
        for (int i = 1; i <= 1; i++) {

            new Thread(new NettyClient("thread" + "--" + i)).start();
        }
    }

}
