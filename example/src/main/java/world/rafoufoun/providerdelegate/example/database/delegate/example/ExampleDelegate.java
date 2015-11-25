package world.rafoufoun.providerdelegate.example.database.delegate.example;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import world.rafoufoun.providerdelegate.ProviderDelegate;
import world.rafoufoun.providerdelegate.example.database.ConstantProviderDelegate;
import world.rafoufoun.providerdelegate.example.database.table.ExampleTable;

public class ExampleDelegate extends ProviderDelegate {

	private static final String TAG= ExampleDelegate.class.getSimpleName();

	public ExampleDelegate(String authority) {
		super(authority);
	}

	@Override
	protected void initUriMatcher(String authority) {
		uriMatcher.addURI(authority, ExampleTable.TABLE_NAME, ConstantProviderDelegate.EXAMPLE);
		uriMatcher.addURI(authority, ExampleTable.TABLE_NAME + ConstantProviderDelegate.SLASH + ConstantProviderDelegate.STAR, ConstantProviderDelegate.EXAMPLE_ITEM);
	}

	@Override
	public String getTable() {
		return ExampleTable.TABLE_NAME;
	}

	@Override
	public String getType(Uri uri) {
		final int match = uriMatcher.match(uri);
		switch (match) {
			case ConstantProviderDelegate.EXAMPLE:
				return ExampleTable.CONTENT_TYPE;
			case ConstantProviderDelegate.EXAMPLE_ITEM:
				return ExampleTable.CONTENT_TYPE_ITEM;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	public Uri insert(SQLiteDatabase db, Uri uri, ContentValues values) {
		long itemId = db.insert(ExampleTable.TABLE_NAME, null, values);
		return ExampleTable.buildItemUri((int)itemId);
	}

	public int delete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
		final int match = uriMatcher.match(uri);
		if(TextUtils.isEmpty(selection)){
			selection="";
		}
		switch (match) {
			case ConstantProviderDelegate.EXAMPLE_ITEM:
				selection=  selection+ " "+BaseColumns._ID+"="+getId(uri);
			case ConstantProviderDelegate.EXAMPLE:
				return db.delete(ExampleTable.TABLE_NAME, selection, selectionArgs);
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	public int update(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final int match = uriMatcher.match(uri);
		if(TextUtils.isEmpty(selection)){
			selection="";
		}
		switch (match) {
			case ConstantProviderDelegate.EXAMPLE_ITEM:
				selection = selection + " " + BaseColumns._ID + "=" + getId(uri);
			case ConstantProviderDelegate.EXAMPLE:
				return db.update(ExampleTable.TABLE_NAME, values, selection, selectionArgs);
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	public Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final int match = uriMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		switch (match) {
			case ConstantProviderDelegate.EXAMPLE_ITEM:
				queryBuilder.appendWhere(BaseColumns._ID + "=" + getId(uri));
			case ConstantProviderDelegate.EXAMPLE:
				queryBuilder.setTables(ExampleTable.TABLE_NAME);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		return queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
	}

	private long getId(Uri uri) {
		String lastPathSegment = uri.getLastPathSegment();
		if (lastPathSegment != null) {
			try {
				return Long.parseLong(lastPathSegment);
			} catch (NumberFormatException e) {
				Log.e(TAG, "Number Format Exception : " + e);
			}
		}
		return -1;
	}
}
