package world.rafoufoun.providerdelegate.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import world.rafoufoun.providerdelegate.example.database.table.ExampleTable;


public class ExampleDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "example";
	private static final int DATABASE_VERSION = 1;

	public ExampleDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ExampleTable.TABLE_NAME + " ( '" + ExampleTable._ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '" + ExampleTable.TITLE + "' TEXT NOT NULL );");
		db.execSQL("INSERT INTO " + ExampleTable.TABLE_NAME + " ( '" + ExampleTable.TITLE + "' ) VALUES ('Hello World');");
		db.execSQL("INSERT INTO " + ExampleTable.TABLE_NAME + " ( '" + ExampleTable.TITLE + "' ) VALUES ('Bonjour');");
		db.execSQL("INSERT INTO " + ExampleTable.TABLE_NAME + " ( '" + ExampleTable.TITLE + "' ) VALUES ('Hallo');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
