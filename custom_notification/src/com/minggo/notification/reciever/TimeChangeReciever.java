package com.minggo.notification.reciever;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.minggo.notification.model.SoundRecord;
import com.minggo.notification.model.User;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.MinggoDate;
import com.minggo.notification.util.PlaySound;
import com.minggo.notification.util.PreferenceShareUtil;
import com.minggo.notification.util.SoundRecordUtil;
import com.minggo.notification.util.UserUtil;

/**
 * 时间广播监听器
 * 
 * @author minggo
 * @date 2013-8-7下午01:01:13
 */
public class TimeChangeReciever extends BroadcastReceiver {

	private MinggoDate date;
	private User user;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_TIME_TICK)) {
			// System.out.println("整点报时--->"+date.get24Hour()+"点");
			if (PreferenceShareUtil.getZhengTimeFlag(context)) {
				date = new MinggoDate();
				user = UserUtil.getUser(context, LoveNotificationApplication.EMAIL);
				if (user.type == 0) {
					if (date.getMinutes() == 00 && date.get24Hour() > 5) {
						// System.out.println("整点报时--->"+date.get24Hour()+"点");
						try {
							PlaySound.play("sound/" + date.get24Hour() + ".mp3", context.getAssets());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (user.type == 1) {
					if (date.getMinutes() == 00) {
						SoundRecord soundRecord = SoundRecordUtil.getSoundRecord(context, date.get24Hour());

						if (soundRecord != null) {

							try {
								PlaySound.playVoice2(soundRecord.path, context.getResources().getAssets(), 2);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							if (date.getMinutes() == 00 && date.get24Hour() > 5) {
								// System.out.println("整点报时--->"+date.get24Hour()+"点");
								try {
									PlaySound.play("sound/" + date.get24Hour() + ".mp3",
											context.getAssets());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}else{
				Log.i("alert", "设置了不提醒");
			}
		}
	}

}
