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
	 * @param number
	 *            The phone number
	 * @param type
	 *            The type of the phone number, correspond to Android id for
	 *            this.
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
