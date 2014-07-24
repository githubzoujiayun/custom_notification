package com.minggo.notification.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.minggo.love.notification.R;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.PreferenceShareUtil;
/**
 * 设置页面
 * @author minggo
 * @time 2014-6-19 S上午12:17:07
 */
public class SettingActivity extends Activity implements OnClickListener{
	
	private View backV;
	private Button feelingBt;
	private Button timeSoundbt;
	private Button lowPowerbt;
	private Button exitbt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initUI();
		LoveNotificationApplication.allActivities.add(this);
	}
	/**
	 * 初始化UI
	 */
	private void initUI(){
		backV = findViewById(R.id.lo_setting_back);
		timeSoundbt = (Button) findViewById(R.id.zheng_sound_bt);
		feelingBt = (Button) findViewById(R.id.bt_feeling_setting);
		lowPowerbt = (Button) findViewById(R.id.low_power_bt);
		exitbt = (Button) findViewById(R.id.exitapp);
		
		lowPowerbt.setOnClickListener(this);
		backV.setOnClickListener(this);
		feelingBt.setOnClickListener(this);
		timeSoundbt.setOnClickListener(this);
		exitbt.setOnClickListener(this);
		
		lowPowerbt.setSelected(!PreferenceShareUtil.getLowPowerFlag(this));
		timeSoundbt.setSelected(!PreferenceShareUtil.getZhengTimeFlag(this));
		feelingBt.setSelected(!PreferenceShareUtil.getUseFeeling(this));
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitapp:
			//统计用户点击退出应用
			StatService.onEvent(SettingActivity.this, "exit_app", "exit");
			((LoveNotificationApplication)this.getApplication()).stopAPP();
			break;
		case R.id.low_power_bt:
			boolean flag0 = !lowPowerbt.isSelected();
			lowPowerbt.setSelected(flag0);
			PreferenceShareUtil.saveLowPowerFlag(this, !PreferenceShareUtil.getLowPowerFlag(this));
			//统计用户设置低电量提醒
			StatService.onEvent(SettingActivity.this, "battery_alert", flag0+"");
			break;
		case R.id.bt_feeling_setting:
			boolean flag1 = !feelingBt.isSelected();
			System.out.println("选择了什么东西--->"+flag1);
			feelingBt.setSelected(flag1);
			PreferenceShareUtil.saveUseFeeling(this, !PreferenceShareUtil.getUseFeeling(this));
			StatService.onEvent(SettingActivity.this, "feeling_set", flag1+"");
			break;
		case R.id.zheng_sound_bt:
			boolean flag2 = !timeSoundbt.isSelected();
			timeSoundbt.setSelected(flag2);
			PreferenceShareUtil.saveZhengTimeFlag(this, !PreferenceShareUtil.getZhengTimeFlag(this));
			//统计用户设置整点报时提醒
			StatService.onEvent(SettingActivity.this, "time_alert", flag2+"");
			break;
		case R.id.lo_setting_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	
}
