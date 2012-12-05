package com.example.mcloudsync;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ConnectTaskTransfers extends
		AsyncTask<String, String, ServerConnectionManager> {

	private HomeScreenActivity parent;
	private ServerConnectionManager scm;

	public ConnectTaskTransfers(HomeScreenActivity act) {
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

			if (TYPE.equals("INVALIDTOKEN")) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						parent);
				alertDialog.setTitle("             User Alert !");
				alertDialog.setMessage("INVALID TOKEN.");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});

				alertDialog.show();
			} else if (TYPE.equals("UPLOADSUCCESS")) {
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
			}
			/*
			 * else if (TYPE.equals("UPLOADFAILED")) {
			 * hs_msg.setText("UPLOADFAILED:" + message_parts[3]); }
			 */
			else if (TYPE.equals("DOWNLOADSUCCESS")) {
				String[] down_status = Status.split("\\|");
				if (down_status[0].equals("0")) {
					if(down_status[1].equals("0"))
					{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							parent);
					alertDialog.setTitle("             User Alert !");
					alertDialog.setMessage("Contact DOWNLOAD FAILED:" + down_status[2]);
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

					alertDialog.show();
					}
					else if(down_status[1].equals("1"))
					{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							parent);
					alertDialog.setTitle("             User Alert !");
					alertDialog.setMessage("SMS DOWNLOAD FAILED:" + down_status[2]);
					alertDialog.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

					alertDialog.show();
					}
				} else if (down_status[0].equals("1")) {
					
					if(down_status[1].equals("0"))
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								parent);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("Contact DOWNLOAD SUCCESS");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								});

						alertDialog.show();
						
						byte[] D_Contact_Data = new byte[0];
						D_Contact_Data = Base64.decode(down_status[2],
								Base64.NO_WRAP);
						String D_Contact_Data_String = new String(D_Contact_Data);
						try {
							Sync.writeContacts(parent.getApplicationContext(),
									D_Contact_Data_String);
							AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
									parent);
							alertDialog1.setTitle("             User Alert !");
							alertDialog1.setMessage("Restore SUCCESSFUL.");
							alertDialog1.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

										}
									});
							alertDialog1.show();
						} catch (Exception e) {
							AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
									parent);
							alertDialog2.setTitle("User Alert!");
							alertDialog2.setMessage("Restore Failed.");
							alertDialog2.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

										}
									});
							alertDialog2.show();
						}
					
					}
					else if(down_status[1].equals("1"))
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								parent);
						alertDialog.setTitle("             User Alert !");
						alertDialog.setMessage("SMS DOWNLOAD SUCCESS");
						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								});

						alertDialog.show();

						byte[] D_SMS_Data = new byte[0];
						D_SMS_Data = Base64.decode(down_status[2],Base64.NO_WRAP);
						
						//Call the function of Writing SMS in database
					
					}		
				}
			}
		}
	}
}
