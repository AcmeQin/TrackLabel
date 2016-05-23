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

    private String macID;

    public String getName() {
        return name;
    }

    private String name;

    public int getDevClass() {
        return devClass;
    }

    private int devClass;

    public int getDevMajorClass() {
        return devMajorClass;
    }

    private int devMajorClass;

    @Override
    public boolean equals(Object o) {
        MyBluetoothDev temp = (MyBluetoothDev) o;
        return this.getMacID().equals(temp.getMacID());
    }
}
