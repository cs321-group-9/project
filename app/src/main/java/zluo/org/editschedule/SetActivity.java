package zluo.org.editschedule;

import temp.MyApplication;
import zluo.org.about.AboutUsActivity;
import zluo.org.androidschedule.R;
import zluo.org.service.RemindReceiver;
import zluo.org.version.VersionActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity {

	private SharedPreferences preferences = null;
	private SharedPreferences.Editor editor = null;	

	private SharedPreferences pre = null;
	private SharedPreferences.Editor pre_editor = null;	

	private AlarmManager alarmManager = null;
	private PendingIntent pi = null;
	private Intent alarm_receiver = null;

	final int SINGLE_DIALOG = 0x113;
	private int time_choice = 0;
	
	private Switch switch_quietButton;
	private Switch switch_remindButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		MyApplication.getInstance().addActivity(this);

		final AudioManager audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
		Intent intent = getIntent();
		final int orgRingerMode = intent.getIntExtra("mode_ringer", AudioManager.RINGER_MODE_NORMAL);
		alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);

		alarm_receiver = new Intent(SetActivity.this,RemindReceiver.class);
//		alarm_receiver.putExtra("anvance_remindtime", time_choice);
		pi = PendingIntent.getBroadcast(SetActivity.this, 0, alarm_receiver, 0);

		TextView backButton = (TextView)findViewById(R.id.backtoMainButton);
		switch_quietButton = (Switch)findViewById(R.id.switch_quiet);
		switch_remindButton = (Switch)findViewById(R.id.switch_remind);

		this.pre = SetActivity.this.getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
		this.pre_editor = pre.edit();	

		this.preferences = SetActivity.this.getSharedPreferences("switch", Context.MODE_MULTI_PROCESS);
		this.editor = preferences.edit();
		Boolean quiet_status = preferences.getBoolean("switch_quiet", false);
		Boolean remind_status = preferences.getBoolean("switch_remind", false);
		switch_quietButton.setChecked(quiet_status);
		switch_remindButton.setChecked(remind_status);		

		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
//				Intent intent = new Intent(Set.this,MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ˢ��
//				startActivity(intent);
			}
		});

		switch_quietButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				Intent intent = new Intent();
				intent.setAction("zluo.org.service.QUIET_SERVICE");
				
				if(isChecked){
					if(startService(intent) != null)
						Toast.makeText(SetActivity.this, "Open successfully, Changing to vibrate during class", 3000).show();
					else{
						Toast.makeText(SetActivity.this, "Fail, Please try again", 3000).show();
						switch_quietButton.setChecked(false);
					}
				}
				else{
					if(stopService(intent))
						Toast.makeText(SetActivity.this, "Close successfully", 3000).show();
					else{
						Toast.makeText(SetActivity.this, "Fail, Please try again", 3000).show();
						switch_quietButton.setChecked(true);
					}
					audioManager.setRingerMode(orgRingerMode);
				}
				SetActivity.this.editor.putBoolean("switch_quiet", isChecked);
				editor.commit();
			}
		});

		switch_remindButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					showDialog(SINGLE_DIALOG);
				}
				else{
					alarmManager.cancel(pi);
				}
				SetActivity.this.editor.putBoolean("switch_remind", isChecked);
				editor.commit();
			}
		});
		
	}
				
	@Override

	protected Dialog onCreateDialog(int id, Bundle args) {
		if(id == SINGLE_DIALOG){
			Builder b = new AlertDialog.Builder(this);
			b.setTitle("Set the reminder");
			b.setSingleChoiceItems(R.array.set_remind, -1, new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog,
					int which){
					switch (which){
						case 0:
							time_choice = 5;
							break;
						case 1:						
							time_choice = 10;
							break;
						case 2:
							time_choice = 20;
							break;
						case 3:
							time_choice = 30;
							break;
						case 4:
							time_choice = 40;
							break;
						case 5:
							time_choice = 50;
							break;
						case 6:
							time_choice = 60;
							break;	
					}
				}
			});
			b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					System.out.println("SetActivity:" + time_choice);
					if(time_choice == 0){
						Toast.makeText(SetActivity.this, "Time-Choice for reminder", 3000).show();
						switch_remindButton.setChecked(false);
					}else{
						SetActivity.this.pre_editor.putInt("time_choice", time_choice);
						pre_editor.commit();
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pi);
						Toast.makeText(SetActivity.this, "Successfully, the app will remind your " + time_choice + "mins before your course", Toast.LENGTH_LONG).show();
					}
				}
			});
			b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch_remindButton.setChecked(false);	
				}
			});
			return b.create();
	 	}
		else
			return null;
	}



	public void click_us(View v){
		Intent intent = new Intent(SetActivity.this, AboutUsActivity.class);
		startActivity(intent);
	}
	public void click_version(View v){
		Intent intent = new Intent(SetActivity.this, VersionActivity.class);
		startActivity(intent);
	}
	public void click_revision(View v){
		Log.i("MyDebug", "revision");
	} 
}
