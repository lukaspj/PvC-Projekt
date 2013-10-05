package com.pik_ant.projectslug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author baljenurface
 *
 */
public class Map extends Activity{

	private LocationManager locationManager;
	private GoogleMap gMap;
	private MapModifier modifier;
	private BluetoothAdapter adapter;
	private WifiManager wifiManager;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private HashMap<String, Location> bssidLocationMap = new HashMap<String, Location>();
	private String bluetoothTarget = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		getActionBar().hide();

		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = mapFragment.getMap();
		gMap.setMyLocationEnabled(true);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		modifier = new MapModifier(gMap, locationManager, this, getFragmentManager());
		modifier.getLocation();
		modifier.getLocationMarkers();
	    IntentFilter filter = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND);
	    IntentFilter filter2 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED);
	    registerReceiver(mReceiver, filter);
	    registerReceiver(mReceiver, filter2);
		adapter = BluetoothAdapter.getDefaultAdapter();
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		wifiManager.startScan();
		List<ScanResult> scanResult = wifiManager.getScanResults();
		for(ScanResult s : scanResult){
			if (s.level >= 15){
				bssidLocationMap.put(s.BSSID , modifier.getCurLocation());
			}
		}
		if (!adapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 1);
		}
		adapter.startDiscovery();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		modifier.updateMarkers();
	}
	
	
	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            devices.add(device);
	            if(device.getAddress().equals(bluetoothTarget)){
	            	Toast.makeText(context, "Your target is in range", Toast.LENGTH_LONG);
	            }
	        }
	        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
	            Toast.makeText(context, device.getName() + " Device is now connected", Toast.LENGTH_LONG).show();
	        }      
	    }
	};
	//we use degrees as range so 0.001 degrees is arround 
	public void findNewTarget(View v){
		CloudInterface.radiusSearch(new User("", modifier.getCurLocation(),""), 0.001, new CloudCallback(){
			public void RadiusSearchRecieved(List<User> lis){
				if(!lis.isEmpty()){
					int i = new Random().nextInt(lis.size()-1);
					modifier.setTargetIcon(lis.get(i).BluetoothID);
					bluetoothTarget = lis.get(i).BluetoothID;
				}
			}
		});
//		int i = new Random().nextInt(devices.size()-1);
//		modifier.setTargetIcon(devices.get(i).getAddress());
//		bluetoothTarget = devices.get(i).getAddress();
		
	}
	

	
}
