/**
 * 
 */
package org.nexyu.nexyuAndroid.client.protocol;

import com.google.gson.Gson;

/**
 * @author Paul Ecoffet
 * 
 */
@SuppressWarnings("unused")
public class Conversation implements NetworkMessageable
{
	private int thread_id;
	private String snippet;
	private String address;
	private int date;
	private int type;
	private transient int collection_id;

	public Conversation(int thread_id, String address, String snippet,
			int date, int type)
	{
		this.thread_id = thread_id;
		this.address = address;
		this.snippet = snippet;
		this.date = date;
		this.type = type;
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		return new NetworkMessage("conversation", gson.toJsonTree(this), collection_id);
	}

	@Override
	public void setCollectionId(int collection_id)
	{
		this.collection_id = collection_id;
	}

}
