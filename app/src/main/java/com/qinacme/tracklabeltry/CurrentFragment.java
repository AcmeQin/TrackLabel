package com.qinacme.tracklabeltry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinacme on 2016/5/22.
 */
public class CurrentFragment extends Fragment {
    private Context mApp;

    private AMap aMap;
    private MapView mapView;
    private MyLocationSource mLocationSource;

    private RecyclerView mRecyclerView;
    private RecyclerView bluetoothDevRecycleView;
    private RecyclerView.Adapter bluetoothDevAdapter;
    private RecyclerView.LayoutManager bluetoothDevLayoutManager;

    private BluetoothAdapter mBluetoothAdapter;
    private List<MyBluetoothDev> bluetoothDevList = new ArrayList<MyBluetoothDev>();
    private Button bluetoothButton;
    private BroadcastReceiver myReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MyBluetoothDev newDevice = new MyBluetoothDev(device.getName(),
                        device.getBluetoothClass().getMajorDeviceClass() ,
                        device.getBluetoothClass().getDeviceClass(), device.getAddress());
                if (!bluetoothDevList.contains(newDevice))
                    bluetoothDevList.add(newDevice);
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mBluetoothAdapter.startDiscovery();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current,container,false);
        mApp = getActivity();
        mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mLocationSource = new MyLocationSource(mApp);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothButton = (Button) rootView.findViewById(R.id.bluetooth_button);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 0);
                } else {
                    mBluetoothAdapter.startDiscovery();
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    mApp.registerReceiver(myReciever, filter);
                }
            }
        });

        bluetoothDevRecycleView = (RecyclerView) rootView.findViewById(R.id.bluetooth_recycle_view);
        bluetoothDevAdapter = new MyAdapter(mApp,bluetoothDevList);
        bluetoothDevRecycleView.setAdapter(bluetoothDevAdapter);
        bluetoothDevRecycleView.setHasFixedSize(true);
        bluetoothDevLayoutManager = new LinearLayoutManager(mApp);
        bluetoothDevRecycleView.setLayoutManager(bluetoothDevLayoutManager);

//        for test
        bluetoothDevList.add(new MyBluetoothDev("myPhone", 1, 0,"mac"));
        bluetoothDevList.add(new MyBluetoothDev("default_icon",0,0,"mac"));
        bluetoothDevList.add(new MyBluetoothDev("myTablet", 4,0, "mac"));
        bluetoothDevList.add(new MyBluetoothDev("myPhone",1,0,"mac"));
        bluetoothDevList.add(new MyBluetoothDev("default_icon",0,0,"mac"));
        bluetoothDevList.add(new MyBluetoothDev("myTablet",4,0,"mac"));

        return rootView;
    }

    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.point));
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(mLocationSource);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        mLocationSource.deactivate();
        super.onDestroy();
    }
}
