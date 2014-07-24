package com.minggo.notification.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.minggo.love.notification.R;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.UserUtil;
/**
 * 登录页面
 * @author minggo
 * @time 2014-6-28 S下午10:00:11
 */
public class LoginActivity extends Activity implements OnClickListener {

	private View backBt;
	private EditText userNameEd;
	private EditText passwordEd;
	private Button loginBt;
	private Button registerBt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
		
		LoveNotificationApplication.allActivities.add(this);
	}
	/**
	 *  初始化UI
	 */
	private void initUI(){
		backBt = findViewById(R.id.lo_login_back);
		userNameEd = (EditText) findViewById(R.id.ed_username);
		passwordEd = (EditText) findViewById(R.id.ed_password);
		loginBt = (Button) findViewById(R.id.bt_login);
		registerBt = (Button) findViewById(R.id.bt_register);
		
		backBt.setOnClickListener(this);
		loginBt.setOnClickListener(this);
		registerBt.setOnClickListener(this);
	}
	/**
	 * 登陆操作
	 */
	private void login(){
		String email = userNameEd.getText().toString();
		String pass = passwordEd.getText().toString();
		if (!email.equals("")&&!pass.equals("")) {
			if (email.contains("@")) {
				if(UserUtil.getUser(this, email)!=null){
					Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
					this.startActivity(new Intent(this, IndexActivity.class));
				}else{
					Toast.makeText(this, R.string.user_no_exist, Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, R.string.input_email_right, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, R.string.input_null, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lo_login_back:
			onBackPressed();
			break;
		case R.id.bt_login:
			login();
			break;
		case R.id.bt_register:
			startActivity(new Intent(this, RegisterActivity.class));
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
