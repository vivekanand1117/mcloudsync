package com.example.mcloudsync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreenActivity extends Activity {

	// ServerConnectionManager scm ;

	LoginScreenActivity lsa = this;

	//private boolean loggedIn;
	Intent intent;
	private ServerConnectionManager scm;

	public void setLoggedIn(boolean loggedIn) {
		if (loggedIn == true) {
			Intent intx = new Intent(getApplicationContext(),
					HomeScreenActivity.class);
			startActivity(intx);
			finish();
		}
		// this.loggedIn = loggedIn;
	}

	/*
	 * void CallHomeScreen(String Token) { //intent.putExtra("AUTHTOKEN",
	 * Token);
	 * 
	 * Log.v("HOME SCREEN AUTHTOKEN 2", Token);
	 * 
	 * //startActivity(intent); }
	 */

	public void setSCM(ServerConnectionManager scm) {
		this.scm = scm;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginscreen);

		intent = new Intent(getApplicationContext(), HomeScreenActivity.class);

		// LOGIN BUTTON : Check the Credentials and Open HomeScreen Activity, if
		// credentials are valid
		Button loginButton = (Button) findViewById(R.id.LoginScreen_ButtonLogin);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				

				final EditText emailField = (EditText) findViewById(R.id.LoginScreen_EditTextEmail);
				String mail_ID = emailField.getText().toString();

				final EditText passwordField = (EditText) findViewById(R.id.LoginScreen_EditTextPassword);
				String password = passwordField.getText().toString();

				if (emailField.length() == 0 || passwordField.length() == 0) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							LoginScreenActivity.this);
					alertDialog.setTitle("             User Alert !");
					alertDialog
							.setMessage("Please Enter Valid Username and Password");
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

					alertDialog.show();
				}

				if (scm.isConnected()) {

					scm.DoAuth(mail_ID, password);
				} else {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							LoginScreenActivity.this);
					alertDialog.setTitle("             User Alert !");
					alertDialog
							.setMessage("Sorry, Not connect to server yet.");
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

					alertDialog.show();					
				}

				/**/

				// Store Email Id and Password in a file with Packet format and
				// Send the packet to Server and get the Reg_ID and comments
				/*
				 * int flag = 1; // set 1 if credentials are not valid if (flag
				 * == 1) { validatelabelField.setEnabled(true);
				 * validatelabelField
				 * .setText("*Email-ID or Password Incorrect");
				 * //passwordField.setText(null); } else { Intent intent = new
				 * Intent(getApplicationContext(), HomeScreenActivity.class);
				 * startActivity(intent); }
				 */
			}
		});

		// CANCEL BUTTON : Exit Application, if "Cancel" Button is pressed
		Button cancelButton = (Button) findViewById(R.id.LoginScreen_ButtonCancel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
			}
		});

		// FORGOT PASSWORD BUTTON : Open Forgot Password Activity, if Forgot
		// Password Button is pressed
		Button forgotPasswordButton = (Button) findViewById(R.id.LoginScreen_ButtonForgotPassword);
		forgotPasswordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginScreenActivity.this,
						ForgotPasswordActivity.class);
				startActivity(intent);
			}
		});

		// SIGNUP BUTTON : Open SignUp Activity, if "Signup for New User" Button
		// is pressed
		Button signupButton = (Button) findViewById(R.id.LoginScreen_ButtonNewUser);
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginScreenActivity.this,
						SignUpActivity.class);
				startActivity(intent);
			}
		});

		// TODO Auto-generated method stub
	}

	@Override
	protected void onResume() {
		super.onResume();
		ConnectTask ct = new ConnectTask(this);
		ct.execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_loginscreen, menu);
		return true;
	}
}
