/**
 *
 */
package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import android.telephony.SmsMessage;

import com.google.gson.JsonObject;

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
		sender = sms.getOriginatingAddress();
		timestamp = sms.getTimestampMillis();
		body = bodyGen.toString();
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		JsonObject data = new JsonObject();

		data.addProperty("sender", sender);
		data.addProperty("timestamp", timestamp);
		data.addProperty("body", body);
		return new NetworkMessage("message", data);
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#fromNetworkMessage(org.nexyu.nexyuAndroid.client.protocol.NetworkMessage)
	 */
	@Override
	public void fromNetworkMessage(NetworkMessage msg)
	{
		JsonObject data = msg.getData().getAsJsonObject();
		sender = data.get("sender").getAsString();
		timestamp = data.get("timestamp").getAsLong();
		body = data.get("body").getAsString();
	}
}
