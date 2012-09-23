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
public class Contact implements NetworkMessageable
{

	private String					name;
	private ArrayList<PhoneNumber>	phoneNumbers;
	private boolean					starred;

	public Contact() // For Gson purpose
	{
		name = new String();
		phoneNumbers = new ArrayList<PhoneNumber>();
		starred = false;
	}

	/**
	 * @param name
	 * @param phoneNumbers
	 * @param starred
	 */
	public Contact(String name, String phoneNumber, int phoneType, int starred)
	{
		this.name = name;
		this.phoneNumbers = new ArrayList<PhoneNumber>();
		this.starred = (starred != 0) ? true : false;

		this.addPhone(phoneNumber, phoneType);
	}

	/**
	 * @param phoneNumber
	 * @param phoneType
	 */
	public void addPhone(String phoneNumber, int phoneType)
	{
		PhoneNumber phone = new PhoneNumber(phoneNumber, phoneType);
		phoneNumbers.add(phone);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the starred
	 */
	public boolean isStarred()
	{
		return starred;
	}

	/**
	 * @param starred
	 *            the starred to set
	 */
	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement data = gson.toJsonTree(this, getClass());
		return new NetworkMessage("contact", data);
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(name + "\n");
		builder.append(starred);
		builder.append("\n");
		for(PhoneNumber phone : phoneNumbers)
		{
			builder.append("\t" +phone.toString());
		}
		return builder.toString();
		
	}
}
