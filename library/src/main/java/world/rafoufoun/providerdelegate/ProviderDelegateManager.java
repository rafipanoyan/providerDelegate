package world.rafoufoun.providerdelegate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

/**
 * This class is the element that link the ContentProvider and all the {@link ProviderDelegate}.
 * Here are dispatched all the ContentProvider calls to match the right {@link ProviderDelegate} registered.
 */
public class ProviderDelegateManager {

	protected final Context context;

	/**
	 * The ContentProvider's authority
	 */
	protected final String authority;

	/**
	 * Collection of delegates
	 */
	protected final HashMap<String, ProviderDelegate> delegates;

	public ProviderDelegateManager(Context context, String authority) {
		this.context = context;
		delegates = new HashMap<>();
		this.authority = authority;
	}

	/**
	 * Add a delegate. Throw IllegalArgumentException if this delegate is already registered, regarding the
	 * table (see {@link ProviderDelegate#getTable()})
	 *
	 * @param delegate the delegate to add.
	 * @throws NullPointerException     if delegate is null
	 * @throws IllegalArgumentException if delegate is already registered or another delegate handle this Uri
	 */
	public void addDelegate(ProviderDelegate delegate) {
		if (delegate == null) {
			throw new NullPointerException("ProviderDelegate is null, can't add !");
		}

		String table = delegate.getTable();
		if (delegates.get(table) != null) {
			throw new IllegalArgumentException("ProviderDelegate is already registered !");
		}

		delegates.put(table, delegate);
	}

	public void removeDelegate(ProviderDelegate delegate) {
		if (delegate == null) {
			throw new NullPointerException("ProviderDelegate is null, can't remove !");
		}

		String table = delegate.getTable();
		if (delegates.get(table) == null) {
			throw new IllegalArgumentException("ProviderDelegate is not registered !");
		}

		delegates.remove(table);
	}


	public String getType(Uri uri) {

		for (ProviderDelegate delegate : delegates.values()) {
			if (isTableMatcher(delegate.getTable(), uri)) {
				return delegate.getType(uri);
			}
		}

		throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! " + uri);
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

		for (ProviderDelegate delegate : delegates.values()) {
			if (isTableMatcher(delegate.getTable(), uri)) {
				Cursor cursor = delegate.query(db, uri, projection, selection, selectionArgs, sortOrder);
				cursor.setNotificationUri(context.getContentResolver(), uri);
				return cursor;
			}
		}
		throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! " + uri);
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

		for (ProviderDelegate delegate : delegates.values()) {
			if (isTableMatcher(delegate.getTable(), uri)) {
				Uri newUri = delegate.insert(db, uri, values);
				delegate.notifyUri(context, uri);
				return newUri;
			}
		}

		throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! " + uri);
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

		for (ProviderDelegate delegate : delegates.values()) {
			if (isTableMatcher(delegate.getTable(), uri)) {
				int rowDeleted = delegate.delete(db, uri, selection, selectionArgs);
				delegate.notifyUri(context, uri);
				return rowDeleted;
			}
		}

		throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! " + uri);
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

		for (ProviderDelegate delegate : delegates.values()) {
			if (isTableMatcher(delegate.getTable(), uri)) {
				int rowUpdate = delegate.update(db, uri, values, selection, selectionArgs);
				delegate.notifyUri(context,uri);
				return rowUpdate;
			}
		}

		throw new IllegalArgumentException("No ProviderDelegate registered for this Uri ! " + uri);
	}

	private boolean isTableMatcher(String table, Uri uri) {
		List<String> segments = uri.getPathSegments();
		if (segments != null && segments.size() > 0) {
			String uriTable = segments.get(0);
			return TextUtils.equals(table, uriTable);
		} else {
			return false;
		}
	}
}
