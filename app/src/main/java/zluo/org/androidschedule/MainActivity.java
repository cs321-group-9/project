package zluo.org.androidschedule;

import temp.DataBase;
import temp.MyApplication;
import temp.MyDialog;
import temp.ShareMethod;
import zluo.org.editschedule.SetActivity;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends Activity {
	 
	public ListView list[] = new ListView[7];
	private TextView exitButton = null;
	private TextView setButton = null;
	public SimpleCursorAdapter adapter;
	private TabHost tabs   = null;
	public Cursor[] cursor=new Cursor[7];
	private SharedPreferences pre;
	public static DataBase db;

	//flip detector
	private GestureDetector detector = null;
	//min flip distance
	private final int FLIP_DISTANCE = 200;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//add the activity to Myapplication
		MyApplication.getInstance().addActivity(this);
		
		db=new DataBase(MainActivity.this);
		pre=getSharedPreferences("firstStart",Context.MODE_PRIVATE);

		//if the program is running for the first time, then create the database to store the data
		if(pre.getBoolean("firstStart", true)){
			SingleInstance.createTable();
			(pre.edit()).putBoolean("firstStart",false).commit();
//			finish();			
		}


		exitButton = (TextView)findViewById(R.id.exitButton);
		setButton = (TextView)findViewById(R.id.setButton);
		list[0] = (ListView)findViewById(R.id.list0);
		list[1] = (ListView)findViewById(R.id.list1);
		list[2] = (ListView)findViewById(R.id.list2);
		list[3] = (ListView)findViewById(R.id.list3);
		list[4] = (ListView)findViewById(R.id.list4);
		list[5] = (ListView)findViewById(R.id.list5);
		list[6] = (ListView)findViewById(R.id.list6);
		tabs  = (TabHost)findViewById(R.id.tabhost);

	    detector = new GestureDetector(this, new DetectorGestureListener());
   

		tabs.setup();

		TabHost.TabSpec  spec = null;
		addCard(spec,"ta1",R.id.list0,"Su");
		addCard(spec,"ta2",R.id.list1,"Mo");
		addCard(spec,"ta3",R.id.list2,"Tu");
		addCard(spec,"ta4",R.id.list3,"We");
		addCard(spec,"ta5",R.id.list4,"Th");
		addCard(spec,"ta6",R.id.list5,"Fr");
		addCard(spec,"ta7",R.id.list6,"Sa");
		

		TabWidget tabWidget = tabs.getTabWidget();
		for(int i=0;i<tabWidget.getChildCount();i++){
			TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(0xff004499);				
		}
		

		tabs.setCurrentTab(ShareMethod.getWeekDay());
		

		for(int i=0;i<7;i++){
			cursor[i]=MainActivity.db.select(i);		
			list[i].setAdapter(adapter(i));
		}
		

		final AudioManager audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);

		 final int orgRingerMode = audioManager.getRingerMode(); 
		 

		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				exit(builder);
			}
		}); 
		

		setButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SetActivity.class);

				intent.putExtra("mode_ringer", orgRingerMode);
				startActivity(intent);
			}
		});
		
		for( int day=0;day<7;day++){

			list[day].setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event)   {
					return detector.onTouchEvent(event);
				}
			});

			list[day].setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int id, long arg3) {
					final int currentDay=tabs.getCurrentTab();
					final int n=id;
				    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				    builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("Choice");
					TextView tv=(TextView)arg1.findViewById(R.id.ltext0);
					Log.i("Test",(tv.getText().toString().equals(""))+"");

					if((tv.getText()).toString().equals("")){

						builder.setItems(R.array.edit_options1, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {					

								if(which == 0){					
									new MyDialog(MainActivity.this).add(currentDay,n);
								}
							}
						});
						builder.create().show();
					  }
					else{
						builder.setItems(R.array.edit_options2, new DialogInterface.OnClickListener() {
							
							@SuppressWarnings("deprecation")
							@Override
							public void onClick(DialogInterface dialog, int which) {

								if(which == 0){					
									new MyDialog(MainActivity.this).modify(currentDay,n);
								}
								if(which == 1){
									cursor[currentDay].moveToPosition(n);
									int n1=Integer.parseInt(cursor[currentDay].getString(7));//�γ̵��ܽ���
									int n2=Integer.parseInt(cursor[currentDay].getString(8));//ѡ�е�Ϊ�ÿγ̵ĵڼ���
									switch(n2){
										case 0:
											for(int m=0;m<n1;m++){
												MainActivity.db.deleteData(currentDay,n+m+1);
												}
											break;
	
										case 1:
											MainActivity.db.deleteData(currentDay,n);
											for(int m=1;m<n1;m++){
												MainActivity.db.deleteData(currentDay,n+m);
												}
											break;		
										case 2:
											MainActivity.db.deleteData(currentDay,n-1);
											MainActivity.db.deleteData(currentDay,n);
											for(int m=2;m<n1;m++){
												MainActivity.db.deleteData(currentDay,n+m-1);
												}
												break;
										case 3:
											for(int m=n2;m>=0;m--){
												MainActivity.db.deleteData(currentDay,n-m+1);
												}
												break;
										default:
											break;
									}
									cursor[currentDay].requery();
									list[currentDay].invalidate();
								}
							}
						});
						builder.create().show();
					}
				}
			});
		}
		
	}

	class DetectorGestureListener implements GestureDetector.OnGestureListener{

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}


		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int i = tabs.getCurrentTab();

				if(e1.getX() - e2.getX() > FLIP_DISTANCE){
					if(i<6)
						tabs.setCurrentTab(i+1);
				//	float currentX = e2.getX();
				//	list[i].setRight((int) (inialX - currentX));
					return true;
				}


				else if(e2.getX() - e1.getX() > FLIP_DISTANCE){
					if(i>0)
						tabs.setCurrentTab(i-1);	
					return true;
				}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(item.getItemId() == R.id.menu_exit){
			exit(builder);
			return true;
		}
		if(item.getItemId() == R.id.menu_settings){
			Intent intent = new Intent(MainActivity.this, SetActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	


	public void addCard(TabHost.TabSpec spec,String tag,int id,String name){
		spec = tabs.newTabSpec(tag);
		spec.setContent(id);
		spec.setIndicator(name);
		tabs.addTab(spec);
	}

	public void exit(AlertDialog.Builder builder){

		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Exit");
		builder.setMessage("Do you want to exit?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				MyApplication.getInstance().exitApp();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
								
			}
		});

		builder.create().show();
	}
	/*
	 */
	@SuppressWarnings("deprecation")
	public SimpleCursorAdapter adapter(int i){
		return new SimpleCursorAdapter(this, R.layout.list_v2,cursor[i],new String[]{"_id","classes","location",
		"teacher","zhoushu"},new int[]{R.id.number,R.id.ltext0,R.id.ltext1,R.id.ltext6,R.id.ltext7} );
	}
	
	/*
	 */
	static class SingleInstance{
		static SingleInstance si;
		private SingleInstance(){
			for(int i=0;i<7;i++){
				db.createTable(i);
			}
		}
		static SingleInstance createTable(){
			if(si==null)
				return si=new SingleInstance();
			return null;
		}
	}
}
