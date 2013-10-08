package com.pik_ant.projectslug;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapModifier implements LocationListener{

	private boolean updateMap = true;
	private Location curLocation = new Location(LocationManager.GPS_PROVIDER);
	private GoogleMap gMap;
	private LocationManager manager;
	private Activity context;
	private Handler handler = new Handler(Looper.getMainLooper());
	public HashMap<String, Marker> userMarkersMap = new HashMap<String, Marker>();
	private Marker targetMarker;
	private String username = Login.user_name;
	public MapModifier(GoogleMap map, LocationManager manager, Activity context, final FragmentManager fManager){
		this.gMap = map;
		this.manager = manager;
		this.context = context;
		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.crosshair));
				return false;
			}
		});
		context.getPreferences(Context.MODE_PRIVATE);
		
	}

	public static LatLng getLatLng(Location l){
		return new LatLng(l.getLatitude(), l.getLongitude());
	}

	public void getLocationMarkers(){
		final GoogleMap map = gMap;
		CloudInterface.getUsers(new CloudCallback(){
			public void GetUsersRecieved(List<User> lis){
				if(!lis.isEmpty()){
					for(User u : lis){
						if(!(u.Username.equals(username))){
							final User u2 = u;
							handler.post(new Runnable() {

								@Override
								public void run() {
									Marker m = map.addMarker(new MarkerOptions()
									.position(new LatLng(u2.lat, u2.lng))
									.title(u2.Username));
									userMarkersMap.put(u2.Username, m );
								}
							});

						}
					}
				}
			}
		});

	}
	public void updateMarkers(){
		final GoogleMap map = gMap;
		CloudInterface.getUsers(new CloudCallback(){
			public void GetUsersRecieved(List<User> lis){
				if(!lis.isEmpty()){
					for(int i = 0; i<lis.size(); i++){
						final User u = lis.get(i);
						if(userMarkersMap.get(u.Username) != null){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									userMarkersMap.get(u.Username).setPosition(new LatLng(u.lat, u.lng));
								}
							});
						}
						else if(!(u.Username.equals(username))){
							handler.post(new Runnable() {

								@Override
								public void run() {
									Marker m = map.addMarker(new MarkerOptions()
									.position(new LatLng(u.lat, u.lng))
									.title(u.Username));
									userMarkersMap.put(u.Username, m);
								}
							});
						}
					}
				}
			}
		});
	}
	/*Sets the Location manager and centers the map on the last known position of the user.
	also the place for the locationListner.*/
	public void getLocation(){
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10,this);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
		/*Check weather the GPS is enabled and use AlertDialog
		 * To inform the user and help him go turn it on */
		if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			//Ask the user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Location Manager");
			builder.setMessage("Would you like to enable GPS?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Launch settings, allowing user to make a change
					Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(i);
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//What to do when no LocationProvider enabled?
				}
			});
			builder.create().show();
		}
	}


	@Override
	public void onLocationChanged(Location location) {
		curLocation = location;
		if (updateMap) {
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(curLocation), 13));
			updateMap = false;
		}
		User u = new User(username, location, "");

		CloudInterface.updatePosition(u, new CloudCallback(){
			public void UpdatePositionRecieved(int errornum) {
				if(errornum == 0){
				}
				else{
				}
			};
		});
		updateMarkers();
	}

	
	public void setTargetIcon(String username){
		final Marker m = userMarkersMap.get(username);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				//	TODO Auto-generated method stub
				m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.crosshair));
				targetMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
				targetMarker = m;
			}
		});
	}
	
	public Location getCurLocation(){
		return curLocation;
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
	
}
