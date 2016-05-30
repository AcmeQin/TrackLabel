package com.qinacme.tracklabeltry;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinacme on 2016/5/22.
 */
public class CurrentFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private Context mApp;

    private MediaPlayer mMediaPlayer;

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
    private Button playButton;
    private Button pauseButton;
    private BroadcastReceiver myReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MyBluetoothDev newDevice = new MyBluetoothDev(device.getName(),
                        device.getBluetoothClass().getMajorDeviceClass() ,
                        device.getBluetoothClass().getDeviceClass(), device.getAddress());
                if (!bluetoothDevList.contains(newDevice)){
                    bluetoothDevList.add(newDevice);
                    bluetoothDevAdapter.notifyDataSetChanged();
                }
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                Toast.makeText(mApp, name+"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mBluetoothAdapter.startDiscovery();
            }
        }
    };

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Member object for the chat services
     */
    private BluetoothRingService mRingService = null;

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

        playButton = (Button) rootView.findViewById(R.id.play_button);
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mMediaPlayer.isPlaying())
//                    try {
//                        mMediaPlayer.prepare();
//                        mMediaPlayer.start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//            }
//        });

        pauseButton = (Button) rootView.findViewById(R.id.pause_button);
//        pauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMediaPlayer.stop();
//            }
//        });


        bluetoothDevRecycleView = (RecyclerView) rootView.findViewById(R.id.bluetooth_recycle_view);
        bluetoothDevAdapter = new MyAdapter(mApp,bluetoothDevList);
        bluetoothDevRecycleView.setAdapter(bluetoothDevAdapter);
        bluetoothDevRecycleView.setHasFixedSize(true);
        bluetoothDevLayoutManager = new LinearLayoutManager(mApp);
        bluetoothDevRecycleView.setLayoutManager(bluetoothDevLayoutManager);

//        for test
//        bluetoothDevList.add(new MyBluetoothDev("myPhone", 1, 0,"mac"));
//        bluetoothDevList.add(new MyBluetoothDev("default_icon",0,0,"mac"));
//        bluetoothDevList.add(new MyBluetoothDev("myTablet", 4,0, "mac"));
//        bluetoothDevList.add(new MyBluetoothDev("myPhone",1,0,"mac"));
//        bluetoothDevList.add(new MyBluetoothDev("default_icon",0,0,"mac"));
//        bluetoothDevList.add(new MyBluetoothDev("myTablet",4,0,"mac"));

        return rootView;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer){
        mMediaPlayer = mediaPlayer;
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
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mRingService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mRingService.getState() == BluetoothRingService.STATE_NONE) {
                // Start the Bluetooth chat services
                mRingService.start();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mRingService == null) {
            setupRing();
        }
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


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothRingService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothRingService.STATE_CONNECTING:
                            setStatus(getString(R.string.title_connecting));
                            break;
                        case BluetoothRingService.STATE_LISTEN:
                        case BluetoothRingService.STATE_NONE:
                            setStatus(getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
                    // TODO: 2016/5/30 Transform into play/stop


//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (readMessage.equals("play")){
                        if (!mMediaPlayer.isPlaying())
                            try {
                                mMediaPlayer.prepare();
                                mMediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }else if(readMessage.equals("pause")){
                        mMediaPlayer.stop();

                    }
                    // TODO: 2016/5/30 Transform into play/stop.

//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }


    /**
     * Set up the UI and background operations for chat.
     */
    private void setupRing() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
//        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

//        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
//        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the play button with a listener that for click events
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    String message = "play";
                    sendMessage(message);
                }
            }
        });


        // Initialize the pause button with a listener that for click events
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    String message = "pause";
                    sendMessage(message);
                }
            }
        });


        // Initialize the BluetoothChatService to perform bluetooth connections
        mRingService = new BluetoothRingService(mApp, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }



    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mRingService.getState() != BluetoothRingService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mRingService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }



}
