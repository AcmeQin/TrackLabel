package com.qinacme.tracklabeltry;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by qinacme on 4/14/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    protected List<MyBluetoothDev> bluetoothDevList;
    protected Context mContext;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View myBluetoothDevView;
        public ViewHolder(View v){
            super(v);
            myBluetoothDevView =v;
        }
    }
    public MyAdapter(Context context, List<MyBluetoothDev> bluetoothDevList){
        this.mContext=context;
        this.bluetoothDevList=bluetoothDevList;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_dev_items, parent, false);
        TextView devName = (TextView)v.findViewById(R.id.bluetooth_dev_name);
        devName.setText("Default Devices");
        ViewHolder vh =new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        MyBluetoothDev myBluetoothDev = bluetoothDevList.get(position);
        TextView devName = (TextView)holder.myBluetoothDevView.findViewById(R.id.bluetooth_dev_name);
        devName.setText(myBluetoothDev.getName());
        TextView devRssi = (TextView)holder.myBluetoothDevView.findViewById(R.id.bluetooth_dev_rssi);
        devRssi.setText(myBluetoothDev.getRssi() + "dBm");
        ImageView devIcon = (ImageView)holder.myBluetoothDevView.findViewById(R.id.bluetooth_dev_icon);
        Drawable d ;
        switch (myBluetoothDev.getDevClass()){
            case BluetoothClass.Device.TOY_VEHICLE: d=mContext.getResources().getDrawable(R.drawable.default_icon);
                break;
            case BluetoothClass.Device.PHONE_SMART: d=mContext.getResources().getDrawable(R.drawable.smartphone);
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP: d=mContext.getResources().getDrawable(R.drawable.notebook);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES: d=mContext.getResources().getDrawable(R.drawable.headphones);
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA: d=mContext.getResources().getDrawable(R.drawable.tablet);
                break;
            case BluetoothClass.Device.TOY_GAME: d=mContext.getResources().getDrawable(R.drawable.gaming);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA: d=mContext.getResources().getDrawable(R.drawable.camera);
                break;
            default:
                d = mContext.getResources().getDrawable(R.drawable.default_icon);
                break;
        }
        devIcon.setImageDrawable(d);
    }
    @Override
    public int getItemCount(){

        return (null!=bluetoothDevList?bluetoothDevList.size():0);
    }
}
