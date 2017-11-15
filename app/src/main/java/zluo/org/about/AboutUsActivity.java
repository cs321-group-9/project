package zluo.org.about;

import temp.MyApplication;
import zluo.org.androidschedule.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_us);
		

		MyApplication.getInstance().addActivity(this);
		
		TextView backButton = (TextView)findViewById(R.id.backtoSetButton);

		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
//				Intent intent = new Intent(AboutUs.this,MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ˢ��
//				startActivity(intent);
			}
		});
		
	}

}
