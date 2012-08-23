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
	 * @return the what
	 */
	public String getWhat()
	{
		return type;
	}
	/**
	 * @param what the what to set
	 */
	public void setWhat(String what)
	{
		this.type = what;
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
