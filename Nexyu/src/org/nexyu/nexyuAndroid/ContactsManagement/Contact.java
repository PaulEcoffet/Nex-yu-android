/**
 * 
 */
package org.nexyu.nexyuAndroid.ContactsManagement;

import java.util.ArrayList;

import android.util.Pair;

/**
 * @author Paul Ecoffet
 * 
 */
public class Contact
{
	/**
	 * @param name
	 * @param phoneNumbers
	 * @param starred
	 */
	public Contact(String name, String phoneNumber, int phoneType, int starred)
	{
		this.name = name;
		this.phoneNumbers = new ArrayList<Pair<String, Integer>>();
		this.starred = (starred != 0) ? true : false;
		
		this.addPhone(phoneNumber, phoneType);
	}

	private String								name;
	private ArrayList<Pair<String, Integer>>	phoneNumbers;
	private boolean								starred;
	/**
	 * @param phoneNumber
	 * @param phoneType
	 */
	public void addPhone(String phoneNumber, int phoneType)
	{
		Pair<String, Integer> phone = new Pair<String, Integer>(phoneNumber, phoneType);
		phoneNumbers.add(phone);
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the starred
	 */
	public boolean isStarred()
	{
		return starred;
	}
	/**
	 * @param starred the starred to set
	 */
	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}
}
