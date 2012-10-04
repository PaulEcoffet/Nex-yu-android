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
public class VerifCode implements NetworkMessageable
{
	private String verifCode;

	/**
	 * @param verifCode
	 */
	public VerifCode(String verifCode)
	{
		this.verifCode = verifCode;
	}

	/**
	 * @see org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable#toNetworkMessage()
	 */
	@Override
	public NetworkMessage toNetworkMessage()
	{
		Gson gson = new Gson();
		JsonElement elem = gson.toJsonTree(verifCode);
		return new NetworkMessage("verifCode", elem);
	}

}