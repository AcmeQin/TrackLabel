package com.qinacme.tracklabeltry;

import java.util.Date;

/**
 * Created by qinacme on 2016/5/30.
 */
public class MyBluetoothHistDev extends MyBluetoothDev{
    public MyBluetoothHistDev(MyBluetoothDev mbd,Date date, double curLatitude, double curLongtitude) {
        super(mbd.name,mbd.devMajorClass,mbd.devClass,mbd.macID);
        this.date = date;
        this.curLatitude = curLatitude;
        this.curLongtitude = curLongtitude;
    }

    public Date getDate() {
        return date;
    }
    protected Date date;

    public double getCurLatitude(){
        return curLatitude;
    }
    protected double curLatitude;

    public double getCurLongtitude(){
        return curLongtitude;
    }
    protected double curLongtitude;
}
