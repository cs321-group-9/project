package temp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;


public class MyApplication extends Application {
	

	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;
	
	private MyApplication(){}

	public static MyApplication getInstance(){
		if(instance == null)
			instance = new MyApplication();
		return instance;
	}

	public void addActivity(Activity activity){
		activityList.add(activity);
	}

	public void exitApp(){
		for(Activity activity : activityList){
			if(activity != null)
				activity.finish();
		}
		System.exit(0);
	}

	@Override
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
	
}
