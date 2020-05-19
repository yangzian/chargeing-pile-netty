package com.chargeingpile.netty.chargeingpilenetty.controller;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.net.InetSocketAddress;


@Component
@Order(value = 1)
public class StartServer implements ApplicationRunner {






    @Value("${pile.soket_ip}")
    private String soketIp;

    @Value("${pile.soket_port}")
    private Integer soketPort;



    @Autowired
    private NettyServer nettyServer;





    public void setServletContext(ServletContext servletContext) {



       /* // 根据服务ip 和 端口号 开启服务

*/
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        //System.out.println("setServletContext is start");

        InetSocketAddress address = new InetSocketAddress(soketIp, soketPort);
//
        //System.out.println("address-------------"+address);

        int i =  nettyServer.start(address);

        if (i != 0){
           // System.out.println("============================pile start server faile====================");
        }
        //System.out.println("============================pile start server success====================");

    }
}
