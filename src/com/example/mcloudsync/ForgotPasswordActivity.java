package com.example.mcloudsync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends Activity {

	private ServerConnectionManager scm;

	public void setSCM(ServerConnectionManager scm) {
		this.scm = scm;
	}

	@Override
	protected void onResume() {
		super.onResume();
		ConnectTaskForgotPassword ct = new ConnectTaskForgotPassword(this);
		ct.execute("");
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);

		// ENETER BUTTON : Match both the password fields
		Button resetPasswordButton = (Button) findViewById(R.id.ForgotPassword_ButtonResetPassword);
		resetPasswordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final EditText emailField = (EditText) findViewById(R.id.ForgotPassword_EditTextEmail);
				String mail_ID = emailField.getText().toString();

				if (mail_ID.length() == 0) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ForgotPasswordActivity.this);
					alertDialog.setTitle("             User Alert !");
					alertDialog.setMessage("Please Enter Valid Username!!!");
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});

					alertDialog.show();
				}

				if (scm.isConnected()) {
					((Button) findViewById(R.id.ForgotPassword_ButtonResetPassword))
							.setEnabled(false);

					scm.ForgotPassword(mail_ID);
				} else {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ForgotPasswordActivity.this);
					alertDialog.setTitle("             User Alert !");
					alertDialog.setMessage("Sorry, Not connect to server yet.");
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});

					alertDialog.show();
				}
			}
		});

		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_forgotpassword, menu);
		return true;
	}

}
