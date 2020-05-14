package com.chargeingpile.netty.chargeingpilenetty.service.serviceImpl;

import com.chargeingpile.netty.chargeingpilenetty.config.ServerResponse;
import com.chargeingpile.netty.chargeingpilenetty.constans.DefaultConstans;
import com.chargeingpile.netty.chargeingpilenetty.mapper.ChargingMapper;
import com.chargeingpile.netty.chargeingpilenetty.netty.server.NettyServer;
import com.chargeingpile.netty.chargeingpilenetty.pojo.BasChaPilPojo;
import com.chargeingpile.netty.chargeingpilenetty.service.ChargingService;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHServer;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * ***************************************************
 *
 * @Auther: zianY
 * @Descipion: TODO
 * @CreateDate: 2019-11-15
 * ****************************************************
 */

@Service
public class ChargingServiceImpl implements ChargingService {





    @Autowired
    private NettyServer nettyServer;


    @Autowired
    private ChargingMapper chargingMapper;


    /**
     * 测试
     * @return
     */
    public ServerResponse getUserInfo(){

        List<Map<String,Object>> userList = chargingMapper.getUserInfo();

        return ServerResponse.createBySuccess("查询成功",userList);
    }











    public ServerResponse startService(){



            // 根据服务ip 和 端口号 开启服务
            //
          InetSocketAddress address = new InetSocketAddress(DefaultConstans.SOKET_IP, DefaultConstans.SOKET_PORT);

        System.out.println("address1-------------"+address);

            int i =  nettyServer.start(address);

           if (i != 0){
               return ServerResponse.createByErrorMessage("shibai。");
           }




 /*
        SHServer shServer = new SHServer(9999);
           shServer.start();;
           */

        return ServerResponse.createBySuccess("启动成功",0);


    }


















    // 根据充电桩ip 和 充电桩编号 停止服务  0成功 1失败
    public Integer stopService(String chaIp,String chaNum){

        try {

            ClientConnection conn = ClientManager.getClientConnection(chaIp,chaNum);

            if (conn != null){

                InetSocketAddress insocket = (InetSocketAddress) conn.getCtx().channel().remoteAddress();

                nettyServer.stop();
                return 0;

            }

        } catch (Exception e) {

            e.printStackTrace();
            //return 0;
        }

        return 1;

    }





    public List<BasChaPilPojo> selChaIp(String chaId,String chaNum){

        return chargingMapper.selChaIp(chaId,chaNum);


    }


}
