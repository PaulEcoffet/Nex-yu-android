/**
 * 
 */
package org.nexyu.nexyuAndroid.client.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author Paul Ecoffet
 *
 */
public class SMSSent implements NetworkMessageable
{

	public static final int	SUCCESS	= 0;
	private static final int	FAILURE	= 1;
	private int	id;
	private int	result;

	public SMSSent() //For Gson purpose.
	{
		id = 0;
		result = FAILURE;
	}
	/**
	 * @param id
	 * @param success2
	 */
	public SMSSent(int id, int result)
	{
		setId(id);
		setResult(result);
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement data = gson.toJsonTree(this, getClass());
		return new NetworkMessage("SMSSent", data);
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return the result
	 */
	public int getResult()
	{
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result)
	{
		this.result = result;
	}

}
