/**
 *
 */
package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import android.telephony.SmsMessage;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * SMS messages to send formatted so as to be sent over the network.
 * 
 * @author Paul Ecoffet
 * 
 */
public class SMSToSendNetworkMessage extends NetworkMessage
{
	/**
	 * @param messages
	 *            The list of received messages.
	 */
	public SMSToSendNetworkMessage(ArrayList<SmsMessage> messages)
	{
		super("messages", null);
		JsonArray data = new JsonArray();

		for (SmsMessage message : messages)
		{
			JsonObject jsonMess = new JsonObject();
			jsonMess.addProperty("body", message.getMessageBody());
			jsonMess.addProperty("sender", message.getOriginatingAddress());
			jsonMess.addProperty("timestamp", message.getTimestampMillis());
			data.add(jsonMess);
		}
		super.setData(data);
	}
}
