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
package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * List of contacts "ready-to-be-sent" because it implements
 * {@link NetworkMessageable}.
 * 
 * @author Paul Ecoffet
 */
public class ContactsList implements NetworkMessageable
{

	private ArrayList<Contact>	contacts;

	/**
	 * Default constructor as recommended by Gson.
	 */
	public ContactsList()
	{
		contacts = new ArrayList<Contact>();
	}

	/**
	 * @param gatherContacts
	 *            A List of contacts. It is supposed to be used after the use of
	 *            GatherContactsWithPhoneNumbers
	 */
	public ContactsList(ArrayList<Contact> gatheredContacts)
	{
		contacts = gatheredContacts;
	}

	/**
	 * Transform this contactsList into a NetworkMessage so as to be send over
	 * the network.
	 * 
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		return new NetworkMessage("ContactsList", gson.toJsonTree(contacts));
	}

	/**
	 * @return the contacts
	 */
	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}

	/**
	 * @param contacts
	 *            the contacts to set
	 */
	public void setContacts(ArrayList<Contact> contacts)
	{
		this.contacts = contacts;
	}

}
