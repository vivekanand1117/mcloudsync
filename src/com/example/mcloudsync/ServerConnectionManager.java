package com.example.mcloudsync;

import android.util.Base64;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnectionManager {

	private String serverMessage;
	public String SERVERIP = GlobalData.T.ServerIP; // your computer IP address
	public static final int SERVERPORT = 2403;
	private OnMessageReceived mMessageListener = null;
	private boolean mRun = false;

	PrintWriter out;
	BufferedReader in;

	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages
	 * received from server
	 */
	public ServerConnectionManager(OnMessageReceived listener, String SERVERIP) {
		this.SERVERIP = SERVERIP;
		mMessageListener = listener;
	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param message
	 *            text entered by client
	 */
	public void sendMessage(String message) {
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stopClient() {
		mRun = false;
	}

	public String Password = "";

	public void DoAuth(String Email, String Password) {
		this.Password = Password;
		sendMessage("LOGINREQUEST:" + Email + ":::EOP");
	}

	public void DoUpload(int TYPE, byte[] CONTENT, String FileName) {
		String Data = TYPE + "|" + FileName + "|"
				+ Base64.encodeToString(CONTENT, Base64.NO_WRAP);
		sendMessage("UPLOADREQUEST:" + GlobalData.T.getEmailID() + ":"
				+ GlobalData.T.getAuthToken() + ":" + Data + ":EOP\r\n");

	}

	// ----------------------------------------------------------------------------------------
	public void DoDownload(int TYPE, String FileName) {
		String Data = TYPE + "|" + FileName;
		sendMessage("DOWNLOADREQUEST:" + GlobalData.T.getEmailID() + ":"
				+ GlobalData.T.getAuthToken() + ":" + Data + ":EOP\r\n");
	}

	public void CreateNewUser(String Email) {

		sendMessage("NEWUSER:" + Email + ":::EOP");
	}

	public void ForgotPassword(String Email) {

		sendMessage("FORGETPASSWORD:" + Email + ":::EOP");
	}

	private boolean IsConnected = false;

	public boolean isConnected() {
		return IsConnected;
	}

	// ----------------------------------------------------------------------------------------
	Socket socket;

	public void initialize() {
		mRun = true;
		try {
			// here you must put your computer's IP address.
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

			Log.e("TCP Client", "C: Connecting...");

			// create a socket to make the connection with the server
			socket = new Socket(serverAddr, SERVERPORT);

			try {
				// send the message to the server
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);

				Log.e("TCP Client", "C: Sent.");

				Log.e("TCP Client", "C: Done.");

				// receive the message which the server sends back
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				// connected

				IsConnected = true;

				// in this while the client listens for the messages sent by the
				// server
				while (mRun) {
					serverMessage = in.readLine();

					if (serverMessage != null && mMessageListener != null) {
						// Log.v("REC_M", "RECEIVED:" + serverMessage);
						// call the method messageReceived from MyActivity class
						mMessageListener.messageReceived(serverMessage);
					}

					serverMessage = null;
				}

				Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
						+ serverMessage + "'");

			} catch (Exception e) {
				Log.e("TCP", "S: Error", e);
			} finally {
				// the socket must be closed. It is not possible to reconnect to
				// this socket
				// after it is closed, which means a new socket instance has to
				// be created.
				socket.close();
			}
		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);
		}

	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the MyActivity
	// class at on asynckTask doInBackground
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
}
