package com.chargeingpile.netty.chargeingpilenetty.shenghong.handle;

import java.util.HashMap;
import java.util.Map;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.SHUtils;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientConnection;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.manager.ClientManager;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.message.*;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.chargepile.service.collection.Http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 盛宏 业务逻辑处理
 *
 */
public class ShServerHandler extends SimpleChannelInboundHandler<byte[]> {

   // private static final Logger log = LogManager.getLogger(ShServerHandler.class);

    String pipleCode;
    
    @Autowired
    //private InsertDao insert;

   // private ChargeDao chargeDao;
    

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final byte[] msg) throws Exception {
        if (!SHUtils.isShengHong(msg)) {
            ctx.fireChannelRead(msg);
            return;
        }
        
        String cmd = BytesUtil.getMsgCmd(msg);

        pipleCode = SHUtils.getPileNum(msg);
        

        final ClientConnection client = ClientManager.getClientConnection(ctx, pipleCode);
      

        if (cmd.equalsIgnoreCase(ShengHong.PARA_MAST)) {
            if (msg[50] == 0) {

            } else {
               // System.out.println("参数查询/设置--失败");
            }
        } else if (cmd.equalsIgnoreCase(ShengHong.STATE_MAST)) {

            handleChargeState(ctx, msg, client);

        } else if (cmd.equalsIgnoreCase(ShengHong.CHARGEINFO_REQ)) {

            ctx.executor().execute(new Runnable() {
                public void run() {
                    try {

                        ChargeRecordInfo info = ChargeRecordInfo.getInfo(msg);
                        pipleCode = info.getPileCode();

                        //log.info( "pipleCode =  "+ pipleCode +" 上报充电记录");
                        //log.info( info.toString());
                      //  System.out.println("pipleCode = "+ pipleCode +" 上报充电记录");
                       // System.out.println(info.toString());

                        //chargeDao.insertChargeRecord(info, pipleCode);

                        //响应
                        String gun = BytesUtil.int2HexString(info.getGun());
                        String card = BytesUtil.bytesToHexString(info.getCarId());
                        CharInfoResponse res = new CharInfoResponse(gun, card);
                        ctx.writeAndFlush(res.getMsgByte(2));

                        //TODO 是否需要
//                        Insert.insertCharInfo(msg);

                        String id = "";
                        if (client != null) {
                            id = client.getPile_code();
                            // 停止充电时用到，充电信息
                            client.setChargeRecordInfo(info);
                        }
                        Http.staCha(id, info.getChargeEle());
                        

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        ReferenceCountUtil.safeRelease(msg);
                    }

                }
            });

        } else if (cmd.equalsIgnoreCase(ShengHong.ALREM_REQ)) {
            
            //预留，先不处理
//            AlarmInfo info = AlarmInfo.getIns(msg);
//            pipleCode = info.getPile_code();
//            log.info("充电桩 上报  告警信息  : pipleCode=" + pipleCode);
//
//            // TODO
//            ctx.executor().execute(new Runnable() {
//                public void run() {
//
//                    try {
//                        if (!CommonUtil.isEmpty(pipleCode)) {
//
//                            Insert.insertAlarm(msg);
//
//                            AlarmInfo_dao.updateChpStaAlarm(pipleCode);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

        } else {
            ctx.fireChannelRead(msg);
        }
        ReferenceCountUtil.safeRelease(msg);

    }

    /**
     * 充电桩状态信息 处理
     * 
     * @param ctx
     * @param msg
     * @param client
     */
    private void handleChargeState(final ChannelHandlerContext ctx, final byte[] msg,
                final ClientConnection client) {
       // log.info("盛宏充电桩 状态信息上报 --> ");
        //System.out.println("盛宏充电桩 状态信息上报 --> ");
        ctx.executor().execute(new Runnable() {
            public void run() {
                try {
                    PileStateInfo info = PileStateInfo.getStateInfo(msg);
                 //   log.info("handleChargeState --> " + info.toString());
                  //  System.out.println("handleChargeState --> " + info.toString());

                    //chargeDao.insertChargeRecord(info, pipleCode);

                    int pileState = info.getWorkState();

                    String gun = BytesUtil.byteToHexString(info.getGun());
                    String cardStr = BytesUtil.bytesToHexString(info.getCardID());
                    StateResponse sr = new StateResponse(gun, cardStr);
                    ctx.writeAndFlush(sr.getMsgByte(1));
                    
                    //桩原本是空闲状态，这次是充电状态，就认为桩开启了充电
                    if (client != null ) {
                        int lastState = client.getPileState();
                        
                        if (lastState != ClientConnection.STATE_CHARGE && 
                                    pileState== ClientConnection.STATE_CHARGE) {
                            
//                            String id = client.getUserID();
//                            com.chargepile.service.collection.suowei.sql.Insert.insertChargeStart(
//                                id, pipleCode,
//                                    new Date(System.currentTimeMillis()));
                        }
                        
                        client.setPileState(pileState);
                    }
                    
                    
                    

                    // 实时充电数据放入缓存
                    StateInfo stateInfo = StateInfo.getIns(msg);
                    pipleCode = stateInfo.getZhuangId();
                    Map<String, Object> retMap = new HashMap<String, Object>();
                    retMap.put("elec", stateInfo.getElecQua());
                    retMap.put("time", stateInfo.getCharTim());
                    retMap.put("DC_v", stateInfo.getDireV());
                    retMap.put("DC_i", stateInfo.getDireI());
                    retMap.put("av", stateInfo.getaV());
                    retMap.put("ai", stateInfo.getaI());
                    retMap.put("bv", stateInfo.getbV());
                    retMap.put("bi", stateInfo.getbI());
                    retMap.put("cv", stateInfo.getcV());
                    retMap.put("ci", stateInfo.getcI());
                    //剩余充电时间  秒
                    retMap.put("rem_tim", stateInfo.getRemTim());
                    retMap.put("soc", stateInfo.getSoc());

                    //CacheManager cacheManager = CacheManager.getInstance();
                   // Cache sample = cacheManager.getCache("loginCache");
                   // sample.remove(pipleCode + "chaReaTim");
                  //  Element element = new Element(pipleCode + "chaReaTim", retMap);
                  //  sample.put(element);

                    // 告警状态
                    Map<String, Object> alarmMap = new HashMap<String, Object>();
                    alarmMap.put("alarm", stateInfo.getAlarm());
                    Element alarm = new Element(pipleCode + "alarm", alarmMap);
                  //  sample.put(alarm);

                    updatePileState(pipleCode, pileState);
                    
                    // 插入数据库
                   
                    //insert.insertStateInfo(msg);
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    
    /**
     * @param code
     * @param pileState
     * @return
     */
    public int updatePileState(String code, int pileState) {
        if (pileState < 0) {
            return -1;
        }
       // int pileDBState = ProjectUtils.convertStatus2DBStatus(pileState);


        //return chargeDao.updatePileState(code, pileDBState);
        return 1;
    }
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //chargeDao = SpringContextUtil.getBean("chargeDao");
        //insert = SpringContextUtil.getBean("insertDao");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //log.error(cause);
        System.out.println(cause);
    }
}
