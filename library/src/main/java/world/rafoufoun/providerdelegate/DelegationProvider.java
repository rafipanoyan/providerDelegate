package world.rafoufoun.providerdelegate;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A ready to go {@link ContentProvider} implementation having a {@link ProviderDelegateManager}
 * ready to use. You can call {@link ProviderDelegateManager#addDelegate(ProviderDelegate)}
 * at any time if you have called {@link #onCreate()} into your subclass {@link DelegationProvider#onCreate()}
 */
public abstract class DelegationProvider extends ContentProvider {

    private ProviderDelegateManager delegateManager;

    /**
     * Initialize the ProviderDelegateManager.
     *
     * @return return true if the initialization is ok.
     */
    @Override
    public final boolean onCreate() {
        delegateManager = new ProviderDelegateManager(getContext(), getAuthority());
        initializeProvider();
        addProviderDelegate(delegateManager);
        return true;
    }

    /**
     * Method called from {@link DelegationProvider#onCreate()}
     * This stand for initializing anything you need that would normally be in {@link ContentProvider#onCreate()}
     */
    public abstract void initializeProvider();

    /**
     * Thi method is called from {@link DelegationProvider#onCreate()}
     * Add your {@link ProviderDelegate} here with {@link ProviderDelegateManager#addDelegate(ProviderDelegate)}
     */
    protected abstract void addProviderDelegate(ProviderDelegateManager delegateManager);

    /**
     * Get the authority for this provider
     *
     * @return return the provider authority
     */
    protected abstract String getAuthority();

    protected abstract SQLiteDatabase getWritableDatabase();

    protected abstract SQLiteDatabase getReadableDatabase();

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return delegateManager.getType(uri);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return delegateManager.query(getReadableDatabase(), uri, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return delegateManager.insert(getWritableDatabase(), uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return delegateManager.delete(getWritableDatabase(), uri, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return delegateManager.update(getWritableDatabase(), uri, values, selection, selectionArgs);
    }
}
