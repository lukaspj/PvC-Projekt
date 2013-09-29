package com.pik_ant.projectslug;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * @author baljenurface
 *
 */
public class Map extends Activity{
	//private GoogleMap gMap;

	private LocationManager locationManager;
	private GoogleMap gMap;
	//Use a shared preference with key last_user instead
	private MapModifier modifier;
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
		Intent i = new Intent(this, BluetoothService.class);
		startService(i);	
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
	
	
	
}
