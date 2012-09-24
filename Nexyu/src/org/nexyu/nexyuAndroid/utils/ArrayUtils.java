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
	 * 
	 * @param sparseArray
	 *            The SparseArray to convert
	 * @return A ArrayList containing the elements of the SparseArray.
	 */
	public static <T> ArrayList<T> SparseArrayToArrayList(SparseArray<T> sparseArray)
	{
		ArrayList<T> result = new ArrayList<T>();
		int size = sparseArray.size();
		for (int i = 0; i < size; i++)
		{
			result.add(sparseArray.valueAt(i));
		}
		return result;
	}

}
