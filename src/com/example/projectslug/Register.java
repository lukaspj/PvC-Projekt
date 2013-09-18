package com.example.projectslug;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void register(View view){
		//Get chosen password as strings
		EditText usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		EditText usrPass_2 = (EditText) findViewById(R.id.repeat_pass);
		String pass_1 = usrPass_1.getText().toString();
		String pass_2 = usrPass_2.getText().toString();
		
		//Get chosen username as string
		EditText usrName = (EditText) findViewById(R.id.choose_usr);
		String userName = usrName.getText().toString();		
		
		//Check if the passwords are equal are equal
		if (pass_1.equals(pass_2)){
			//check if username is available
		}
	}

}
