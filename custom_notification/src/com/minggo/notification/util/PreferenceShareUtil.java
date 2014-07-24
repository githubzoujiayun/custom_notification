package com.minggo.notification.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 保存用户选择声音播放的数据
 * @author minggo
 * @date 2013-8-21上午11:57:44
 */
public class PreferenceShareUtil {
	private static final String USER_DATA = "minggo_battery";
	private static final String LOW_POWER_SOUND = "low_power_sound";
	private static final String ZHENG_TIME_SOUND = "zheng_time_sound";
	
	public static final String SUNDAY_FELLING = "sunday_felling";
	public static final String MONDAY_FELLING = "monday_felling";
	public static final String TUESDAY_FELLING = "tuesday_felling";
	public static final String WEDNESDAY_FELLING = "wednesday_felling";
	public static final String THURSDAY_FELLING = "thursday_felling";
	public static final String FRIDAY_FELLING = "friday_felling";
	public static final String SATURDAY_FELLING = "saturday_felling";
	public static final String DEFAULT_FELLING_SETTING = "default_felling_setting";
	public static final String DEFAULT_ALERT_SETTING = "default_alert_setting";
	
	
	/**
	 * 保存设置自定义心情提醒
	 * @param context
	 * @param flag
	 */
	public static void saveUseFeeling(Context context,boolean flag){
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_WORLD_WRITEABLE);
		Editor editor = share.edit();
		editor.putBoolean(DEFAULT_FELLING_SETTING, flag);
		editor.commit();
	}
	/**
	 * 获取是否适用自定义心情设置
	 * @param context
	 * @return
	 */
	public static boolean getUseFeeling(Context context){
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
		if (share != null) {
			return share.getBoolean(DEFAULT_FELLING_SETTING, false);
		}
		return false;
	}
	
	/**
	 * 获取某一天的心情
	 * @param context
	 * @param whichDay
	 * @return
	 */
	public static String getFeeling(Context context,String whichDay){
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		if (share != null) {
			//System.out.println("数据库拿到的时候--->"+share.getBoolean(LOW_POWER_SOUND, false));
			return share.getString(whichDay, "");
		}
		return "";
	}
	
	/**
	 * 保存用户低电量声音设置
	 * @param context
	 * @param flag
	 */
	public static void saveFeeling(Context context,String whichDay,String feeling) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(whichDay, feeling);
		editor.commit();
	}
	
	/**
	 * 获取用户设置低电量的设置
	 * @param context
	 * @return
	 */
	public static boolean getLowPowerFlag(Context context) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		if (share != null) {
			//System.out.println("数据库拿到的时候--->"+share.getBoolean(LOW_POWER_SOUND, false));
			return share.getBoolean(LOW_POWER_SOUND, true);
		}else{
			saveLowPowerFlag(context, true);
		}
		return true;
	}
	/**
	 * 获取用户整点报时声音的设置
	 * @param context
	 * @return
	 */
	public static boolean getZhengTimeFlag(Context context) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
		if (share != null) {
			return share.getBoolean(ZHENG_TIME_SOUND, true);
		}else{
			saveZhengTimeFlag(context, true);
		}
		return true;
	}
	/**
	 * 保存用户低电量声音设置
	 * @param context
	 * @param flag
	 */
	public static void saveLowPowerFlag(Context context,boolean flag) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_WORLD_WRITEABLE);
		Editor editor = share.edit();
		editor.putBoolean(LOW_POWER_SOUND, flag);
		editor.commit();

	}
	/**
	 * 保存用户整点报时声音设置
	 * @param context
	 * @param flag
	 */
	public static void saveZhengTimeFlag(Context context,boolean flag) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_WORLD_WRITEABLE);
		Editor editor = share.edit();
		editor.putBoolean(ZHENG_TIME_SOUND, flag);
		editor.commit();
	}
	
	
	
}
