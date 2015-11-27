package world.rafoufoun.providerdelegate;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * This abstract class handle all the database logic for a database table. Here are done all the operations
 * related to one model object.
 *
 * CRUD methods throw an {@link UnsupportedOperationException} by default. Override them only if needed.
 */
public abstract class ProviderDelegate {

    protected UriMatcher uriMatcher;

    public ProviderDelegate(String authority){
        this.uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        initUriMatcher(authority);
    }

    /**
     * Initialize the {@link #uriMatcher}. Declare all the handled URIs by this {@link ProviderDelegate} associating it with a URI code (int).
     * These code will be used into you CRUD methods to know which URI is called.
     */
    protected abstract void initUriMatcher(String authority);

    /**
     * Return the table to which this delegate responds. This value MUST be unique across all the delegates
     *
     * @return the handled URI as String
     */
    public abstract String getTable();

    /**
     * {@link android.content.ContentProvider#getType(Uri)}
     * @param uri the URI to query.
     * @return a MIME type string, or null if there is no type.
     */
    public abstract String getType(Uri uri);

    /**
     * {@link android.content.ContentProvider#insert(Uri, ContentValues)}
     * @param db the database object, must be writable
     * @param uri the URI to query.
     * @param values the content values to insert
     * @return the URI for the newly inserted item.
     */
    public Uri insert(SQLiteDatabase db, Uri uri, ContentValues values){
        throw new UnsupportedOperationException("insert not allowed on this uri : " + uri);
    }

    /**
     * {@link android.content.ContentProvider#delete(Uri, String, String[])}
     * @param db the database object, must be writable
     * @param uri the URI to query.
     * @param selection An optional restriction to apply to rows when deleting.
     * @return the number of rows affected.
     */
    public int delete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        throw new UnsupportedOperationException("delete not allowed on this uri : " + uri);
    }

    /**
     * {@link android.content.ContentProvider#update(Uri, ContentValues, String, String[])}
     * @param db the database object, must be writable
     * @param uri the URI to query.
     * @param values A set of column_name/value pairs to update in the database. This must not be null.
     * @param selection An optional filter to match rows to update.
     * @return the number of rows affected.
     */
    public int update(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs){
        throw new UnsupportedOperationException("update not allowed on this uri : " + uri);
    }

    /**
     * {@link android.content.ContentProvider#query(Uri, String[], String, String[], String)}
     * @param db the database object, readable is OK
     * @param uri the URI to query
     * @param projection the list of columns to put into the cursor. If null all columns are included.
     * @param selection a selection criteria to apply when filtering rows. If null then all rows are included.
     * @param selectionArgs you may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @param sortOrder how the rows in the cursor should be sorted. If null then the provider is free to define the sort order.
     * @return a Cursor or null.
     */
    public Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        throw new UnsupportedOperationException("query not allowed on this uri : " + uri);
    }

    /**
     * Helper method to notify a change on a Uri
     *
     * @param uri The Uri to notify
     */
    public void notifyUri(Context context, Uri uri) {
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }
}
