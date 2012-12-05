package com.example.mcloudsync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class BackUpListActivity extends Activity {

	BackUpListActivity x = this;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backuplist);

		// OK BUTTON : Start temporary Backup on phone, if "OK" Button is
		// pressed
		Button okButton = (Button) findViewById(R.id.BackUpList_ButtonOK);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = getIntent();
				final CheckBox contactCheckBox = (CheckBox) findViewById(R.id.BackUpList_CheckboxContact);
				if (contactCheckBox.isChecked()) {

					if (GlobalData.T.getRequest_Code() == 0) {
						GlobalData.T.setU_Contact(1);
					}

					if (GlobalData.T.getRequest_Code() == 1) {
						GlobalData.T.setD_Contact(1);
					}

					// // ************Syncing Contacts**********
					// try {
					// Sync.contactSync(x);
					// } catch (JSONException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync contacts: JSONException "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync contacts: IOException "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// } catch (Exception e) {
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync contacts: Exception "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// }
					//
					// Toast.makeText(getApplicationContext(),
					// "ContactsSynced!",
					// Toast.LENGTH_LONG).show();
				}

				final CheckBox smsCheckBox = (CheckBox) findViewById(R.id.BackUpList_CheckboxSMS);
				if (smsCheckBox.isChecked()) {

					if (GlobalData.T.getRequest_Code() == 0) {
						GlobalData.T.setU_SMS(1);
					}

					if (GlobalData.T.getRequest_Code() == 1) {
						GlobalData.T.setD_SMS(1);
					}

					// // *********Syncing SMS*************
					//
					// try {
					// Sync.smsSync(x);
					// } catch (JSONException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync SMSs: JSONException "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync SMSs: IOException "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// } catch (Exception e) {
					// e.printStackTrace();
					// Toast.makeText(
					// getApplicationContext(),
					// "Could not sync SMSs: Exception "
					// + e.getLocalizedMessage(),
					// Toast.LENGTH_LONG).show();
					// return;
					// }
					//
					// Toast.makeText(getApplicationContext(), "SMSsSynced!",
					// Toast.LENGTH_LONG).show();
				}

				final CheckBox photoCheckBox = (CheckBox) findViewById(R.id.BackUpList_CheckboxPhotos);
				if (photoCheckBox.isChecked()) {
					// Write Code for Syncing Photos
				}
				final CheckBox textfileCheckBox = (CheckBox) findViewById(R.id.BackUpList_Checkbox_Textfiles);
				if (textfileCheckBox.isChecked()) {
					// Write Code for Syncing Photos
				}

				setResult(RESULT_OK, i);
				finish();
			}
		});

		// CANCEL BUTTON : go to previous screen, if "Cancel" Button is pressed
		Button cancelButton = (Button) findViewById(R.id.BackUpList_ButtonCancel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_backuplist, menu);
		return true;
	}
}
