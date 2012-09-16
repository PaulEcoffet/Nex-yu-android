/**
 * 
 */
package org.nexyu.nexyuAndroid.ContactsManagement;

import java.util.ArrayList;

import org.nexyu.nexyuAndroid.service.NexyuService;
import org.nexyu.nexyuAndroid.utils.ArrayUtils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

/**
 * @author Paul Ecoffet
 */
public class ContactsGatherer
{
	private NexyuService	service;
	private Uri				uri			= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private String[]		projections	= new String[] {
			ContactsContract.CommonDataKinds.Phone.NUMBER,
			ContactsContract.CommonDataKinds.Phone.TYPE,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.STARRED,
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
	private String			order		= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
												+ "ASC";

	/**
	 * @param service
	 */
	public ContactsGatherer(NexyuService service)
	{
		this.service = service;
	}

	public ArrayList<Contact> gatherContacts()
	{
		SparseArray<Contact> contactsList = new SparseArray<Contact>();

		Cursor cur = service.getContentResolver().query(uri, projections, null, null, order);
		int indexContactId = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
		int indexPhoneNumber = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		int indexPhoneType = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
		int indexDisplayedName = cur
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int indexStarred = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED);
		while (cur.moveToNext())
		{
			Contact curContact = contactsList.get(cur.getInt(indexContactId));
			String displayedName = cur.getString(indexDisplayedName);
			String phoneNumber = cur.getString(indexPhoneNumber);
			int phoneType = cur.getInt(indexPhoneType);
			int starred = cur.getInt(indexStarred);

			if (curContact == null)
			{
				curContact = new Contact(displayedName, phoneNumber, phoneType, starred);
			}
			else
			{
				curContact.addPhone(phoneNumber, phoneType);
			}
		}
		return ArrayUtils.SparseArrayToArrayList(contactsList);
	}
}
