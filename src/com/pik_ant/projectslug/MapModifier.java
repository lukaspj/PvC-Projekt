package com.pik_ant.projectslug;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapModifier implements LocationListener{

	private boolean target = false;
	private boolean updateMap = true;
	private Location curLocation = new Location(LocationManager.GPS_PROVIDER);
	private GoogleMap gMap;
	private LocationManager manager;
	private Activity context;
	private Marker marker;
	private Handler handler = new Handler(Looper.getMainLooper());
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private SharedPreferences sharedPrefs;
	private boolean locationUpdated;

	public MapModifier(GoogleMap map, LocationManager manager, Activity context, final FragmentManager fManager){
		this.gMap = map;
		this.manager = manager;
		this.context = context;
		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker _marker) {
				marker = _marker;
				TargetDialogFragment target = new TargetDialogFragment();
				target.show(fManager, "target dialog");
				return false;
			}
		});
		sharedPrefs = context.getPreferences(Context.MODE_PRIVATE);
		
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
						if(!(curLocation.getLatitude()==u.lat && curLocation.getLongitude()==u.lng)){
							final User u2 = u;
							handler.post(new Runnable() {

								@Override
								public void run() {
									markers.add(map.addMarker(new MarkerOptions()
									.position(new LatLng(u2.lat, u2.lng))
									.title(u2.Username)));
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
						final int _i = i;
						if(i<markers.size()){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									markers.get(_i).setPosition(new LatLng(u.lat, u.lng));
								}
							});
						}
						else{
							handler.post(new Runnable() {

								@Override
								public void run() {
									markers.add(map.addMarker(new MarkerOptions()
									.position(new LatLng(u.lat, u.lng))
									.title(u.Username)));
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
	@SuppressLint("ValidFragment")
	public class TargetDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle b){
			final boolean hasTarget = target;
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(context.getString(R.string.confirm_target, marker.getTitle()));
			builder.setPositiveButton("yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(!hasTarget){
						marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.crosshair));
						target = true;
					}
					else{
						Toast.makeText(getActivity(), context.getString(R.string.error_target_exists), Toast.LENGTH_SHORT).show();
					}
				}
			});
			builder.setNegativeButton("no", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			return builder.create();
			
		}
		

	}
	
	public void hasTarget(boolean b){
		target = b;
	}


	@Override
	public void onLocationChanged(Location location) {
		curLocation = location;
		if (updateMap) {
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(curLocation), 13));
			updateMap = false;
		}
		String userName=sharedPrefs.getString(context.getString(R.string.last_user), "");
		User u = new User(userName, location, 0);

		CloudInterface.updatePosition(u, new CloudCallback(){
			public void UpdatePositionRecieved(int errornum) {
				if(errornum == 0){
					locationUpdated = true;
				}
				else{
					locationUpdated = false;
				}
			};
		});
		updateMarkers();
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
