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

import com.google.gson.JsonElement;

/**
 * Class that represents a message (not a SMS) received or to send over the
 * network.
 * 
 * Note: Should be abstract but gson doesn't support it.
 * 
 * @author Paul Ecoffet
 * 
 */
public final class NetworkMessage implements NetworkMessageable
{
	private String		type;
	private JsonElement	data;
	private int collection_id;

	/**
	 * Default constructor as recommended by Gson.
	 */
	public NetworkMessage()
	{
		type = "unknown";
		data = null;
		collection_id = 0;
	}

	public NetworkMessage(String what, JsonElement data)
	{
		type = what;
		this.data = data;
	}

	public NetworkMessage(String what, JsonElement data, int collection_id)
	{
		type = what;
		this.data = data;
		this.collection_id = collection_id;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public JsonElement getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(JsonElement data)
	{
		this.data = data;
	}

	/**
	 * @return the collection_id
	 */
	public int getCollectionId()
	{
		return collection_id;
	}

	/**
	 * @param collection_id the collection_id to set
	 */
	@Override
	public void setCollectionId(int collection_id)
	{
		this.collection_id = collection_id;
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		return this;
	}
}
