package com.pik_ant.projectslug;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Register extends Activity {

	private SharedPreferences sharedPref;
	private Resources resourses;
	private EditText usrName;
	private EditText usrPass_1;
	private EditText usrPass_2;
	private Button btn_regi;
	private BluetoothAdapter bta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		resourses = getResources();
		
		//Check for network connection
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork.isConnectedOrConnecting();
		TextView errorConn = (TextView) findViewById(R.id.connectionError);
		
		if(!isConnected){
			btn_regi.setClickable(false);
			btn_regi.setAlpha((float) 0.5);
			errorConn.setText("Not connected to network");
			errorConn.setTextColor(Color.RED);
			errorConn.setVisibility(0);
		}

		//Disable button, until user name is available and passwords match
		btn_regi = (Button) findViewById(R.id.btn_register);
		btn_regi.setClickable(false);
		btn_regi.setAlpha((float) 0.5);
		
		//Get password/user name and views
		usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		usrPass_2 = (EditText) findViewById(R.id.repeat_pass);	
		usrName = (EditText) findViewById(R.id.choose_usr);

		//Add an action listener to the 'repeat password'
		usrPass_2.setOnEditorActionListener(new OnEditorActionListener() {
			boolean userAvail = false;
			boolean passMatch = false;
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {			
				String pass_1 = usrPass_1.getText().toString();
				String pass_2 = usrPass_2.getText().toString();
				String userName = usrName.getText().toString();
				ImageView passCheck = (ImageView) findViewById(R.id.reap_pass_check);
				TextView errorPass = (TextView) findViewById(R.id.passwordError);

				//Check for password length of match
				if(pass_1.length() < 4){
					btn_regi.setClickable(false);
					btn_regi.setAlpha((float) 0.5);
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
					passCheck.setVisibility(0);
					errorPass.setText("Password must have length of at least 4 characters");
					errorPass.setTextColor(Color.RED);
					errorPass.setVisibility(0);
					userAvail = false;
				}
				else if(pass_1.equals(pass_2)){
					errorPass.setVisibility(4);
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
					passCheck.setVisibility(0);
					userAvail = true;
				}
				else {
					btn_regi.setClickable(false);
					btn_regi.setAlpha((float) 0.5);
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
					passCheck.setVisibility(0);
					errorPass.setText("The two passwords doesn't match");
					errorPass.setTextColor(Color.RED);
					errorPass.setVisibility(0);
					userAvail = false;
				}
				
				//Check for user name availability
				CloudInterface.is_username_available(userName, new CloudCallback(){

					ImageView usrCheck = (ImageView) findViewById(R.id.usr_check);
					TextView errorUser = (TextView) findViewById(R.id.userNameError);
					
					//Runnable for available
					Runnable setCheck = new Runnable(){
						@Override
						public void run(){
							usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
							usrCheck.setVisibility(0);
							errorUser.setVisibility(4);
							passMatch = true;
							
							if (passMatch & userAvail){
								btn_regi.setClickable(true);
								btn_regi.setAlpha((float) 1);
							}
						}
					};

					//Runnable for not available
					Runnable setError = new Runnable(){
						@Override
						public void run(){
							btn_regi.setClickable(false);
							btn_regi.setAlpha((float) 0.5);
							usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
							usrCheck.setVisibility(0);
							errorUser.setText("The chosen username is already taken");
							errorUser.setTextColor(Color.RED);
							errorUser.setVisibility(0);
							passMatch = false;
						}
					};	

					@Override
					public void IsUserRecieved(IsUserResult res){
						
						if(res == IsUserResult.Registered){
							runOnUiThread(setError);
						}
						else if(res == IsUserResult.NotRegistered){
							runOnUiThread(setCheck);
						}
						else{
							runOnUiThread(setError);
						}
					}

				});
								
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void register(View view){
		btn_regi.setClickable(false);
		sharedPref = getPreferences(MODE_PRIVATE);
		bta = BluetoothAdapter.getDefaultAdapter();
		
		final Intent intent = new Intent(this, Login.class);
		
		//Loading animation
		final ProgressBar loading_animation = (ProgressBar) findViewById(R.id.load_anim);
		final TextView loading_text = (TextView) findViewById(R.id.load_text);
		loading_animation.setVisibility(0);
		loading_text.setVisibility(0);

		//Get user name and password
		usrName = (EditText) findViewById(R.id.choose_usr);
		usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		final String userName = usrName.getText().toString();
		final String userPass = usrPass_1.getText().toString();
		String bluetoothId = bta.getAddress();
		
		User user = new User();
		user.Username = userName;
		user.setPassword(userPass);
		//Dummy coordinates sending the new user to Java island
		user.lat = -7.491667;
		user.lng = 110.004444;
		user.BluetoothID = bluetoothId;
		
		/*
		CloudInterface.registerUser(user, new CloudCallback(){
			Runnable wasRegistered = new Runnable(){
				@Override
				public void run(){					
					loading_animation.setVisibility(4);
					loading_text.setVisibility(4);
					
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.last_user), userName);
					editor.putString(getString(R.string.last_pass), userPass);
					editor.commit();
					
					startActivity(intent);
				}
			};
			
			@Override
			public void RegisterUserRecieved(int errornum){
				if(errornum == 0){
					runOnUiThread(wasRegistered);
				}
			}
		});*/
		
		

	}

}
