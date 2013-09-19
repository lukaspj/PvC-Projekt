package com.pik_ant.projectslug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends Activity {
	
	public final static String MESSAGE = "USER_NAME";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	//Called when pressing login button
	public void login(View view){
		Intent intent = new Intent(this, Map.class);
		
		//Disble login button, so that the server doesn't get flooded with requests
		Button login_btn = (Button) findViewById(R.id.btn_login);
		login_btn.setClickable(false);
		
		//Loading animation
		ProgressBar loading_animation = (ProgressBar) findViewById(R.id.load_anim);
		TextView loading_text = (TextView) findViewById(R.id.load_text);
		loading_animation.setVisibility(0);
		loading_text.setVisibility(0);
		
		//Get username and password as strings
		EditText usrName = (EditText) findViewById(R.id.type_usr);
		EditText usrPass = (EditText) findViewById(R.id.type_pass);
		String userName = usrName.getText().toString();
		String userPass = usrPass.getText().toString();
		
		//Request server for login
		//GET TO IT LUKAS!!!
		
		intent.putExtra(MESSAGE, userName);
		startActivity(intent);
	}
	
	//Called when pressing register button
	public void register(View view){
		Intent i = new Intent(this, Register.class);
		startActivity(i);
	}
	

}
