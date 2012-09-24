/**
 *
 */
package org.nexyu.nexyuAndroid.utils;

import java.util.ArrayList;

import android.util.SparseArray;

/**
 * @author Paul Ecoffet
 *
 */
public class ArrayUtils
{

	/**
	 * Convert a sparseArray into an ArrayList
	 * @param sparseArray The SparseArray to convert
	 * @return A ArrayList containing the elements of the SparseArray.
	 */
	public static <T> ArrayList<T> SparseArrayToArrayList(SparseArray<T> sparseArray)
	{
		ArrayList<T> result = new ArrayList<T>();
		int size = sparseArray.size();
		for(int i = 0; i < size; i++)
		{
			result.add(sparseArray.valueAt(i));
		}
		return result;
	}

}
