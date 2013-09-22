package com.pik_ant.projectslug;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.pik_ant.projectslug.R;

/**
 * @author baljenurface
 *
 */
public class Map extends Activity{
	//private GoogleMap gMap;

	private LocationManager locationManager;
	private GoogleMap gMap;
	private String userName;
	private MapModifier modifier;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getActionBar().hide();
		userName = getIntent().getExtras().getString(Login.MESSAGE);
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = mapFragment.getMap();
		gMap.setMyLocationEnabled(true);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		modifier = new MapModifier(gMap, locationManager, this, getFragmentManager());
		modifier.getLocation();
		modifier.getLocationMarkers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
}