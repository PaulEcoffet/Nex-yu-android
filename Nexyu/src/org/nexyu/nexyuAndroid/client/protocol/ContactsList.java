/**
 * 
 */
package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * List of contacts "ready-to-be-sent" because it implements {@link NetworkMessageable}.
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
