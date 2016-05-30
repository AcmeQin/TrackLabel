package com.qinacme.tracklabeltry;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinacme on 2016/5/22.
 */
public class HistoryFragment extends Fragment {
    private Context mApp;

    private AMap aMap;
    private MapView mapView;
    private MyLocationSource mLocationSource;

    private RecyclerView bluetoothHistRecycleView;
    private RecyclerView.Adapter bluetoothHistAdapter;
    private RecyclerView.LayoutManager bluetoothHistLayoutManager;

    private List<MyBluetoothHistDev> bluetoothHistList = new ArrayList<MyBluetoothHistDev>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);
        mApp = getActivity();
        mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mLocationSource = new MyLocationSource(mApp);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        bluetoothHistRecycleView = (RecyclerView) rootView.findViewById(R.id.bluetooth_history_recycle_view);
        bluetoothHistAdapter = new MyHistAdapter(mApp,bluetoothHistList);
        bluetoothHistRecycleView.setAdapter(bluetoothHistAdapter);
        bluetoothHistRecycleView.setHasFixedSize(true);
        bluetoothHistLayoutManager = new LinearLayoutManager(mApp);
        bluetoothHistRecycleView.setLayoutManager(bluetoothHistLayoutManager);

        bluetoothHistList.add(new MyBluetoothHistDev(new MyBluetoothDev("myPhone", 1, 0, "mac"),
                mLocationSource.getCurLocationDate(), mLocationSource.getCurLatitude(),
                mLocationSource.getCurLongtitude()));
        bluetoothHistAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

}
