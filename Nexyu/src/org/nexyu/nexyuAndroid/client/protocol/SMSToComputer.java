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

import android.telephony.SmsMessage;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * SMS messages to send formatted so as to be sent over the network.
 * 
 * @author Paul Ecoffet
 * 
 */
public class SMSToComputer implements NetworkMessageable
{
	private String	sender;
	private long	timestamp;
	private String	body;

	public SMSToComputer() // For Gson purpose.
	{
		sender = new String();
		body = new String();
		timestamp = 0;
	}

	/**
	 * @param messages
	 *            The list of received messages.
	 */
	public SMSToComputer(ArrayList<SmsMessage> messages)
	{
		SmsMessage sms = messages.get(0);
		StringBuilder bodyGen = new StringBuilder();
		for (SmsMessage message : messages)
		{
			bodyGen.append(message.getMessageBody());
		}
		setSender(sms.getOriginatingAddress());
		setTimestamp(sms.getTimestampMillis());
		setBody(bodyGen.toString());
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement data = gson.toJsonTree(this, getClass());
		return new NetworkMessage("message", data);
	}

	/**
	 * @return the sender
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender)
	{
		this.sender = sender;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
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

	@Override
	public void setCollectionId(int collection_id)
	{
		// TODO Auto-generated method stub
		
	}
}
