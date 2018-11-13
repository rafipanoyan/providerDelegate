package world.rafoufoun.providerdelegate

import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

/**
 * This abstract class handle all the database logic for a database table. Here are done all the operations
 * related to one model object.
 *
 *
 * CRUD methods throw an [UnsupportedOperationException] by default. Override them only if needed.
 */
abstract class ProviderDelegate {

    protected val uriMatcher: UriMatcher by lazy { UriMatcher(UriMatcher.NO_MATCH) }

    /**
     * Return the table to which this delegate responds. This value MUST be unique across all the delegates
     *
     * @return the handled URI as String
     */
    abstract val table: String

    /**
     * Initialize the [uriMatcher]. Declare all the handled URIs by this [ProviderDelegate] associating it with a URI code (int).
     * These code will be used into you CRUD methods to know which URI is called.
     */
    abstract fun initUriMatcher(authority: String)

    /**
     * [android.content.ContentProvider.getType]
     *
     * @param uri the URI to query.
     * @return a MIME type string, or null if there is no type.
     */
    abstract fun getType(uri: Uri): String

    /**
     * [android.content.ContentProvider.insert]
     *
     * @param db     the database object, must be writable
     * @param uri    the URI to query.
     * @param values the content values to insert
     * @return the URI for the newly inserted item.
     */
    open fun insert(db: SQLiteDatabase, uri: Uri, values: ContentValues?): Uri {
        throw UnsupportedOperationException("insert not allowed on this uri : $uri")
    }

    /**
     * [android.content.ContentProvider.delete]
     *
     * @param db        the database object, must be writable
     * @param uri       the URI to query.
     * @param selection An optional restriction to apply to rows when deleting.
     * @return the number of rows affected.
     */
    open fun delete(db: SQLiteDatabase, uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("delete not allowed on this uri : $uri")
    }

    /**
     * [android.content.ContentProvider.update]
     *
     * @param db        the database object, must be writable
     * @param uri       the URI to query.
     * @param values    A set of column_name/value pairs to update in the database. This must not be null.
     * @param selection An optional filter to match rows to update.
     * @return the number of rows affected.
     */
    open fun update(db: SQLiteDatabase, uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("update not allowed on this uri : $uri")
    }

    /**
     * [android.content.ContentProvider.query]
     *
     * @param db            the database object, readable is OK
     * @param uri           the URI to query
     * @param projection    the list of columns to put into the cursor. If null all columns are included.
     * @param selection     a selection criteria to apply when filtering rows. If null then all rows are included.
     * @param selectionArgs you may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @param sortOrder     how the rows in the cursor should be sorted. If null then the provider is free to define the sort order.
     * @return a Cursor or null.
     */
    open fun query(db: SQLiteDatabase, uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        throw UnsupportedOperationException("query not allowed on this uri : $uri")
    }

    /**
     * check if a notification must be sent through the ContentResolver. See [android.content.ContentResolver.notifyChange]
     *
     * @param uri       the URI on which the operation has been done
     * @param operation the operation done on the URI
     * @return true if must notify, false otherwise. Return true by default
     */
    open fun mustSendNotification(uri: Uri, operation: Operation): Boolean = true
}
