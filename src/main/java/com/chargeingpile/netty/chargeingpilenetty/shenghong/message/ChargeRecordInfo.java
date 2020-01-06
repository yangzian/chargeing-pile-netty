package com.chargeingpile.netty.chargeingpilenetty.shenghong.message;

import java.util.Arrays;

import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.ASCIIUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.BytesUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.CommonUtil;
import com.chargeingpile.netty.chargeingpilenetty.shenghong.utils.TimeUtils;

/**
 * 充电记录信息
 * 
 * @author cj
 *
 */
public class ChargeRecordInfo {

    /**
     * 桩 ID， ASSIC 编码
     */
    private String pileCode;

    /**
     * 充电枪位置类型 1‐直流 2‐交流
     */
    private int gunType;

    /**
     * 充电枪口
     */
    private int gun;

    /**
     * 充电卡号
     */
    private String cardID;

    private byte[] carId = new byte[32];

    /**
     * 充电 开始时间
     */
    private String startTime;

    /**
     * 充电 结束时间
     */
    private String endTime;

    /**
     * 充电时间长度 -秒
     */
    private int durationTime;

    /**
     * 开始SOC
     */
    private int startSOC;

    private int endSOC;

    /**
     * 充电结束原因
     */
    private byte[] chargeEndReason = new byte[4];

    private int stopReason;

    /**
     * 本次充电电量 (1AH/bit)
     */
    private float chargeEle;

    /**
     * 充电前电表读数
     */
    private int chargeStart;

    /**
     * 充电后电表读数
     */
    private int chargeEnd;

    /**
     * 本次充电金额
     */
    private float chargeMoney;

    /**
     * 充电前卡余额
     */
    private float cardBalance;

    /**
     * 当前充电记录索引
     */
    private int hisIndex;

    /**
     * 总充电记录条目
     */
    private int hisCount;

    /**
     * 充电策略 0 自动充满 1 按时间充满 2 定金额 3 按电量充满
     */
    private int chargeType;

    /**
     * 充电策略参数 时间单位为 1 秒 金额单位为 0.01 元 电量时单位为 0.01kw
     * 
     */
    private float chargeTypeParam;

    /**
     * 车辆 VIN, 直流桩上传，没有填’\0’
     */
    private String carVIN;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 48个时段电量，没个时段电量2个字节
     */
    private byte[] electric = new byte[96];

    private float[] eleTime;

    /**
     * 充电启动方式 0：本地刷卡启动 1：后台启动 2：本地管理员启动
     */
    private int chargeStartType;

    /**
     * 将桩的信息转化为实体
     * 
     * @param msg
     * @return
     */
    public static ChargeRecordInfo getInfo(byte[] msg) {
        if (msg == null || msg.length == 0) {
            return null;
        }
        ChargeRecordInfo info = new ChargeRecordInfo();

        String pileCode = ASCIIUtil.ASCII2Int(msg, 12, 32);
        
        info.setPileCode(pileCode);

        info.setGunType(msg[44]);
        info.setGun(msg[45]);

        byte[] data = info.getCarId();
        System.arraycopy(msg, 46, data, 0, data.length);
        //TODO
        String cardID = ASCIIUtil.ASCII2Int(msg, 46, 78);
        if (CommonUtil.isEmpty(cardID)) {
            cardID = "0";
        }
        info.setCardID(cardID);

        // time
        info.setStartTime(TimeUtils.getSHBCDTime(msg, 78));
        info.setEndTime(TimeUtils.getSHBCDTime(msg, 86));

        int time = BytesUtil.toInt4(msg, 94);
        info.setDurationTime(time);

        // soc
        info.setStartSOC(msg[98]);
        info.setEndSOC(msg[99]);
        data = info.getChargeEndReason();
        System.arraycopy(msg, 100, data, 0, data.length);
        info.setStopReason(BytesUtil.toInt4(data));

        float elecQua = BytesUtil.toInt4(msg, 104) * 0.01f;
        info.setChargeEle(elecQua);

        int read_before = BytesUtil.toInt4(msg, 108);
        info.setChargeStart(read_before);
        int read_after = BytesUtil.toInt4(msg, 112);
        info.setChargeEnd(read_after);
        // 金额
        float money = BytesUtil.toInt4(msg, 116) * 0.01f;
        info.setChargeMoney(money);
        float blanceMoney = BytesUtil.toInt4(msg, 124) * 0.01f;
        info.setCardBalance(blanceMoney);

        info.setHisIndex(BytesUtil.toInt4(msg, 128));
        info.setHisCount(BytesUtil.toInt4(msg, 132));

        int type = msg[137];
        info.setChargeType(type);
        float chargeTypeParam = BytesUtil.toInt4(msg, 138);
        if (type == 2 || type == 3) {
            chargeTypeParam = chargeTypeParam * 0.01f;
        }
        info.setChargeTypeParam(chargeTypeParam);
        // TODO vin
        String carVin = ASCIIUtil.ASCII2Int(msg, 142, 17);
        info.setCarVIN(carVin);
        String carNum = ASCIIUtil.ASCII2Int(msg, 159, 8);
        info.setCarVIN(carNum);

        if (msg.length >= 167) {
            float[] elecTim = new float[48];
            for (int i = 167; i < 48; i++) {
                elecTim[i] = BytesUtil.toInt2(msg, i*2);
            }
            info.setEleTime(elecTim);
        }

        info.setChargeStartType(msg[msg.length - 2]);

        return info;
    }

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public int getGunType() {
        return gunType;
    }

