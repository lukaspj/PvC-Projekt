package com.pik_ant.projectslug;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Register extends Activity {

	private Resources resourses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		resourses = getResources();
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
		
		//Get chosen password as strings
		EditText usrPass_1 = (EditText) findViewById(R.id.choose_pass);
		EditText usrPass_2 = (EditText) findViewById(R.id.repeat_pass);
		String pass_1 = usrPass_1.getText().toString();
		String pass_2 = usrPass_2.getText().toString();
		
		//Check if chosen passwords are equal and set image accordingly
		ImageView usrCheck = (ImageView) findViewById(R.id.usr_check);
		ImageView passCheck = (ImageView) findViewById(R.id.reap_pass_check);
		
		if(pass_1.equals(pass_2)){
			passCheck.setImageDrawable(resourses.getDrawable(R.drawable.raemi_check_mark));
			passCheck.setVisibility(0);
		}
		else {
			passCheck.setImageDrawable(resourses.getDrawable(R.drawable.checkerror));
			passCheck.setVisibility(0);
		}
		
		//Get chosen username as string
		EditText usrName = (EditText) findViewById(R.id.choose_usr);
		String userName = usrName.getText().toString();		
		
	}

}
