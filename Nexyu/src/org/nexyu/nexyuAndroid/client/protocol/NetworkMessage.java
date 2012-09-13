/**
 *
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
public final class NetworkMessage
{
	private String		type;
	private JsonElement	data;

	/**
	 * Default constructor as recommended by Gson.
	 */
	public NetworkMessage()
	{
		type = "unknown";
		data = null;
	}

	public NetworkMessage(String what, JsonElement _data)
	{
		type = what;
		this.data = _data;
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
}
