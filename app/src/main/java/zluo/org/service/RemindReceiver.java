package zluo.org.service;

import java.util.Calendar;
import java.util.Date;
import temp.DataBase;
import temp.ShareMethod;
import zluo.org.editschedule.RemindActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

public class RemindReceiver extends BroadcastReceiver {


	Cursor[] cursor = new Cursor[7];

	String[][][] temp = new String[7][12][3];

	int[][][] start_time = new int[7][12][2];
	private int advance_time;
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {

		DataBase db = new DataBase(arg0);

		for(int i=0;i<7;i++){
			cursor[i]=db.select(i);					
		}

		for(int day=0;day<7;day++){ 
			for(int row=0;row<12;row++){
				cursor[day].moveToPosition(row);
				temp[day][row][2] = cursor[day].getString(5);
	 			if(!temp[day][row][2].equals("")){
	 				temp[day][row][2] = temp[day][row][2].substring(temp[day][row][2].indexOf(":")+2);
	 				temp[day][row][0] = temp[day][row][2].substring(0, temp[day][row][2].indexOf(":"));
	 				temp[day][row][1] = temp[day][row][2].substring(temp[day][row][2].indexOf(":")+1);
	 			}
	 			else{
	 				temp[day][row][0] = temp[day][row][1] = "0";
	 			}
	 			for(int hm=0;hm<2;hm++){
	 				start_time[day][row][hm] = Integer.parseInt(temp[day][row][hm]);
	 			}
			}
		}
		

		Intent remind_intent = new Intent(arg0, RemindActivity.class);
		remind_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		

//		int advance_time = arg1.getIntExtra("anvance_remindtime", 20);

		SharedPreferences pre = arg0.getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
		advance_time = pre.getInt("time_choice", 30);
		int currentday = ShareMethod.getWeekDay();
//		System.out.println(advance_time);
		
		Calendar c = Calendar.getInstance();

		int current_hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		int current_minute = c.get(Calendar.MINUTE);
				

		boolean flag = true;

		for(int i=0;i<12;i++){
			if(!(start_time[currentday][i][0]==0 && start_time[currentday][i][1]==0)){

				c.set(Calendar.HOUR_OF_DAY, start_time[currentday][i][0]);
				c.set(Calendar.MINUTE, start_time[currentday][i][1]);
				long remind_time = c.getTimeInMillis()-advance_time*60*1000;		
				Date date=new Date(remind_time);
				c.setTime(date);
				

				int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				

				if(hourOfDay==current_hourOfDay && minute==current_minute){
					if(flag){
						arg0.startActivity(remind_intent);						
//						System.out.println("time remind" + i);
						flag = false;						
					}
				}else{
					flag = true;
				}
			}
		}
		
	}

}
