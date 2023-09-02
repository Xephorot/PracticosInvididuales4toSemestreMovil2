package com.example.ejercicio8;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothHeadphonesReceiver extends BroadcastReceiver {

    public static final String ACTION_BLUETOOTH_DEVICE_CONNECTED = "com.example.ejercicio8.ACTION_BLUETOOTH_DEVICE_CONNECTED";
    public static final String ACTION_BLUETOOTH_DEVICE_DISCONNECTED = "com.example.ejercicio8.ACTION_BLUETOOTH_DEVICE_DISCONNECTED";
    public static final String EXTRA_DEVICE_NAME = "com.example.ejercicio8.EXTRA_DEVICE_NAME";

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        return filter;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Integer state = null;

        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action) || "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(action) || BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            if ("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(action)) {
                state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
                if (state != BluetoothProfile.STATE_CONNECTED && state != BluetoothProfile.STATE_DISCONNECTED) {
                    return;
                }
            }

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Intent deviceIntent;

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action) || (state != null && state == BluetoothProfile.STATE_CONNECTED)) {
                deviceIntent = new Intent(ACTION_BLUETOOTH_DEVICE_CONNECTED);
            } else {
                deviceIntent = new Intent(ACTION_BLUETOOTH_DEVICE_DISCONNECTED);
            }

            deviceIntent.putExtra(EXTRA_DEVICE_NAME, device.getName());
            context.sendBroadcast(deviceIntent);
        }
    }
}
