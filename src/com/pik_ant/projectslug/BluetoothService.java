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
	private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	private final Context c = this;
	public BluetoothService() {
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
	    return null;
	}

	@Override
	public void onCreate() 
	{               

	    Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		adapter.startDiscovery();
	    Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	    IntentFilter filter1, filter2, filter3, filter4;
	    filter1 = new IntentFilter("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
	    filter2 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED);
	    filter3 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED);
	    filter4 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
	    this.registerReceiver(mReceiver, filter1);
	    this.registerReceiver(mReceiver, filter2);
	    this.registerReceiver(mReceiver, filter3);
	    this.registerReceiver(mReceiver, filter4);
	    return START_STICKY;
	}

	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        Toast.makeText(c, "BT change received !", Toast.LENGTH_LONG).show();

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            Toast.makeText(c, device.getName() + " Device found", Toast.LENGTH_LONG).show();
	        }
	        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
	            Toast.makeText(c, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();
	        }
	        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
	            Toast.makeText(c, device.getName() + " Device is about to disconnect", Toast.LENGTH_LONG).show();
	        }
	        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
	            Toast.makeText(c, device.getName() + " Device has disconnected", Toast.LENGTH_LONG).show();
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
}
