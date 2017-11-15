package temp;
import zluo.org.androidschedule.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class MyAdapter {

	private Context context;
	private MainActivity main;
	private Cursor[] cursor=new Cursor[7];
	private SimpleCursorAdapter[] adapter;
	
	private SharedPreferences preferences;
	
	public MyAdapter(Context context){
		this.context=context;
		main=(MainActivity) context;
	}
	public void test(){
	
	
			
	}
	
}
