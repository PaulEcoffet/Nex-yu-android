/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
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

	public static final int		SUCCESS	= 0;
	private static final int	FAILURE	= 1;
	private int					id;
	private int					result;

	public SMSSent() // For Gson purpose.
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
	 * @param id
	 *            the id to set
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
	 * @param result
	 *            the result to set
	 */
	public void setResult(int result)
	{
		this.result = result;
	}

}
