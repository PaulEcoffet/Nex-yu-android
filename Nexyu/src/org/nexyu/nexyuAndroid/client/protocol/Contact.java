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
import com.google.gson.JsonElement;

/**
 * Object that represents a contact with only the useful information needed by
 * Nex yu.
 * 
 * @author Paul Ecoffet
 */
@SuppressWarnings("unused")
public class Contact implements NetworkMessageable
{

	private String name;
	private ArrayList<PhoneNumber> phoneNumbers;
	private boolean starred;
	private int id;
	private transient int collection_id;

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
	public Contact(int id, String name, String phoneNumber, int phoneType, int starred)
	{
		this.name = name;
		this.phoneNumbers = new ArrayList<PhoneNumber>();
		this.starred = (starred != 0) ? true : false;
		this.id = id;

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
		return new NetworkMessage("contact", data, collection_id);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(name + "\n");
		builder.append(starred);
		builder.append("\n");
		for (PhoneNumber phone : phoneNumbers)
		{
			builder.append("\t" + phone.toString());
		}
		return builder.toString();

	}

	@Override
	public void setCollectionId(int collection_id)
	{
		this.collection_id = collection_id;
	}
}
