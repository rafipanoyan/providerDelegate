package world.rafoufoun.providerdelegate

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.text.TextUtils
import java.util.*

/**
 * This class is the element that link the ContentProvider and all the [ProviderDelegate].
 * Here are dispatched all the ContentProvider calls to match the right [ProviderDelegate] registered.
 */
class ProviderDelegateManager(private val contentResolver: ContentResolver, private val authority: String) {

    /**
     * Collection of delegates
     */
    private val delegates: HashMap<String, ProviderDelegate> = HashMap()

    /**
     * Add a delegate. Throw IllegalArgumentException if this delegate is already registered, regarding the
     * table (see [ProviderDelegate.table])
     *
     * @param delegate the delegate to add.
     * @throws NullPointerException     if delegate is null
     * @throws IllegalArgumentException if delegate is already registered or another delegate handle this Uri
     */
    fun addDelegate(delegate: ProviderDelegate) {
        val table = delegate.table
        if (delegates.containsKey(table)) {
            throw IllegalArgumentException("ProviderDelegate is already registered !")
        }

        delegate.initUriMatcher(authority)
        delegates[table] = delegate
    }

    fun removeDelegate(delegate: ProviderDelegate) {
        val table = delegate.table
        if (!delegates.containsKey(table)) {
            throw IllegalArgumentException("ProviderDelegate is not registered !")
        }

        delegates.remove(table)
    }


    fun getType(uri: Uri): String {

        for (delegate in delegates.values) {
            if (isTableMatcher(delegate.table, uri)) {
                return delegate.getType(uri)
            }
        }

        throw IllegalArgumentException("No ProviderDelegate registered for this Uri ! $uri")
    }

    /**
     * This method must be called in [DelegationProvider.query]
     *
     * @param db            Readable Database
     * @param uri           Uri
     * @param projection    Projection
     * @param selection     Selection
     * @param selectionArgs Selection args
     * @param sortOrder     Sort order
     * @return Cursor
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    fun query(db: SQLiteDatabase, uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {

        for (delegate in delegates.values) {
            if (isTableMatcher(delegate.table, uri)) {
                val cursor = delegate.query(db, uri, projection, selection, selectionArgs, sortOrder)
                cursor.setNotificationUri(contentResolver, uri)
                return cursor
            }
        }
        throw IllegalArgumentException("No ProviderDelegate registered for this Uri ! $uri")
    }

    /**
     * This method must be called in [DelegationProvider.insert]
     *
     * @param db     Writable database
     * @param uri    Uri
     * @param values Values to be inserted
     * @return New item Uri
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    fun insert(db: SQLiteDatabase, uri: Uri, values: ContentValues?): Uri {

        for (delegate in delegates.values) {
            if (isTableMatcher(delegate.table, uri)) {
                val newUri = delegate.insert(db, uri, values)
                if (delegate.mustSendNotification(uri, Operation.INSERT)) {
                    notifyUri(contentResolver, uri)
                }
                return newUri
            }
        }

        throw IllegalArgumentException("No ProviderDelegate registered for this Uri ! $uri")
    }

    /**
     * This method must be called in [DelegationProvider.delete]
     *
     * @param db            Writable database
     * @param uri           Uri
     * @param selection     Selection
     * @param selectionArgs Selection args
     * @return Deleted row count
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    fun delete(db: SQLiteDatabase, uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        for (delegate in delegates.values) {
            if (isTableMatcher(delegate.table, uri)) {
                val rowDeleted = delegate.delete(db, uri, selection, selectionArgs)
                if (delegate.mustSendNotification(uri, Operation.DELETE)) {
                    notifyUri(contentResolver, uri)
                }
                return rowDeleted
            }
        }

        throw IllegalArgumentException("No ProviderDelegate registered for this Uri ! $uri")
    }

    /**
     * This method must be called in [DelegationProvider.update]
     *
     * @param db            Writable database
     * @param uri           Uri
     * @param values        Values to update
     * @param selection     Selection
     * @param selectionArgs Selection args
     * @return Updated row count
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    fun update(db: SQLiteDatabase, uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {

        for (delegate in delegates.values) {
            if (isTableMatcher(delegate.table, uri)) {
                val rowUpdate = delegate.update(db, uri, values, selection, selectionArgs)
                if (delegate.mustSendNotification(uri, Operation.UPDATE)) {
                    notifyUri(contentResolver, uri)
                }
                return rowUpdate
            }
        }

        throw IllegalArgumentException("No ProviderDelegate registered for this Uri ! $uri")
    }

    private fun isTableMatcher(table: String, uri: Uri): Boolean {
        val segments = uri.pathSegments
        return if (segments != null && segments.size > 0) {
            val uriTable = segments[0]
            TextUtils.equals(table, uriTable)
        } else {
            false
        }
    }

    private fun notifyUri(contentResolver: ContentResolver, uri: Uri) {
        contentResolver.notifyChange(uri, null)
    }
}
