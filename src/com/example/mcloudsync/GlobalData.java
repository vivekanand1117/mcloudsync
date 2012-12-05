package com.example.mcloudsync;

public enum GlobalData {
	T;

	private String authToken;
	private String EmailID;
	public final String ServerIP="192.168.48.3";
	private int U_Contact = 0;
	private int U_SMS = 0;
	private int D_Contact = 0;
	private int D_SMS = 0;
	private int Request_Code = -1;

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getEmailID() {
		return EmailID;
	}

	public void setEmailID(String EmailID) {
		this.EmailID = EmailID;
	}
	
	public int getU_Contact() {
		return U_Contact;
	}
	
	public void setU_Contact(int U_Contact) {
		this.U_Contact = U_Contact;
	}
	
	public int getU_SMS() {
		return U_SMS;
	}
	
	public void setU_SMS(int U_SMS) {
		this.U_SMS = U_SMS;
	}		
	
	public int getD_Contact() {
		return D_Contact;
	}
	
	public void setD_Contact(int D_Contact) {
		this.D_Contact = D_Contact;
	}
	
	public int getD_SMS() {
		return D_SMS;
	}
	
	public void setD_SMS(int D_SMS) {
		this.D_SMS = D_SMS;
	}		
	
	public int getRequest_Code() {
		return Request_Code;
	}
	
	public void setRequest_Code(int Request_Code) {
		this.Request_Code = Request_Code;
	}	
}
