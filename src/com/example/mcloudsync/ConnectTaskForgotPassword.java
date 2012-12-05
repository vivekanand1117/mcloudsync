package com.example.mcloudsync;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ConnectTaskForgotPassword extends
		AsyncTask<String, String, ServerConnectionManager> {

	private ForgotPasswordActivity parent;
	private ServerConnectionManager scm;

	public ConnectTaskForgotPassword(ForgotPasswordActivity act) {
		parent = act;
	}

	@Override
	protected ServerConnectionManager doInBackground(String... message) {

		// we create a TCPClient object and
		scm = new ServerConnectionManager(
				new ServerConnectionManager.OnMessageReceived() {
					@Override
					// here the messageReceived method is implemented
					public void messageReceived(String message) {
						// this method calls the onProgressUpdate
						publishProgress(message);
					}
				}, GlobalData.T.ServerIP);
		parent.setSCM(scm);

		scm.initialize();

		return null;
	}

	/*
	 * protected void onPostExecute(Boolean result) { //Toast.makeText(activity,
	 * Boolean.toString(result), Toast.LENGTH_LONG).show();
	 * 
	 * activity.startActivity(new Intent(activity, HomeScreenActivity.class)); }
	 */

	protected void onPostExecute(Boolean result) {
		Toast.makeText(parent, Boolean.toString(result), Toast.LENGTH_LONG)
				.show();

		// activity.startActivity(new Intent(activity, BuiltInCamera.class));
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);		

		// EditText passwordField = (EditText)
		// parent.findViewById(R.id.LoginScreen_EditTextPassword);

		String[] message_parts = values[0].split(":");

		// FORMAT: [TYPE]:[EMAIL]:[TOKEN]:[DATA]:[EOP]
		// 0 1 2 3 4
		Log.v("REC_M", "RECEIVED:" + values[0]);

		if (message_parts.length == 5) {
			String TYPE = message_parts[0];
			String Status = message_parts[3];

			Log.v("TYPE", "TYPE:" + TYPE);

			if (TYPE.equals("STATUS")) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						parent);
				alertDialog.setTitle("             User Alert !");
				alertDialog.setMessage(Status);
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				alertDialog.show();					
				(parent.findViewById(R.id.ForgotPassword_ButtonResetPassword)).setEnabled(true);
			}			
		}		
	}
}
