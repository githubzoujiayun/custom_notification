package com.minggo.notification.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 震动效果
 * @author minggo
 * @date 2014-6-23 下午1:38:58
 */
public class VibratorUtil {
	/**
	 * 单次按时间震动
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,Long milliseconds){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	/**
	 * 重复按时间震动
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,long[] milliseconds,boolean isRepeat){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds,isRepeat?1:-1);
	}
}

