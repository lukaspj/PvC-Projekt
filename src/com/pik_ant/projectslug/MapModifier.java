package com.pik_ant.projectslug;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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

	private boolean updateMap = true;
	private Location curLocation = new Location(LocationManager.GPS_PROVIDER);
	private GoogleMap gMap;
	private LocationManager manager;
	private Context context;
	private static Marker marker;
	private Handler handler = new Handler(Looper.getMainLooper());
	private static boolean hasTarget = false;

	public MapModifier(GoogleMap map, LocationManager manager, Context context, final FragmentManager fManager){
		this.gMap = map;
		this.manager = manager;
		this.context = context;
		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker _marker) {
				// TODO Auto-generated method stub
				marker = _marker;
				TargetDialogFragment target= new TargetDialogFragment();
				target.show(fManager, "target dialog");
				return false;
			}
		});
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
									// TODO Auto-generated method stub
									map.addMarker(new MarkerOptions()
									.position(new LatLng(u2.lat, u2.lng))
									.title(u2.Username));
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
	public static class TargetDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle b){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Want to set" + marker.getTitle() + "as target?");
			builder.setPositiveButton("yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(!hasTarget ){
						marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.slug_launcher));
						hasTarget = true;
					}
					else{
						Toast.makeText(getActivity(), "you all ready have a target",Toast.LENGTH_SHORT).show();
					}
				}
			});
			builder.setNegativeButton("no", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			return builder.create();
			
		}
	}
	public void hasTarget(boolean b){
		hasTarget = b;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		curLocation = location;
		if (updateMap) {
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(curLocation), 13));
			updateMap = false;
		}
		//CloudInterface.updatePosition(userName, location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