    public void setGunType(int gunType) {
        this.gunType = gunType;
    }

    public int getGun() {
        return gun;
    }

    public void setGun(int gun) {
        this.gun = gun;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public byte[] getCarId() {
        return carId;
    }

    public void setCarId(byte[] carId) {
        this.carId = carId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public int getStartSOC() {
        return startSOC;
    }

    public void setStartSOC(int startSOC) {
        this.startSOC = startSOC;
    }

    public int getEndSOC() {
        return endSOC;
    }

    public void setEndSOC(int endSOC) {
        this.endSOC = endSOC;
    }

    public byte[] getChargeEndReason() {
        return chargeEndReason;
    }

    public void setChargeEndReason(byte[] chargeEndReason) {
        this.chargeEndReason = chargeEndReason;
    }

    public float getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(float chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public int getHisIndex() {
        return hisIndex;
    }

    public void setHisIndex(int hisIndex) {
        this.hisIndex = hisIndex;
    }

    public int getHisCount() {
        return hisCount;
    }

    public void setHisCount(int hisCount) {
        this.hisCount = hisCount;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public float getChargeTypeParam() {
        return chargeTypeParam;
    }

    public void setChargeTypeParam(float chargeTypeParam) {
        this.chargeTypeParam = chargeTypeParam;
    }

    public byte[] getElectric() {
        return electric;
    }

    public void setElectric(byte[] electric) {
        this.electric = electric;
    }

    public int getChargeStartType() {
        return chargeStartType;
    }

    public void setChargeStartType(int chargeStartType) {
        this.chargeStartType = chargeStartType;
    }

    public float getChargeEle() {
        return chargeEle;
    }

    public void setChargeEle(float chargeEle) {
        this.chargeEle = chargeEle;
    }

    public int getChargeStart() {
        return chargeStart;
    }

    public void setChargeStart(int chargeStart) {
        this.chargeStart = chargeStart;
    }

    public int getChargeEnd() {
        return chargeEnd;
    }

    public void setChargeEnd(int chargeEnd) {
        this.chargeEnd = chargeEnd;
    }

    public int getStopReason() {
        return stopReason;
    }

    public void setStopReason(int stopReason) {
        this.stopReason = stopReason;
    }

    public float getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(float cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCarVIN() {
        return carVIN;
    }

    public void setCarVIN(String carVIN) {
        this.carVIN = carVIN;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public float[] getEleTime() {
        return eleTime;
    }

    public void setEleTime(float[] eleTime) {
        this.eleTime = eleTime;
    }

    @Override
    public String toString() {
        return "ChargeRecordInfo [pileCode=" + pileCode + ", gunType=" + gunType + ", gun=" + gun
                    + ", cardID=" + cardID + ", carId=" + Arrays.toString(carId) + ", startTime=" + startTime
                    + ", endTime=" + endTime + ", durationTime=" + durationTime + ", startSOC=" + startSOC
                    + ", endSOC=" + endSOC + ", chargeEndReason=" + Arrays.toString(chargeEndReason)
                    + ", stopReason=" + stopReason + ", chargeEle=" + chargeEle + ", chargeStart="
                    + chargeStart + ", chargeEnd=" + chargeEnd + ", chargeMoney=" + chargeMoney
                    + ", cardBalance=" + cardBalance + ", hisIndex=" + hisIndex + ", hisCount=" + hisCount
                    + ", chargeType=" + chargeType + ", chargeTypeParam=" + chargeTypeParam + ", carVIN="
                    + carVIN + ", plateNumber=" + plateNumber + ", electric=" + Arrays.toString(electric)
                    + ", eleTime=" + Arrays.toString(eleTime) + ", chargeStartType=" + chargeStartType + "]";
    }

}
