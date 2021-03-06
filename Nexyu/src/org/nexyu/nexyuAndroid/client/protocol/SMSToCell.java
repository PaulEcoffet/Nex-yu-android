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

import com.google.gson.JsonObject;

/**
 * @author Paul Ecoffet
 * 
 */
public class SMSToCell
{
	private String	recipient;
	private String	body;
	private int		id;

	public SMSToCell() // For Gson Purpose
	{
		recipient = new String();
		body = new String();
		id = 0;
	}

	public SMSToCell(NetworkMessage msg)
	{
		if (!msg.getType().equals("messageToCell"))
			throw new ClassCastException("the NetworkMessage is not castable into a SMSToCell");

		JsonObject elem = msg.getData().getAsJsonObject();
		this.id = elem.get("id").getAsInt();
		this.body = elem.get("body").getAsString();
		this.recipient = elem.get("recipient").getAsString();
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient()
	{
		return recipient;
	}

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	/**
	 * @return the body
	 */
	public String getBody()
	{
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body)
	{
		this.body = body;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
}
