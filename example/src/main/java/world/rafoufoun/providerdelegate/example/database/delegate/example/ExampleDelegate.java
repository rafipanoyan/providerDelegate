package world.rafoufoun.providerdelegate.example.database.delegate.example;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import world.rafoufoun.providerdelegate.ProviderDelegate;
import world.rafoufoun.providerdelegate.example.database.ConstantProviderDelegate;
import world.rafoufoun.providerdelegate.example.database.table.ExampleTable;

public class ExampleDelegate extends ProviderDelegate {

    private static final String TAG = "ExampleDelegate";

    public ExampleDelegate() {
        super();
    }

    @Override
    public void initUriMatcher(@NonNull String authority) {
        getUriMatcher().addURI(authority, ExampleTable.TABLE_NAME, ConstantProviderDelegate.EXAMPLE);
        getUriMatcher().addURI(authority, ExampleTable.TABLE_NAME + ConstantProviderDelegate.SLASH + ConstantProviderDelegate.STAR, ConstantProviderDelegate.EXAMPLE_ITEM);
    }

    @NonNull
    @Override
    public String getTable() {
        return ExampleTable.TABLE_NAME;
    }

    @NonNull
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = getUriMatcher().match(uri);
        switch (match) {
            case ConstantProviderDelegate.EXAMPLE:
                return ExampleTable.CONTENT_TYPE;
            case ConstantProviderDelegate.EXAMPLE_ITEM:
                return ExampleTable.CONTENT_TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @NonNull
    public Uri insert(@NonNull SQLiteDatabase db, @NonNull Uri uri, ContentValues values) {
        long itemId = db.insert(ExampleTable.TABLE_NAME, null, values);
        return ExampleTable.buildItemUri((int) itemId);
    }

    public int delete(@NonNull SQLiteDatabase db, @NonNull Uri uri, String selection, String[] selectionArgs) {
        final int match = getUriMatcher().match(uri);
        if (TextUtils.isEmpty(selection)) {
            selection = "";
        }
        switch (match) {
            case ConstantProviderDelegate.EXAMPLE_ITEM:
                selection = selection + " " + BaseColumns._ID + "=" + getId(uri);
            case ConstantProviderDelegate.EXAMPLE:
                return db.delete(ExampleTable.TABLE_NAME, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    public int update(@NonNull SQLiteDatabase db, @NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = getUriMatcher().match(uri);
        if (TextUtils.isEmpty(selection)) {
            selection = "";
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

    @NonNull
    public Cursor query(@NonNull SQLiteDatabase db, @NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = getUriMatcher().match(uri);
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
            return Long.parseLong(lastPathSegment);
        }
        return -1;
    }
}
