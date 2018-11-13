package world.rafoufoun.providerdelegate

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

/**
 * A ready to go [ContentProvider] implementation having a [ProviderDelegateManager]
 * ready to use. You can call [ProviderDelegateManager.addDelegate]
 * at any time if you have called [.onCreate] into your subclass [DelegationProvider.onCreate]
 */
abstract class DelegationProvider : ContentProvider() {

    private lateinit var delegateManager: ProviderDelegateManager

    /**
     * Get the authority for this provider
     *
     * @return return the provider authority
     */
    protected abstract val authority: String

    protected abstract val writableDatabase: SQLiteDatabase

    protected abstract val readableDatabase: SQLiteDatabase

    /**
     * Initialize the ProviderDelegateManager.
     *
     * @return return true if the initialization is ok.
     */
    override fun onCreate(): Boolean {
        delegateManager = ProviderDelegateManager(context!!.contentResolver, authority)
        initializeProvider()
        addProviderDelegate(delegateManager)
        return true
    }

    /**
     * Method called from [DelegationProvider.onCreate]
     * This stand for initializing anything you need that would normally be in [ContentProvider.onCreate]
     */
    abstract fun initializeProvider()

    /**
     * Thi method is called from [DelegationProvider.onCreate]
     * Add your [ProviderDelegate] here with [ProviderDelegateManager.addDelegate]
     */
    protected abstract fun addProviderDelegate(delegateManager: ProviderDelegateManager)

    override fun getType(uri: Uri): String {
        return delegateManager.getType(uri)
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        return delegateManager.query(readableDatabase, uri, projection, selection, selectionArgs, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        return delegateManager.insert(writableDatabase, uri, values)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return delegateManager.delete(writableDatabase, uri, selection, selectionArgs)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return delegateManager.update(writableDatabase, uri, values, selection, selectionArgs)
    }
}
