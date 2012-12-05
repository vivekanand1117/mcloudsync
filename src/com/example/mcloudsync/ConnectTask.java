package com.example.mcloudsync;

import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectTask extends
		AsyncTask<String, String, ServerConnectionManager> {

	private LoginScreenActivity parent;
	private ServerConnectionManager scm;

	public ConnectTask(LoginScreenActivity act) {
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
		EditText emailField = (EditText) parent
				.findViewById(R.id.LoginScreen_EditTextEmail);

		String[] messsage_parts = values[0].split(":");

		// FORMAT: [TYPE]:[EMAIL]:[TOKEN]:[DATA]:[EOP]
		// 0 1 2 3 4
		Log.v("REC_M", "RECEIVED:" + values[0]);

		if (messsage_parts.length == 5) {

			String TYPE = messsage_parts[0];

			Log.v("TYPE", "TYPE:" + TYPE);

			if (TYPE.equals("LOGINCHALLENGE")) {
				try {
					String hash_string = messsage_parts[3];
					byte[] CHALLENGE_HASH = Base64.decode(hash_string,
							Base64.DEFAULT);

					Log.v("GOT_LOGINCHALLENGE__4", hash_string);

					// String pass = passwordField.getText().toString();
					Log.v("GOT_PASSWORD", scm.Password);

					byte[] PASS_HASH = CryptoUtils
							.computeSha1HashToArray(scm.Password);

					Log.v("CALC_PASSHASH__4",
							Base64.encodeToString(PASS_HASH, Base64.NO_WRAP));

					if ((PASS_HASH.length == 20)
							&& (CHALLENGE_HASH.length == 20)) {
						byte[] CONCAT_HASH = new byte[40];

						for (int i = 0; i < 20; i++) {
							CONCAT_HASH[i] = CHALLENGE_HASH[i];
							CONCAT_HASH[i + 20] = PASS_HASH[i];
						}

						byte[] LOGIN_RESPONSE = CryptoUtils
								.computeHash(CONCAT_HASH);

						String dataTS = "LOGINRESPONSE:"
								+ emailField.getText().toString()
								+ "::"
								+ Base64.encodeToString(LOGIN_RESPONSE,
										Base64.NO_WRAP) + ":EOP";

						scm.sendMessage(dataTS);

						Log.v("CALC_LOGINRESPONSE__4", dataTS);
					}

				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} else if (TYPE.equals("AUTHSUCCESS")) {
				// validatelabelField.setText("AUTH SUCCESS");

				String auth_token = messsage_parts[3];
				//byte[] AUTH_TOKEN = Base64.decode(auth_token, Base64.DEFAULT);
				GlobalData.T.setAuthToken(auth_token);
				GlobalData.T.setEmailID(emailField.getText().toString().trim());
				parent.setLoggedIn(true);

			} else if (TYPE.equals("AUTHFAILED")) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						parent);
				alertDialog.setTitle("             User Alert !");
				alertDialog.setMessage("AUTH FAILED");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				alertDialog.show();
			}
		}

		// in the arrayList we add the messaged received from server
		// arrayList.add(values[0]);
		// notify the adapter that the data set has changed. This means that new
		// message received
		// from server was added to the list
		// mAdapter.notifyDataSetChanged();
	}
}
