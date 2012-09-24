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

/**
 * An object implementing this interface is able to be converted into a
 * {@link NetworkMessage}, which is required by the Pipeline to be sent over the
 * network.
 * 
 * @author Paul Ecoffet
 */
public interface NetworkMessageable
{
	/**
	 * Convert the object implementing the {@link NetworkMessageable} interface
	 * into a {@link NetworkMessage} so as to be sent over the network.
	 * 
	 * @return The {@link NetworkMessageable} object converted into a
	 *         {@link NetworkMessage}
	 */
	public NetworkMessage toNetworkMessage();
}
