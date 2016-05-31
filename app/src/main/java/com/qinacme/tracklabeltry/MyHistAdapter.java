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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by qinacme on 2016/5/30.
 */
public class MyHistAdapter extends RecyclerView.Adapter<MyHistAdapter.ViewHolder>  {
    protected List<MyBluetoothHistDev> bluetoothHistList;
    protected Context mContext;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View myBluetoothDevView;
        public ViewHolder(View v){
            super(v);
            myBluetoothDevView =v;
        }
    }
    public MyHistAdapter(Context context, List<MyBluetoothHistDev> bluetoothHistList){
        this.mContext=context;
        this.bluetoothHistList = bluetoothHistList;
    }
    @Override
    public MyHistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_hist_items, parent, false);
        TextView devName = (TextView)v.findViewById(R.id.bluetooth_dev_name);
        devName.setText("Default Devices");
        ViewHolder vh =new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        MyBluetoothHistDev myBluetoothDev = bluetoothHistList.get(position);
        TextView devName = (TextView)holder.myBluetoothDevView.findViewById(R.id.bluetooth_dev_name);
        devName.setText(myBluetoothDev.getName());
        TextView devDate = (TextView)holder.myBluetoothDevView.findViewById(R.id.bluetooth_dev_name);
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String theDate = df.format(myBluetoothDev.getDate());
        devDate.setText(theDate);
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
            default: d=mContext.getResources().getDrawable(R.drawable.default_icon);
                break;
        }
        devIcon.setImageDrawable(d);
    }
    @Override
    public int getItemCount(){
        return (null!=bluetoothHistList?bluetoothHistList.size():0);
    }


}
