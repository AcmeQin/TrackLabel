package com.qinacme.tracklabeltry;

/**
 * Created by qinacme on 4/17/2016.
 */
public class MyBluetoothDev {
    public MyBluetoothDev(String name, int devMajorClass , int devClass, String macID) {
        this.name = name;
        this.devClass = devClass;
        this.macID = macID;
        this.devMajorClass = devMajorClass;
    }

    public String getMacID() {
        return macID;
    }

    protected String macID;

    public String getName() {
        return name;
    }

    protected String name;

    public int getDevClass() {
        return devClass;
    }

    protected int devClass;

    public int getDevMajorClass() {
        return devMajorClass;
    }

    protected int devMajorClass;

    @Override
    public boolean equals(Object o) {
        MyBluetoothDev temp = (MyBluetoothDev) o;
        return this.getMacID().equals(temp.getMacID());
    }
}
