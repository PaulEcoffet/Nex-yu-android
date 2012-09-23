/**
 * 
 */
package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author Paul Ecoffet
 * 
 */
public class ContactsList implements NetworkMessageable
{

	private ArrayList<Contact>	contacts;

	public ContactsList()
	{
		contacts = new ArrayList<Contact>();
	}

	/**
	 * @param gatherContacts
	 */
	public ContactsList(ArrayList<Contact> gatheredContacts)
	{
		contacts = gatheredContacts;
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement data = gson.toJsonTree(contacts);
		return new NetworkMessage("ContactsList", data);
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
