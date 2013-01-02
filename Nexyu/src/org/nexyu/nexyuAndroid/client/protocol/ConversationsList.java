package org.nexyu.nexyuAndroid.client.protocol;

import java.util.ArrayList;

import com.google.gson.Gson;

public class ConversationsList implements NetworkMessageable {


	private ArrayList<Conversation>	conversations;
	
	/**
	 * Default constructor as recommended by Gson.
	 */
	public ConversationsList()
	{
		conversations = new ArrayList<Conversation>();
	}

	/**
	 * @param gatherContacts
	 *            A List of conversations. It is supposed to be used after the use of
	 *            GatherConversations
	 */
	public ConversationsList(ArrayList<Conversation> gatheredConversations)
	{
		conversations = gatheredConversations;
	}

	/**
	 * Transform this conversationsList into a NetworkMessage so as to be send over
	 * the network.
	 * 
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		return new NetworkMessage("ConversationsList", gson.toJsonTree(conversations));
	}

	/**
	 * @return the conversations
	 */
	public ArrayList<Conversation> getContacts()
	{
		return conversations;
	}

	/**
	 * @param conversations
	 *            the conversations to set
	 */
	public void setContacts(ArrayList<Conversation> conversations)
	{
		this.conversations = conversations;
	}

}

