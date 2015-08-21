package world.rafoufoun.providerdelegate;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;

/**
 * This class is the element that link the ContentProvider and all the {@link ProviderDelegate}.
 * Here are dispatched all the ContentProvider calls to match the right {@link ProviderDelegate} registered.
 */
public class ProviderDelegateManager {

    Context context;

    /**
     * The ContentProvider authority
     */
    String authority;

    /**
     * Collection of delegates
     */
    SparseArrayCompat<ProviderDelegate> delegates;

    /**
     * UriMatcher having all the paths handled by the provider
     */
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public ProviderDelegateManager(Context context, String authority) {
        this.context = context;
        delegates = new SparseArrayCompat<>();
        this.authority = authority;
    }

    /**
     * Add a delegate. Throw IllegalArgumentException if this delegate is already registered, regarding the
     * match Code (see {@link ProviderDelegate#getMatchCode()}) or
     * if this delegate respond to an existing Uri (see {@link ProviderDelegate#getPath()})
     *
     * @param delegate the delegate to add.
     * @throws NullPointerException     if delegate is null
     * @throws IllegalArgumentException if delegate is already registered or another delegate handle this Uri
     */
    public void addDelegate(ProviderDelegate delegate) {
        if (delegate == null) {
            throw new NullPointerException("ProviderDelegate is null, can't add !");
        }

        int matchCode = delegate.getMatchCode();
        if (delegates.get(matchCode) != null) {
            throw new IllegalArgumentException("ProviderDelegate is already registered !");
        }

        String path = delegate.getPath();
        Uri uri = Uri.parse(authority + "/" + path);
        if (sURIMatcher.match(uri) != UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("Uri is already registered into UriMatcher ! " + uri.toString());
        }

        delegates.put(matchCode, delegate);
        sURIMatcher.addURI(authority, path, matchCode);
    }

    public void removeDelegate(ProviderDelegate delegate) {
        if (delegate == null) {
            throw new NullPointerException("ProviderDelegate is null, can't remove !");
        }

        int matchCode = delegate.getMatchCode();
        if (delegates.get(matchCode) == null) {
            throw new IllegalArgumentException("ProviderDelegate is not registered !");
        }

        delegates.remove(matchCode);
    }

    /**
     * This method must be called in {@link DelegationProvider#query(Uri, String[], String, String[], String)}
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
    public Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriCode = sURIMatcher.match(uri);

        int delegateCount = delegates.size();
        for (int i = 0; i < delegateCount; i++) {
            ProviderDelegate delegate = delegates.valueAt(i);
            if (delegate.getMatchCode() == uriCode) {
                Cursor cursor = delegate.query(db, uri, projection, selection, selectionArgs, sortOrder);
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            }
        }

        throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! uriId = " + uriCode);
    }

    /**
     * This method must be called in {@link DelegationProvider#insert(Uri, ContentValues)}
     *
     * @param db     Writable database
     * @param uri    Uri
     * @param values Values to be inserted
     * @return New item Uri
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    public Uri insert(SQLiteDatabase db, Uri uri, ContentValues values) {
        int uriCode = sURIMatcher.match(uri);

        int delegateCount = delegates.size();
        for (int i = 0; i < delegateCount; i++) {
            ProviderDelegate delegate = delegates.valueAt(i);
            if (delegate.getMatchCode() == uriCode) {
                Uri newUri = delegate.insert(db, uri, values);
                notifyUri(uri);
                return newUri;
            }
        }

        throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! uriId = " + uriCode);
    }

    /**
     * This method must be called in {@link DelegationProvider#delete(Uri, String, String[])}
     *
     * @param db            Writable database
     * @param uri           Uri
     * @param selection     Selection
     * @param selectionArgs Selection args
     * @return Deleted row count
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    public int delete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
        int uriCode = sURIMatcher.match(uri);

        int delegateCount = delegates.size();
        for (int i = 0; i < delegateCount; i++) {
            ProviderDelegate delegate = delegates.valueAt(i);
            if (delegate.getMatchCode() == uriCode) {
                int rowDeleted = delegate.delete(db, uri, selection, selectionArgs);
                notifyUri(uri);
                return rowDeleted;
            }
        }

        throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! uriId = " + uriCode);
    }

    /**
     * This method must be called in {@link DelegationProvider#update(Uri, ContentValues, String, String[])}
     *
     * @param db            Writable database
     * @param uri           Uri
     * @param values        Values to update
     * @param selection     Selection
     * @param selectionArgs Selection args
     * @return Updated row count
     * @throws IllegalArgumentException if no delegate is registered for this uri
     */
    public int update(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriCode = sURIMatcher.match(uri);

        int delegateCount = delegates.size();
        for (int i = 0; i < delegateCount; i++) {
            ProviderDelegate delegate = delegates.valueAt(i);
            if (delegate.getMatchCode() == uriCode) {
                return delegate.update(db, uri, values, selection, selectionArgs);
            }
        }

        throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! uriId = " + uriCode);
    }

    /**
     * Helper method to notify a change on a Uri
     *
     * @param uri The Uri to notify
     */
    private void notifyUri(Uri uri) {
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }
}
