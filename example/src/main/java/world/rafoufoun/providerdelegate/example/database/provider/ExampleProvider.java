package world.rafoufoun.providerdelegate.example.database.provider;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import world.rafoufoun.providerdelegate.DelegationProvider;
import world.rafoufoun.providerdelegate.example.database.Contract;
import world.rafoufoun.providerdelegate.example.database.ExampleDatabase;
import world.rafoufoun.providerdelegate.example.database.delegate.example.ExampleDelegate;

public class ExampleProvider extends DelegationProvider{

	private ExampleDatabase database;
	@Override
	public boolean onCreate() {
		super.onCreate();
		database = new ExampleDatabase(getContext());

		delegateManager.addDelegate(new ExampleDelegate());
		return true;
	}

	@Override
	protected String getAuthority() {
		return Contract.AUTHORITY;
	}

	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = database.getWritableDatabase();
		return delegateManager.query(db, uri, projection, selection, selectionArgs, sortOrder);
	}

	public String getType(Uri uri) {
		return delegateManager.getType(uri);
	}

	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = database.getWritableDatabase();
		return delegateManager.insert(db, uri, values);
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = database.getWritableDatabase();
		return delegateManager.delete(db, uri, selection, selectionArgs);
	}

	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = database.getWritableDatabase();
		return delegateManager.update(db, uri, values, selection, selectionArgs);
	}
}
