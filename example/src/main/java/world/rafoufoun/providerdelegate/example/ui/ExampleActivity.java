package world.rafoufoun.providerdelegate.example.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import world.rafoufoun.providerdelegate.example.R;
import world.rafoufoun.providerdelegate.example.database.table.ExampleTable;

public class ExampleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		TextView titleItem =(TextView) findViewById(R.id.textView);
		TextView countItem =(TextView) findViewById(R.id.textView2);

		Cursor cursorTitle = this.getContentResolver().query(ExampleTable.buildItemUri(1),new String[]{ExampleTable.TITLE},null,null,null);

		if(cursorTitle!=null) {
			if (cursorTitle.moveToFirst()){
				titleItem.setText(String.format(getString(R.string.title_item), cursorTitle.getString(0)));
			}else{
				titleItem.setText(getString(R.string.error));
			}
			cursorTitle.close();
		}

		Cursor cursorCount = this.getContentResolver().query(ExampleTable.buildUri(),new String[]{"count('"+ExampleTable.TABLE_NAME+"') AS count"},null,null,null);

		if(cursorCount!=null) {
			if (cursorCount.moveToFirst()) {
				countItem.setText(String.format(getString(R.string.num_item), cursorCount.getInt(0)));
			} else {
				countItem.setText(getString(R.string.error));
			}
			cursorCount.close();
		}
	}
}
