package com.qinacme.tracklabeltry;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qinacme on 4/22/2016.
 */
public class MyLocationSource implements LocationSource, AMapLocationListener {
    private OnLocationChangedListener myListener;
    private AMapLocationClient myLocationClient;
    private AMapLocationClientOption myLocationOption;
    private Context myContext;
    private int curLocationType;
    private float curAccuracy;
    private double curLatitude;
    private double curLongtitude;
    private Date curLocationDate;

    public MyLocationSource(Context myContext) {

        this.myContext = myContext;

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        myListener = onLocationChangedListener;
        if (myLocationClient==null){
            myLocationClient = new AMapLocationClient(myContext);
            myLocationOption = new AMapLocationClientOption();
            myLocationClient.setLocationListener(this);
            myLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        We have three location options:
//        AMapLocationMode.Hight_Accuracy;
//        AMapLocationMode.Battery_Saving;
//        AMapLocationMode.Device_Sensors;
            myLocationOption.setNeedAddress(true);
            myLocationOption.setOnceLocation(false);
            myLocationOption.setWifiActiveScan(true);
            myLocationOption.setMockEnable(false);
            myLocationOption.setInterval(2000);
            myLocationClient.setLocationOption(myLocationOption);
            myLocationClient.startLocation();

        }
    }

    public Date getCurLocationDate(){
        return curLocationDate;
    }

    @Override
    public void deactivate() {
        myListener = null;
        if (myLocationClient != null){
            myLocationClient.stopLocation();
            myLocationClient.onDestroy();
        }
        myLocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (myListener!= null&& aMapLocation!=null){
            if (aMapLocation!=null && aMapLocation.getErrorCode()==0){
                myListener.onLocationChanged(aMapLocation);
                curLocationType=aMapLocation.getLocationType();
                curLatitude=aMapLocation.getLatitude();
                curLongtitude=aMapLocation.getLongitude();
                curAccuracy=aMapLocation.getAccuracy();
                curLocationDate=new Date(aMapLocation.getTime());
//                        aMapLocation.getAddress();
//                        aMapLocation.getCountry();
//                        aMapLocation.getProvince();
//                        aMapLocation.getCity();
//                        aMapLocation.getDistrict();
//                        aMapLocation.getStreet();
            }else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
            }
        }
    }
}
