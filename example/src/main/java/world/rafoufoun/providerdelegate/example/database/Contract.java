package world.rafoufoun.providerdelegate.example.database;


import android.net.Uri;

public class Contract {
	public static final String AUTHORITY = "world.rafoufoun.providerdelegate.authority";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	private static final String FIRST_VENDOR = "vnd.android.cursor.";
	private static final String TYPE_DIR = "dir";
	private static final String TYPE_ITEM = "item";
	private static final String SUB_VENDOR = "/vnd.providerdelegate.";

	public static final String CONTENT_TYPE = FIRST_VENDOR + TYPE_DIR + SUB_VENDOR;

	public static final String CONTENT_TYPE_ITEM = FIRST_VENDOR + TYPE_ITEM + SUB_VENDOR;
}
