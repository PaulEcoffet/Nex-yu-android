package org.nexyu.nexyuAndroid.client.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@SuppressWarnings("unused")
public class CollectionInformation implements NetworkMessageable
{
	private transient int collection_id;
	private int size;

	public CollectionInformation(int collection_id, int size)
	{
		this.collection_id = collection_id;
		this.size = size;
	}

	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement data = gson.toJsonTree(this, getClass());
		return new NetworkMessage("collectionInformation", data, collection_id);
	}

	@Override
	public void setCollectionId(int collection_id)
	{
		this.collection_id = collection_id;
	}

}
