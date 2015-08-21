package world.rafoufoun.providerdelegate;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * This interface handle all the database logic for an application element. Here is done all the operations
 * related to one model object.
 */
public interface ProviderDelegate {

    /**
     * Return a unique Id that identify this delegate into the Uri Matcher. This id MUST be unique across all the delegates
     *
     * @return the Uri Id
     */
    int getMatchCode();

    /**
     * Return the path to which this delegate responds. This path MUST be unique across all the delegates
     *
     * @return the Uri
     */
    String getPath();

    String getType(Uri uri);

    Uri insert(SQLiteDatabase db, Uri uri, ContentValues values);

    int delete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs);

    int update(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs);

    Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);
}
