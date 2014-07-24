package com.minggo.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.minggo.love.notification.R;
import com.minggo.notification.model.SoundRecord;
import com.minggo.notification.model.User;
import com.minggo.notification.util.UserUtil;
/**
 * 应用全局
 * @author minggo
 * @time 2014-6-23 S下午10:49:54
 */
public class LoveNotificationApplication extends Application {
	
	public List<SoundRecord> defaultSoundList;
	public static final String EMAIL = "minggo8en@gmail.com";
	public static Stack<Activity> allActivities;
	@Override
	public void onCreate() {
		super.onCreate();
		allActivities = new Stack<Activity>();
		firstInit();
		initSoundList();
	}
	
	/**
	 * 结束所有Activity
	 */
	public static void finishAllActivity() {
		for (int i = 0, size = allActivities.size(); i < size; i++) {
			if (null != allActivities.get(i)) {
				allActivities.get(i).finish();
			}
		}
		allActivities.clear();
	}
	/**
	 * 推出应用
	 */
	public void stopAPP(){
		finishAllActivity();
		this.stopService(new Intent("minggo.battery.alarm.service"));
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
	/**
	 * 初始化默认孙燕姿播放列表
	 */
	private void initSoundList(){
		if (defaultSoundList==null) {
			defaultSoundList = new ArrayList<SoundRecord>();
			for (int i = 6; i < 24; i++) {
				SoundRecord soundRecord = new SoundRecord();
				soundRecord.path = "sound/"+i+".mp3";
				soundRecord.type = 1;
				soundRecord.whichHour = i;
				defaultSoundList.add(soundRecord);
			}
		}
	}
	/**
	 * 第一次安装时的初始化
	 */
	private void firstInit(){
		boolean preferences = getSharedPreferences("first", Context.MODE_PRIVATE).getBoolean("isfrist", true);
		if (preferences) {
			User user = new User();
			user.name = "sys_minggo";
			user.email = EMAIL;
			user.password = "12345678";
			user.type = 0;
			user.useDefineSound = 0;
			user.useDefinFeeling = 0;
			UserUtil.saveUser(this, user);
			
			User user1 = new User();
			user1.email = this.getString(R.string.costomer_name);
			user1.password = this.getString(R.string.costomer_password);
			user1.type = 1;
			UserUtil.saveUser(this, user);
		}
	}
	
}
