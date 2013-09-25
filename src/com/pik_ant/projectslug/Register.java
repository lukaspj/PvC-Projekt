package com.pik_ant.projectslug;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		resourses = getResources();

		//Get password and views
		usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		usrPass_2 = (EditText) findViewById(R.id.repeat_pass);		

		//Add an action listener to the 'repeat password', to check if the two passwords are equal
		usrPass_2.setOnEditorActionListener(new OnEditorActionListener() {		
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				String pass_1 = usrPass_1.getText().toString();
				String pass_2 = usrPass_2.getText().toString();
				ImageView passCheck = (ImageView) findViewById(R.id.reap_pass_check);
				TextView errorMess = (TextView) findViewById(R.id.errorMessage);

				if(pass_1.equals(pass_2)){
					errorMess.setVisibility(4);
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
					passCheck.setVisibility(0);
				}
				else {
					passCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
					passCheck.setVisibility(0);
					errorMess.setText(R.string.error_pass_nomatch);
					errorMess.setTextColor(Color.RED);
					errorMess.setVisibility(0);
					
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

		//Get user name and password
		usrName = (EditText) findViewById(R.id.choose_usr);
		usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		String userName = usrName.getText().toString();
		String userPass = usrPass_1.getText().toString();
		final User user = new User();
		user.Username = userName;
		user.setPassword(userPass);

		//Check for user name availability
		CloudInterface.is_username_available(userName, new CloudCallback(){

			ImageView usrCheck = (ImageView) findViewById(R.id.usr_check);
			TextView errorMess = (TextView) findViewById(R.id.errorMessage);
			
			//Runnable for available
			Runnable setCheck = new Runnable(){
				@Override
				public void run(){
					usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
					usrCheck.setVisibility(0);
					errorMess.setVisibility(4);
					CloudInterface.registerUser(new CloudCallback(){}, user);
				}
			};

			//Runnable for not available
			Runnable setError = new Runnable(){
				@Override
				public void run(){
					usrCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
					usrCheck.setVisibility(0);
					errorMess.setText(R.string.error_username_taken);
					errorMess.setTextColor(Color.RED);
					errorMess.setVisibility(0);
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

	}

}
