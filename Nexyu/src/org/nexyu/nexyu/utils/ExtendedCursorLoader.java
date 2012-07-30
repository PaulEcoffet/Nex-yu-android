/**
 * 
 */
package org.nexyu.nexyu.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * @author Paul Ecoffet
 *
 */
public class ExtendedCursorLoader extends CursorLoader
{
	string

	/**
	 * @param context
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 */
	public ExtendedCursorLoader(Context context, Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public ExtendedCursorLoader(Context context)
	{
		super(context);
	}

    @Override
    public Cursor loadInBackground() {
    	
		Cursor cursor = getContext().getContentResolver().query(getUri(), getProjection(), getSelection(),
                getSelectionArgs(), getSortOrder());
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            registerContentObserver(cursor, mObserver);
        }
        return cursor;
    }

}
