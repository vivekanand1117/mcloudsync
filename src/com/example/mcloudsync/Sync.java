package com.example.mcloudsync;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import com.google.gson.Gson;

public class Sync {
	public static class Contact {
		public static class Phone {
			public String number;
			public String type;

			public Phone(String number, String type) {
				super();
				this.number = number;
				this.type = type;
			}
			
			public Phone() {}
		}

		public String name;
		public String email[];
		public Phone phone[];

		public Contact(String name, String[] email, Phone[] phone) {
			super();
			this.name = name;
			this.email = email;
			this.phone = phone;
		}

		public Contact() {
			// TODO Auto-generated constructor stub
		}

	}

	private static Contact phoneNumbers(Context context, int contactId) {
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE,
				Phone.DISPLAY_NAME, Email.DATA, Data.MIMETYPE
		// Phone.
		};
		final Cursor phone = context.getContentResolver().query(
				Data.CONTENT_URI, projection, Data.RAW_CONTACT_ID + "=?",
				new String[] { String.valueOf(contactId) }, null);
		Contact c = null;
		if (phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone
					.getColumnIndex(Phone.NUMBER);
			final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
			String name = null;
			ArrayList<String> email = new ArrayList<String>();
			ArrayList<Contact.Phone> ph = new ArrayList<Contact.Phone>();
			// Coll
			String number = null;
			String type = null;

			while (!phone.isAfterLast()) {
				// System.out.println(contactNumberColumnIndex);
				// System.out.println(phone.getColumnIndex(Email.DATA));
				// System.out.println(phone.getString(phone.getColumnIndex(Data.MIMETYPE)));

				if (phone.getString(phone.getColumnIndex(Data.MIMETYPE))
						.compareTo(Email.CONTENT_ITEM_TYPE) == 0)
					email.add(phone.getString(phone.getColumnIndex(Email.DATA)));

				if (phone.getString(phone.getColumnIndex(Data.MIMETYPE))
						.compareTo(Phone.CONTENT_ITEM_TYPE) == 0) {
					number = phone.getString(contactNumberColumnIndex);
					type = phone.getString(contactTypeColumnIndex);
					ph.add(new Contact.Phone(number, type));
					// System.out.println("Number"+number+"\t"+type);
				}
				if (phone.getString(phone.getColumnIndex(Data.MIMETYPE))
						.compareTo(StructuredName.CONTENT_ITEM_TYPE) == 0) {
					name = phone.getString(phone
							.getColumnIndex(Contacts.DISPLAY_NAME));
					// System.out.println("Name:"+name);
				}
				// final int typeLabelResource =
				// Phone.getTypeLabelResource(type);

				phone.moveToNext();

			}
			c = new Contact(name, email.toArray(new String[0]),
					ph.toArray(new Contact.Phone[0]));

		}
		phone.close();
		return c;
	}

	public static void writeContacts(Context context, String json)
			throws RemoteException, OperationApplicationException, IOException {		
		String str = json;

		System.out.println(str);
		Gson gson = new Gson();
		Contact contacts[] = null;
		try {
		contacts = gson.fromJson(str, Contact[].class);
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("Contacts:\t" + contacts.length);

		for (int i = 0; i < contacts.length; i++) {
			if (contacts[i] == null || contacts[i].name == null)
				continue;
			
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			int rawContactInsertIndex = ops.size();

			ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
					.withValue(RawContacts.ACCOUNT_TYPE, null)
					.withValue(RawContacts.ACCOUNT_NAME, null).build());

			for (com.example.mcloudsync.Sync.Contact.Phone ph : contacts[i].phone) {
				ops.add(ContentProviderOperation
						.newInsert(Data.CONTENT_URI)
						.withValueBackReference(
								ContactsContract.Data.RAW_CONTACT_ID,
								rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, ph.number)
						.withValue(Phone.TYPE, ph.type)
						.build());
			}

			ops.add(ContentProviderOperation
					.newInsert(Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
					.withValue(StructuredName.DISPLAY_NAME, contacts[i].name).build());

			for (String email : contacts[i].email) {
				ops.add(ContentProviderOperation
					.newInsert(Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
					.withValue(Email.DATA, email).build());
			}

			ContentProviderResult[] res = context.getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, ops);

		}
		// Toast.makeText(context, "Contacts restored: "+contacts.length,
		// Toast.LENGTH_LONG).show();
		System.out.println("Contacts added:\t" + contacts.length);
	}

	public static String getContacts(Context context) {
		Cursor rawContacts = null;

		System.out.println("Reading Contacts...");
		// Form an array specifying which columns to return.
		String[] projection = new String[] { Contacts._ID, // the contact id
															// column
				Contacts.DISPLAY_NAME
		// RawContacts.DELETED, // column if this contact is deleted
		// RawContacts.ACCOUNT_NAME,
		// RawContacts.ACCOUNT_TYPE
		};

		String sortOrder = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		String selection = null; // Contacts.IN_VISIBLE_GROUP + " = '0'";
		selection = Contacts.IN_VISIBLE_GROUP + " = '1'"; // 0 for invisible
															// contacts and
															// '1' for
															// visible

		rawContacts = context.getContentResolver().query(Contacts.CONTENT_URI, // the
																				// URI
																				// for
																				// raw
																				// contact
																				// provider
				projection, selection, // selection = null, retrieve all
										// entries
				null, // selection is without parameters
				sortOrder);
		// RawContacts.ACCOUNT_NAME + " ASC");

		int count = rawContacts.getCount();		

		final int contactIdColumnIndex = rawContacts
				.getColumnIndex(Contacts._ID);

		Contact[] contacts = new Contact[count];
		int i = 0;
		if (rawContacts.moveToFirst()) {
			while (!rawContacts.isAfterLast()) { // still a valid entry
													// left?
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				contacts[i++] = phoneNumbers(context, contactId);
				rawContacts.moveToNext(); // move to the next entry				
			}
		}
		rawContacts.close();

		return new Gson().toJson(contacts);
	}

	public static String contactSync(Activity parent) throws JSONException,
			IOException {
		return genericSync(parent, ContactsContract.Contacts.CONTENT_URI,
				"/Contacts");
	}

	public static String smsSync(Activity parent) throws JSONException,
			IOException {
		return genericSync(parent, Uri.parse("content://sms"), "/SMS");
	}

	public static String genericSync(Activity parent, Uri uri, String path)
			throws JSONException, IOException {
		ContentResolver cr = parent.getContentResolver();
		Cursor cur = cr.query(uri, null, null, null, null);

		JSONObject contacts = new JSONObject();
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				JSONObject contact = new JSONObject();
				for (int i = 0; i < cur.getColumnCount(); i++) {
					contact.accumulate(cur.getColumnName(i), cur.getString(i));
				}
				contacts.accumulate(cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID)),
						contact);
			}
			// File extStore = Environment.getExternalStorageDirectory();
			// FileOutputStream fos = new
			// FileOutputStream(extStore.getAbsolutePath() + path);
			return contacts.toString();
			// fos.close();
		}
		return null;
	}

}