package com.pik_ant.projectslug;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Register extends Activity {

	private Resources resourses;
	private EditText usrName;
	private EditText usrPass_1;
	private EditText usrPass_2;
	private boolean userAvailability;
	private boolean userChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		resourses = getResources();

		//Get password views
		usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		usrPass_2 = (EditText) findViewById(R.id.repeat_pass);

		//Add a action listener to the repeat password, to check if the two passwords are equal
		usrPass_2.setOnEditorActionListener(new OnEditorActionListener() {		
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				String pass_1 = usrPass_1.getText().toString();
				String pass_2 = usrPass_2.getText().toString();
				ImageView passCheck = (ImageView) findViewById(R.id.reap_pass_check);

				if(pass_1.equals(pass_2)){
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
					passCheck.setVisibility(0);
				}
				else {
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
					passCheck.setVisibility(0);
				}
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
		//Loading animation
		ProgressBar loading_animation = (ProgressBar) findViewById(R.id.load_anim);
		TextView loading_text = (TextView) findViewById(R.id.load_text);
		loading_animation.setVisibility(0);
		loading_text.setVisibility(0);

		//Get user name view
		usrName = (EditText) findViewById(R.id.choose_usr);
		String userName = usrName.getText().toString();

		CloudInterface.is_username_available(userName, new CloudCallback(){

			@Override
			public void IsUserRecieved(IsUserResult res){

				final String result = res.toString();

				if(result.equals("NotRegistered")){
					userAvailable();
				}
				checkedUserAvailability();

			}

		});
		
		ImageView usrCheck = (ImageView) findViewById(R.id.usr_check);
		
		while(!userChecked){
			//... wait until result comes in... 
			//wanna play cards or something?
			//this is a stupid hack...
		}
		
		if(userAvailability){
			usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
			usrCheck.setVisibility(0);
		}
		else{
			usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
			usrCheck.setVisibility(0);
		}

	}

	public void userAvailable(){
		userAvailability = true;
	}

	public void checkedUserAvailability(){
		userChecked = true;
	}

}
