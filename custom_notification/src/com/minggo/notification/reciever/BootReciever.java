package com.minggo.notification.reciever;

import com.baidu.mobstat.StatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
/**
 * 软件安装和卸载监听
 * @author minggo
 * @date 2014-7-3 下午2:59:24
 */
public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收安装广播
		/*if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
		}*/
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			TelephonyManager tm = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
			String telephone = "no_get";
			if (tm.getLine1Number() != null && !tm.getLine1Number().equals("")) {
				telephone = tm.getLine1Number();
			}
			if ("minggo.battery".equals(packageName)) {
				StatService.onEvent(context, "uninstall", telephone);
			}

		}
	}
}
