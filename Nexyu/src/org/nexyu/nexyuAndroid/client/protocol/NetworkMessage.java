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
public abstract class NetworkMessage
{
	protected String type;
	protected JsonElement data;
	
	protected NetworkMessage(String what, Object data)
	{
		Gson gson = new Gson();
		type = what;
		this.data = gson.toJsonTree(data);
	}
	
	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
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
	 * @param data the data to set
	 */
	public void setData(JsonElement data)
	{
		this.data = data;
	}
}
