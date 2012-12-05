package com.example.mcloudsync;

import com.example.mcloudsync.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

public class LoginChoiceActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginchoice);		

		// If authentication using SERVER

		Button serverButton = (Button) findViewById(R.id.LoginChoice_ButtonServer);
		serverButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*Intent intent = new Intent(getApplicationContext(),
						LoginScreenActivity.class);*/
				Intent intent = new Intent(getApplicationContext(), LoginScreenActivity.class);				
				startActivity(intent);
				finish();
			}
		});

		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_loginscreen, menu);
		return true;
	}

}
