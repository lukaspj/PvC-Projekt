package com.pik_ant.projectslug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends Activity {
	
	private SharedPreferences sharedPref;
	private EditText inputUser;
	private EditText inputPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Getting the values for the last used user and pass.
		//Default user and pass are empty strings.
		sharedPref = getPreferences(Context.MODE_PRIVATE);		
		String lastUsrDef = getResources().getString(R.string.last_user_default);
		String lastPassDef = getResources().getString(R.string.last_pass_default);
		String lastUsr = sharedPref.getString(getString(R.string.last_user), lastUsrDef);
		String lastPass = sharedPref.getString(getString(R.string.last_pass), lastPassDef);
		
		//Setting the last user as default for login
		inputUser = (EditText) findViewById(R.id.type_usr);
		inputPass = (EditText) findViewById(R.id.type_pass);
		
		if(!lastUsr.isEmpty()){
			inputUser.setText(lastUsr);
			inputPass.setText(lastPass);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	//Called when pressing login button
	public void login(View view){
		final Intent intent = new Intent(this, Map.class);
		
		//Disable login button, so that the server doesn't get flooded with requests
		Button login_btn = (Button) findViewById(R.id.btn_login);
		login_btn.setClickable(false);
		
		//Loading animation
		final ProgressBar loading_animation = (ProgressBar) findViewById(R.id.load_anim);
		final TextView loading_text = (TextView) findViewById(R.id.load_text);
		loading_animation.setVisibility(0);
		loading_text.setVisibility(0);
		
		//Get user name and password as strings
		final String userName = inputUser.getText().toString();
		final String userPass = inputPass.getText().toString();
		
		User user = new User();
		user.Username = userName;
		user.setPassword(userPass);
		
		CloudInterface.verifyUser(new CloudCallback(){
			
			Runnable accept = new Runnable(){
				@Override
				public void run(){
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.last_user), userName);
					editor.putString(getString(R.string.last_pass), userPass);
					editor.commit();
					
					startActivity(intent);
				}
			};
			
			Runnable decline = new Runnable(){
				@Override
				public void run(){
					//set error message
					loading_animation.setVisibility(4);
					loading_text.setVisibility(4);
				}
			};
			@Override
			public void IsUserRecieved(IsUserResult res){
					if(res == IsUserResult.Registered){
						runOnUiThread(accept);
					}
					else if(res == IsUserResult.NotRegistered){
						runOnUiThread(decline);
					}
					else{
						runOnUiThread(decline);
					}
			}
		}, user);
		
	}
	
	//Called when pressing register button
	public void register(View view){
		Intent i = new Intent(this, Register.class);
		startActivity(i);
	}
	

}
