package com.chargeingpile.netty.chargeingpilenetty;

import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import java.net.InetSocketAddress;

//@EnableAutoConfiguration
@EnableCaching
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class,MongoAutoConfiguration.class})
@MapperScan("com.ennit.dashboard.gating.mapper")
public class ChargeingPileNettyApplication //extends SpringBootServletInitializer
 {



    public static void main(String[] args) {
        SpringApplication.run(ChargeingPileNettyApplication.class, args);
    }


     //@Override
     protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
         return builder.sources(ChargeingPileNettyApplication.class);
     }

/*

     protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
         return builder.sources(ChargeingPileNettyApplication.class);
     }
*/


    //public void run(String...strings) throws Exception{

       // InetSocketAddress address = new InetSocketAddress(DefaultConstans.SOKET_IP,DefaultConstans.SOKET_PORT);
        //nettyServer.start(address);


    //}



}
