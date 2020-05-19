package com.chargeingpile.netty.chargeingpilenetty.pojo;


import java.io.Serializable;


public class BasChaPilPojo implements Serializable {



    public String chaIp; //桩ip
    public String chaPor; //桩端口号
    public String chaNum;// 桩编号
    public String chpComEqu;// 通讯设备
    public String manNam;// 厂家名称

    public String chaPilSta;

    public String chaId; // 桩id

    public String getChaId() {
        return chaId;
    }

    public void setChaId(String chaId) {
        this.chaId = chaId;
    }

    public String getChaIp() {
        return chaIp;
    }

    public void setChaIp(String chaIp) {
        this.chaIp = chaIp;
    }

    public String getChaPor() {
        return chaPor;
    }

    public void setChaPor(String chaPor) {
        this.chaPor = chaPor;
    }

    public String getChaNum() {
        return chaNum;
    }

    public void setChaNum(String chaNum) {
        this.chaNum = chaNum;
    }

    public String getChpComEqu() {
        return chpComEqu;
    }

    public void setChpComEqu(String chpComEqu) {
        this.chpComEqu = chpComEqu;
    }

    public String getManNam() {
        return manNam;
    }

    public void setManNam(String manNam) {
        this.manNam = manNam;
    }


    public String getChaPilSta() {
        return chaPilSta;
    }

    public void setChaPilSta(String chaPilSta) {
        this.chaPilSta = chaPilSta;
    }
}
