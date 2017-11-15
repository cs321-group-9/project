package zluo.org.version;

import temp.MyApplication;
import zluo.org.androidschedule.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VersionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		

		MyApplication.getInstance().addActivity(this);
		
		TextView backButton = (TextView)findViewById(R.id.backtoSetButton);

		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
