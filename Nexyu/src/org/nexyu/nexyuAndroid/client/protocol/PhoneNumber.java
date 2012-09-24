/**
 *
 */
package org.nexyu.nexyuAndroid.client.protocol;

/**
 * Small class to store phone numbers, mainly created to be parsed in JSON in a
 * nice way, a android Pair would have been sufficient instead
 * 
 * @author Paul Ecoffet
 */
public class PhoneNumber
{
	private String	number;
	private int		type;

	
	/**
	 * Default constructor as recommended by Gson
	 */
	public PhoneNumber()
	{
		number = new String();
		type = -1;
	}

	/**
	 * @param number The phone number
	 * @param type The type of the phone number, correspond to Android id for this.
	 */
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
	 * @param number
	 *            the number to set
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
	 * @param type
	 *            the type to set
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
