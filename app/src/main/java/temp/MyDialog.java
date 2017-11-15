package temp;

import java.util.Calendar;
import zluo.org.androidschedule.MainActivity;
import zluo.org.androidschedule.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyDialog {
	
	private EditText course_name;
	private EditText course_address;
	private EditText course_teacher;
	private EditText course_week;
//	private EditText course_time1,course_time2;
	private EditText course_count;
	

	private View view;
	private Context context;
	private LayoutInflater inflater;
	private Builder builder;
	MyAdapter adapter;
	MainActivity main;
	String s1="",s2="",s3="",s4="",s5="",s6="",s7="";
	
	public MyDialog(Context context){
		this.context=context;
		main=(MainActivity) context;
		adapter=new MyAdapter(context);		
	}
	 /*
	 * ���δ�༭�Ŀγ��б���������ӿγ̡��Ի���
	 */
	public void add(final int day,final int n){
		//��װ�Ի����view
		inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.edit_shedule,null);
		findWidgetes();//ȡ����
		final Button course_time1=(Button)view.findViewById(R.id.time1);
		final Button course_time2=(Button)view.findViewById(R.id.time2);
		//Ϊ��������ʱ��İ�ť�󶨼�����
		course_time1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time1);
			}
		});		
		course_time2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time2);
			}
		});
		
		builder=new AlertDialog.Builder(context)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("Update course")
		.setView(view)
		.setPositiveButton("Yes",new OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(!(s1=course_name.getText().toString()).equals("")) s1="Course: "+s1;
				if(!(s2=course_address.getText().toString()).equals("")) s2="Location: "+s2;
				if(!(s3=course_teacher.getText().toString()).equals("")) s3="Professor: "+s3;
				if(!(s4=course_week.getText().toString()).equals("")) s4="Weeks: "+s4;
				if(!(s6=course_time1.getText().toString()).equals("")) s6="Time: "+s6;
				if(!(s7=course_time2.getText().toString()).equals("")) ;
				
				if((s5=course_count.getText().toString()).equals("")||s1.equals("")) {
					Toast.makeText(context, "Please enter correct course information! ", 3000).show();
					return;
				}
				else {
					int i=Integer.parseInt(s5.trim());//iΪ����
					for(int m=0;m<i;m++){
						MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
					}
						
				}
				
				main.cursor[day].requery();
				main.list[day].invalidate();			
			}
			
		})
		.setNegativeButton("Cancel", new OnClickListener(){
	
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				
			}
			
		});
		builder.create().show();
		
	}
	

	public void modify(final int day,final int n){
		inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.edit_shedule,null);
		findWidgetes();
		final Button course_time1=(Button)view.findViewById(R.id.time1);
		final Button course_time2=(Button)view.findViewById(R.id.time2);
		course_time1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time1);
			}
		});		
		course_time2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time2);
			}
		});

		main.cursor[day].moveToPosition(n);
		String [] temp=new String[8];
		for(int i=0;i<8;i++) {temp[i]=main.cursor[day].getString(i+1);}

		if(!temp[0].equals("")) course_name.setText(temp[0].substring(temp[0].indexOf(":")+2));
		if(!temp[1].equals("")) course_address.setText(temp[1].substring(temp[1].indexOf(":")+2));
		if(!temp[2].equals("")) course_teacher.setText(temp[2].substring(temp[2].indexOf(":")+2));
		if(!temp[3].equals("")) course_week.setText(temp[3].substring(temp[3].indexOf(":")+2));
		if(!temp[4].equals("")) course_time1.setText(temp[4].substring(temp[4].indexOf(":")+2));
		course_time2.setText(temp[5]);
		course_count.setText(temp[6]);
		view.invalidate();
		
		
		builder=new AlertDialog.Builder(context)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("Modify course")
		.setView(view)
		.setPositiveButton("Yes",new OnClickListener(){
	
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(!(s1=course_name.getText().toString()).equals("")) s1="Course: "+s1;
				if(!(s2=course_address.getText().toString()).equals("")) s2="Location: "+s2;
				if(!(s3=course_teacher.getText().toString()).equals("")) s3="Professor: "+s3;
				if(!(s4=course_week.getText().toString()).equals("")) s4="Weeks: "+s4;
				if(!(s6=course_time1.getText().toString()).equals(""))s6="Times: "+s6;
				if(!(s7=course_time2.getText().toString()).equals(""));
				s5=course_count.getText().toString();
				main.cursor[day].moveToPosition(n);
				int n1=Integer.parseInt(main.cursor[day].getString(7).trim());
				int n2=Integer.parseInt(main.cursor[day].getString(8).trim());
				Log.i("kkk",main.cursor[day].getString(7));

				if(s5.equals("")||n1==Integer.parseInt(s5.trim())) {
					switch(n2){
						case 0:
							for(int m=0;m<n1;m++){
								MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
							
						case 1:
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
							for(int m=1;m<n1;m++){
								MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;		
						case 2:
							MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
							for(int m=2;m<n1;m++){
								MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
						case 3:
							for(int m=n2;m>=0;m--){
								MainActivity.db.update(day,n-m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(n2-m));
								}
							break;
					}
				
				}
				//�������б仯����ȷ���½���������ɵ������ٸ�������
				else{
					int n3=Integer.parseInt(s5.trim());
					//�������
					if(n3>n1){
						
						switch(n2){//��������
						case 0:
							for(int m=0;m<n3;m++){
								MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
							
						case 1:
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
							for(int m=1;m<n3;m++){
								MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;		
						case 2:
							MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
							for(int m=2;m<n3;m++){
								MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
						case 3:
							for(int m=n2;m>=0;m--){
								MainActivity.db.update(day,n-m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(n2-m));
								}
							break;
							}
					
					}
					//����������ɾ���������ٸ����µĽ������������������������
					if(n3<n1){
						switch(n2){//ɾ��
							case 0:	
								for(int m=0;m<n1;m++){
									MainActivity.db.deleteData(day,n+m+1);
									}
								break;
	
							case 1:
								MainActivity.db.deleteData(day,n);
								for(int m=1;m<n1;m++){
									MainActivity.db.deleteData(day,n+m);
									}
								break;		
							case 2:
								MainActivity.db.deleteData(day,n-1);
								MainActivity.db.deleteData(day,n);
								for(int m=2;m<n1;m++){
									MainActivity.db.deleteData(day,n+m-1);
									}
									break;
							case 3:
								for(int m=n2;m>=0;m--){
									MainActivity.db.deleteData(day,n-m+1);
									}
								break;
							default:
								Toast.makeText(context, "error", 3000).show();
								break;
						}
						
					
						switch(n2){//��������
							case 0:
								for(int m=0;m<n3;m++){
									MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
								
							case 1:
								MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
								for(int m=1;m<n3;m++){
									MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;		
							case 2:
								MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
								MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
								for(int m=2;m<n3;m++){
									MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
							case 3:
								for(int m=0;m<n3;m++){
									MainActivity.db.update(day,n+m-2,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
						}
							
					}
				}
				main.cursor[day].requery();
				main.list[day].invalidate();
			
			}
			
		})
		.setNegativeButton("Cancel", new OnClickListener(){
	
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				
			}
			
		});
		builder.create().show();
		
	}
	
	private void findWidgetes(){
		course_name=(EditText)view.findViewById(R.id.editText1);
		course_address=(EditText)view.findViewById(R.id.editText2);
		course_teacher=(EditText)view.findViewById(R.id.editText3);
		course_week=(EditText)view.findViewById(R.id.editText4);
//		course_time1=(EditText)view.findViewById(R.id.time1);
//		course_time2=(EditText)view.findViewById(R.id.time2);
		course_count=(EditText)view.findViewById(R.id.jieshu);		
	}
	
	public void TimeSet_Dialog(final TextView text){
		Calendar c = Calendar.getInstance();

		new TimePickerDialog(main,

			new TimePickerDialog.OnTimeSetListener()
			{
				@Override
				public void onTimeSet(TimePicker tp, int hourOfDay,int minute){

					StringBuffer s_hour = new StringBuffer();
					StringBuffer s_minute = new StringBuffer();
					s_hour.append(hourOfDay);
					s_minute.append(minute);
					if(hourOfDay<10){
						s_hour.insert(0,"0");
					}
					if(minute<10){
						s_minute.insert(0,"0");
					}

					text.setText(s_hour.toString() + ":" + s_minute.toString());
				}
			}

		, c.get(Calendar.HOUR_OF_DAY)
		, c.get(Calendar.MINUTE)

		, true).show();
	}
}
