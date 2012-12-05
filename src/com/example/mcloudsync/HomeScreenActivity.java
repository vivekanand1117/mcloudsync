package com.example.mcloudsync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreenActivity extends Activity {
	private ServerConnectionManager scm;

	public void setSCM(ServerConnectionManager scm) {
		this.scm = scm;
	}

	@Override
	protected void onResume() {
		super.onResume();
		ConnectTaskTransfers ct = new ConnectTaskTransfers(this);
		ct.execute("");
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		// START SYNC BUTTON
		Button startSyncButton = (Button) findViewById(R.id.HomeScreen_ButtonStartSync);
		startSyncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(getApplicationContext(),
						BackUpListActivity.class);
				GlobalData.T.setRequest_Code(0);
				startActivityForResult(intent, 0); // "0" is request_code
			}
		});

		// RESTORE BUTTON
		Button restoreButton = (Button) findViewById(R.id.HomeScreen_ButtonRestore);
		restoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						BackUpListActivity.class);
				GlobalData.T.setRequest_Code(1);
				startActivityForResult(intent, 1); // "1" is request_code
			}

		});

		// LOGOUT BUTTON : KILL THE ACTIVITY ON THE TOP OF THE STACK

		Button logoutButton = (Button) findViewById(R.id.HomeScreen_ButtonLogout);
		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		// TODO Auto-generated method stub
	}

	@Override
	protected void onActivityResult(int request_code, int result_code, Intent i) {
		if (request_code == 0 && i != null) {
			int flag = 0;
			// TYPE: 0=CONTACTS, 1=SMS, 2=FILE
			if (result_code == RESULT_OK) {
				if (GlobalData.T.getU_Contact() == 1) {
					GlobalData.T.setU_Contact(0);
					flag = 1;
					if (scm.isConnected()) {
						try {
							Context context = getApplicationContext();
							String Contacts =  Sync.getContacts(context);
							
							scm.DoUpload(0, Contacts.getBytes(),"contacts.json");
						} catch (Exception e) {
							Toast.makeText(this, "Couldn't upload json: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					} else {
						
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								HomeScreenActivity.this);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("Sorry,Not connected to Server yet.");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});

						alertDialog.show();							
					}
				}
				if (GlobalData.T.getU_SMS() == 1) {
					GlobalData.T.setU_SMS(0);
					flag = 1;
					if (scm.isConnected()) {
						try {
							scm.DoUpload(1, Sync.smsSync(this).getBytes(),
									"SMS.json");
						} catch (Exception e) {
							Toast.makeText(this, "Couldn't upload json: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					} else {
						
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								HomeScreenActivity.this);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("Sorry,Not connected to Server yet.");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});

						alertDialog.show();							
					}
				}
			}
			if (flag == 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						HomeScreenActivity.this);
				alertDialog.setTitle("             User Alert !");
				alertDialog.setMessage("Nothing Selected for Upload.");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				alertDialog.show();					
			}
		} else if (request_code == 1 && i != null) {
			int flag = 0;
			// TYPE: 0=CONTACTS, 1=SMS, 2=FILE
			if (result_code == RESULT_OK) {
				if (GlobalData.T.getD_Contact() == 1) {
					GlobalData.T.setD_Contact(0);
					flag = 1;
					if (scm.isConnected()) {
						scm.DoDownload(0, "contacts.json");
					} else {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								HomeScreenActivity.this);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("Sorry,Not connected to Server yet.");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});

						alertDialog.show();	
					}
				}
				if (GlobalData.T.getD_SMS() == 1) {
					GlobalData.T.setD_SMS(0);
					flag = 1;
					if (scm.isConnected()) {
						scm.DoDownload(1, "SMS.json");
					} else {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								HomeScreenActivity.this);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("Sorry,Not connected to Server yet.");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});

						alertDialog.show();
					}
				}
			}
			if (flag == 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						HomeScreenActivity.this);
				alertDialog.setTitle("             User Alert !");
				alertDialog.setMessage("Nothing Selected For Download.");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				alertDialog.show();				
			}
		}
		super.onActivityResult(request_code, result_code, i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_homescreen, menu);
		return true;
	}
}
