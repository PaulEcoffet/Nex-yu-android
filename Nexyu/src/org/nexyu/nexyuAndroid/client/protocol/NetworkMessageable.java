/**
 *
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
