package com.chargeingpile.netty.chargeingpilenetty.mapper;

import com.chargeingpile.netty.chargeingpilenetty.pojo.BasChaPilPojo;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.ChargeRecordInfo;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.PileStateInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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


@Mapper
@Repository
public interface ChargingMapper {


    List<Map<String,Object>> getUserInfo();


    // 根据 桩id 查询桩ip及端口号等信息
    List<BasChaPilPojo> selChaIp(@Param("pileId")String pileId,@Param("chaNum") String chaNum);


    /**
     * 充电桩实时 数据 新增
     * @param pileStateInfo
     * @return
     */
    int insertDatChaPilSta(PileStateInfo pileStateInfo);


    /**
     * 充电桩数据新增  开始 和 结束
     * @param map
     * @return
     */
    int insertDatChaRes(Map<String,String> map);




    /**
     * 充电站实时数据 （开始，结束。）
     * @param chargeRecordInfo
     * @return
     */
    int insertDatChaInf(ChargeRecordInfo chargeRecordInfo);


    /**
     * 添加告警 信息
     * @param map
     * @return
     */
    int insertAla(Map<String,Object> map);

    /**
     * 添加故障 信息
     * @param map
     * @return
     */
    int insertFau(Map<String,Object> map);

    /**
     * 修改充电桩 状态
     * @param id  id
     * @param chp_nam 充电桩名称
     * @param cha_num 充电桩编号
     * @param cha_ip 充电桩的IP
     * @return
     */
       int updChaPilSta(@Param("id")String id,
                        @Param("chp_nam")String chp_nam,
                        @Param("cha_num")String cha_num,
                        @Param("cha_ip")String cha_ip);

}
