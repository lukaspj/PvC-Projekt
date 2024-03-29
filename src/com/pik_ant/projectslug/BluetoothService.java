package com.pik_ant.projectslug;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class BluetoothService extends Service {


	private BluetoothAdapter adapter;
	private final Context c = this;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid){
	    Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		adapter = BluetoothAdapter.getDefaultAdapter();
		adapter.startDiscovery();
	    IntentFilter filter2, filter3, filter4;
	    filter2 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED);
	    filter3 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED);
	    filter4 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
	    registerReceiver(mReceiver, filter2);
	    registerReceiver(mReceiver, filter3);
	    registerReceiver(mReceiver, filter4);
	    return START_STICKY;
	}

	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            if(device.getAddress().equals("")){
	            	
	            	Toast.makeText(context, "Your target is in range", Toast.LENGTH_LONG).show();
	            }
	        }  
	    }
	};

	@Override
	public void onDestroy() 
	{
		adapter.cancelDiscovery();
	    this.unregisterReceiver(mReceiver);
	    Toast.makeText(this, "BlueDetect Stopped", Toast.LENGTH_LONG).show();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
