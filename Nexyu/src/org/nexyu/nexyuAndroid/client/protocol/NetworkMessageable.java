/**
 * 
 */
package org.nexyu.nexyuAndroid.client.protocol;

/**
 * @author Paul Ecoffet
 *
 */
public interface NetworkMessageable
{
	public NetworkMessage toNetworkMessage();
	public void	fromNetworkMessage(NetworkMessage msg);
}
