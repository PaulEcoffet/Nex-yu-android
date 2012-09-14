/**
 *
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

	public SMSToCell(NetworkMessage msg) throws ClassCastException
	{
		if (!msg.getType().equals("messageToCell"))
			throw new ClassCastException("the NetworkMessage is not castable into a SMSToCell");
		try
		{
			JsonObject data = msg.getData().getAsJsonObject();
			recipient = data.get("recipient").getAsString();
			body = data.get("body").getAsString();
			id = data.get("id").getAsInt();
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException("the NetworkMessage is not castable into a SMSToCell");
		}
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
