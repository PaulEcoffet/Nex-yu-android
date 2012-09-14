/**
 *
 */
package org.nexyu.nexyuAndroid.client.protocol;

import com.google.gson.Gson;

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
		Gson gson = new Gson();
		if (!msg.getType().equals("messageToCell"))
			throw new ClassCastException("the NetworkMessage is not castable into a SMSToCell");
		try
		{
			copy(gson.fromJson(msg.getData(), getClass()));
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException("the NetworkMessage is not castable into a SMSToCell");
		}
	}

	/**
	 * @param fromJson 
	 */
	private void copy(SMSToCell copy)
	{
		//FIXME Clearly not the most beautiful implementation. Got to make something better. 
		this.id = copy.getId();
		this.body = copy.getBody();
		this.recipient = copy.getRecipient();
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
