package com.example.projectslug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
		
		//Get username and password as strings
		EditText usrName = (EditText) findViewById(R.id.type_usr);
		EditText usrPass = (EditText) findViewById(R.id.type_pass);
		String userName = usrName.getText().toString();
		String userPass = usrPass.getText().toString();
		
		//Make request to server here
		
		
		intent.putExtra(MESSAGE, userName);
		startActivity(intent);
	}
	
	//Called when pressing register button
	public void register(View view){
		Intent i = new Intent(this, Register.class);
		startActivity(i);
	}
	

}
