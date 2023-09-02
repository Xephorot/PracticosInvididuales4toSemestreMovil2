package com.example.ejercicio8;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BluetoothHeadphonesReceiver bluetoothHeadphonesReceiver;
    private TextView textView;

    private BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothHeadphonesReceiver.ACTION_BLUETOOTH_DEVICE_CONNECTED.equals(intent.getAction())) {
                String deviceName = intent.getStringExtra(BluetoothHeadphonesReceiver.EXTRA_DEVICE_NAME);
                textView.setText("Dispositivo Bluetooth conectado: " + deviceName);
            } else if (BluetoothHeadphonesReceiver.ACTION_BLUETOOTH_DEVICE_DISCONNECTED.equals(intent.getAction())) {
                textView.setText("Dispositivo Bluetooth desconectado");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bluetoothHeadphonesReceiver = new BluetoothHeadphonesReceiver();
        registerReceiver(bluetoothHeadphonesReceiver, BluetoothHeadphonesReceiver.getIntentFilter());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothHeadphonesReceiver.ACTION_BLUETOOTH_DEVICE_CONNECTED);
        intentFilter.addAction(BluetoothHeadphonesReceiver.ACTION_BLUETOOTH_DEVICE_DISCONNECTED);
        registerReceiver(localBroadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothHeadphonesReceiver);
        unregisterReceiver(localBroadcastReceiver);
    }
}
