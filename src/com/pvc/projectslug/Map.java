package com.pvc.projectslug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.JsonReader;
import android.view.Menu;

/**
 * @author baljenurface
 *
 */
public class Map extends Activity {
	private GoogleMap gMap;
	private LocationManager locationManager;
	private JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = mapFragment.getMap();
		gMap.setMyLocationEnabled(true);
		getActionBar().hide();
		getLocation();
		try {
			json = new JSONObject(getJSONString(new URL("")));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}
	/*
	Sets the Location manager and centers the map on the last known position of the user.
	also the place for the locationListner.
	 */
	private void getLocation(){
		LocationListener locationListner = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				double latitude = locationManager.getLastKnownLocation(provider).getLatitude();
				double longitude = locationManager.getLastKnownLocation(provider).getLongitude();
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15));
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				//				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				//				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
			}
		};
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListner);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
				/*double latitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude();
				double longitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude();
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
*/	}
	
	private String getJSONString(URL url){
		BufferedReader reader = null;
		try {
			HttpURLConnection server = (HttpURLConnection) url.openConnection();
			reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}


	
}
