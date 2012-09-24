/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
 */
package org.nexyu.nexyuAndroid.ContactsManagement;

import java.util.ArrayList;

import org.nexyu.nexyuAndroid.client.protocol.Contact;
import org.nexyu.nexyuAndroid.utils.ArrayUtils;

import android.app.Service;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

/**
 * Class used to gather the contacts on the phone and to associate their phone
 * numbers.
 * 
 * @author Paul Ecoffet
 */
public class ContactsGatherer
{
	private Uri			uri			= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private String[]	projections	= new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
			ContactsContract.CommonDataKinds.Phone.TYPE,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.STARRED,
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
	private String		order		= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
	private Service		service;

	/**
	 * The default constructor
	 * 
	 * @param service
	 *            The service is needed to get the contentResolver.
	 * @author Paul Ecoffet
	 */
	public ContactsGatherer(Service service)
	{
		this.service = service;
	}

	/**
	 * Gather all the contacts with at least a phone number, take their name,
	 * their phone numbers and whether they are starred or not and put it in an
	 * ArrayList.
	 * 
	 * @author Paul Ecoffet
	 */
	public ArrayList<Contact> gatherContactsWithPhoneNumbers()
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
			int id = cur.getInt(indexContactId);
			Contact curContact = contactsList.get(id);
			String displayedName = cur.getString(indexDisplayedName);
			String phoneNumber = cur.getString(indexPhoneNumber);
			int phoneType = cur.getInt(indexPhoneType);
			int starred = cur.getInt(indexStarred);

			if (curContact == null)
			{
				curContact = new Contact(displayedName, phoneNumber, phoneType, starred);
				contactsList.append(id, curContact);
			}
			else
			{
				curContact.addPhone(phoneNumber, phoneType);
			}
		}
		return ArrayUtils.SparseArrayToArrayList(contactsList);
	}
}
