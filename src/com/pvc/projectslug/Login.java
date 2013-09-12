package com.pvc.projectslug;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Login extends Activity {

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
		//TO DO: Make request to server
		Intent intent = new Intent(this, Welcome.class);
		EditText usrName = (EditText) findViewById(R.id.type_usr);
		String welcomeUser = "Welcome " + usrName.getText().toString();
		intent.putExtra("Extra text", welcomeUser);
		startActivity(intent);
	}

}
