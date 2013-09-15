package com.example.projectslug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author baljenurface
 *
 */
public class Map extends Activity {
	//private GoogleMap gMap;

	private JSONObject json;
	private LocationManager locationManager;
	private GoogleMap gMap;
	private Location curLocation;
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

	/*Sets the Location manager and centers the map on the last known position of the user.
	also the place for the locationListner.*/


	private void getLocation(){
		LocationListener locationListner = new LocationListener() {


			private boolean updateMap = false;

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				locationManager.getProvider(provider);


			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if (!updateMap) {
				curLocation = location;
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(curLocation), 13));
				updateMap = true;
				}
			}
		}; 
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListner);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);
		/*Check wether the Gps is enabled and use AlertDialog
		 * To inform the user and help him go turn it on */
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		    //Ask the user to enable GPS
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("Location Manager");
		    builder.setMessage("Would you like to enable GPS?");
		    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //Launch settings, allowing user to make a change
		            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(i);
		        }
		    });
		    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //No location service, no Activity
		            finish();
		        }
		    });
		    builder.create().show();
		}
	}

	public LatLng getLatLng(Location l){
		return new LatLng(l.getLatitude(), l.getLongitude());
	}

	/** Get the JSON String from the URL and returns empty string otherwise*/

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
