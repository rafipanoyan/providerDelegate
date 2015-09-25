package world.rafoufoun.providerdelegate.example.database.table;

import android.net.Uri;
import android.provider.BaseColumns;

import world.rafoufoun.providerdelegate.example.database.Contract;

interface ExampleColumns {
	String TITLE = "title";
}

public class ExampleTable implements ExampleColumns, BaseColumns {
	public static final String TABLE_NAME = "Example";
	private static final Uri CONTENT_URI = Contract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
	public static final String CONTENT_TYPE = Contract.CONTENT_TYPE + TABLE_NAME;
	public static final String CONTENT_TYPE_ITEM = Contract.CONTENT_TYPE_ITEM + TABLE_NAME;

	public static Uri buildUri() {
		return CONTENT_URI;
	}

	public static Uri buildItemUri(int id) {
		return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
	}
}
