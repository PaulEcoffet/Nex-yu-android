/**
 *
 */
package org.nexyu.nexyuAndroid.client.protocol;

/**
 * @author Paul Ecoffet
 *
 */
public class PhoneNumber
{
	private String number;
	private int type;

	/**
	 * @param number
	 * @param type
	 */
	
	public PhoneNumber()
	{
		number = new String();
		type = -1;
	}
	
	public PhoneNumber(String number, int type)
	{
		this.number = number;
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber()
	{
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number)
	{
		this.number = number;
	}

	/**
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return number + " " + type + "\n";
	}
}
