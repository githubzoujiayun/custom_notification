package com.minggo.notification.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minggo.love.notification.R;
import com.minggo.notification.activity.LoginActivity;
import com.minggo.notification.adapter.HourAdapter;
import com.minggo.notification.adapter.SoundAdapter;
import com.minggo.notification.adapter.SoundAdapter.TryListener;
import com.minggo.notification.model.SoundRecord;
import com.minggo.notification.model.User;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.PlaySound;
import com.minggo.notification.util.PlaySound.FinishListen;
import com.minggo.notification.util.SoundRecordUtil;
import com.minggo.notification.util.UserUtil;
import com.minggo.notification.util.VibratorUtil;
import com.minggo.notification.view.RecordButton;
import com.minggo.notification.view.RecordButton.OnEventListener;

/**
 * 整点报时设置页面
 * 
 * @author minggo
 * @time 2014-6-16 S下午9:18:35
 */
public class FragmentTimeSetting extends Fragment implements TryListener, OnClickListener, OnItemClickListener,
		OnItemLongClickListener {

	private Activity activity;
	private View timeSettinView;
	public static AssetManager assetManager;
	private RecordButton recordButton;

	private List<SoundRecord> soundRecordList;
	private ListView soundLv;
	private SoundAdapter soundAdapter;
	private ImageView switchIv;
	private User user;
	private TextView listTipsTv;
	private String[] hours;
	private ListView hoursLv;
	private HourAdapter hourAdapter;
	private ImageView arrowDownIv;
	private String currHour = "00:00";
	private TextView currHourTv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		assetManager = activity.getResources().getAssets();
		soundRecordList = new ArrayList<SoundRecord>();
		hours = activity.getResources().getStringArray(R.array.hour);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		timeSettinView = inflater.inflate(R.layout.fragment_alert, container, false);
		recordButton = (RecordButton) timeSettinView.findViewById(R.id.bt_record_sound);
		soundLv = (ListView) timeSettinView.findViewById(R.id.lv_alert_sounds);
		hoursLv = (ListView) timeSettinView.findViewById(R.id.lv_time_option);
		switchIv = (ImageView) timeSettinView.findViewById(R.id.iv_switch);
		listTipsTv = (TextView) timeSettinView.findViewById(R.id.tv_list_tips);
		currHourTv = (TextView) timeSettinView.findViewById(R.id.tv_alert_time);
		arrowDownIv = (ImageView) timeSettinView.findViewById(R.id.iv_list_time);

		arrowDownIv.setOnClickListener(this);
		switchIv.setOnClickListener(this);

		return timeSettinView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSoundList();
		refreshSoundListUI();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			getSoundList();
			refreshSoundListUI();
		}
	}

	@Override
	public View getView() {
		return super.getView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_try_top:

			break;
		case R.id.iv_list_time:
			hoursLv.setVisibility(hoursLv.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
			break;
		case R.id.iv_switch:
			
			//if (UserUtil.isExistUser(activity)) {
				if (user.useDefineSound == 0) {
					user.useDefineSound = 1;
					UserUtil.saveUser(activity, user);
				} else {
					user.useDefineSound = 0;
					UserUtil.saveUser(activity, user);
				}
				getSoundList();
				refreshSoundListUI();
			//}else{
				//activity.startActivity(new Intent(activity, LoginActivity.class));
			//}
			break;

		default:
			break;
		}
	}

	/**
	 * 获取播放录音列表
	 */
	private void getSoundList() {

		if (hourAdapter == null) {
			hourAdapter = new HourAdapter(activity, hours);
			hoursLv.setAdapter(hourAdapter);
			hoursLv.setOnItemClickListener(this);
		}

		if (user == null) {
			user = UserUtil.getUser(activity, LoveNotificationApplication.EMAIL);
		}
		if (user.useDefineSound == 0) {

			soundRecordList.clear();
			soundRecordList.addAll(((LoveNotificationApplication) activity.getApplication()).defaultSoundList);

			listTipsTv.setText(R.string.alert_list_tips_sys);
			recordButton.setEnabled(false);
			switchIv.setImageResource(R.drawable.switch_off);
			recordButton.setOnEventListener(new VoiceListener(), false);
			soundLv.setOnItemLongClickListener(null);
		} else {
			soundLv.setOnItemLongClickListener(this);
			soundRecordList.clear();
			List<SoundRecord> list = SoundRecordUtil.getSoundRecordList(activity, 2);
			if (list != null && !list.isEmpty()) {
				soundRecordList.addAll(list);
			}
			listTipsTv.setText(R.string.alert_list_tips_user);
			recordButton.setEnabled(true);
			switchIv.setImageResource(R.drawable.switch_on);
			recordButton.setOnEventListener(new VoiceListener(), true);
		}
	}

	/**
	 * 刷新声音列表
	 */
	private void refreshSoundListUI() {
		if (soundAdapter == null) {
			soundAdapter = new SoundAdapter(activity, soundRecordList, this);
			soundLv.setAdapter(soundAdapter);
		} else {
			soundAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 实现自定义的录音按钮的开始录音和结束录音
	 * 
	 * @author minggo
	 * @created 2013-2-23下午12:39:56
	 */
	public class VoiceListener implements OnEventListener {

		@Override
		public void onFinishedRecord(String audioPath, int time) {
			try {

				Log.i("record", audioPath + ",time-->" + time);
				SoundRecord soundRecord = new SoundRecord();
				soundRecord.longTime = String.valueOf(time) + "''";
				soundRecord.path = audioPath;
				soundRecord.type = 2;
				soundRecord.whichHour = Integer.parseInt(currHour.substring(0, 2));

				SoundRecordUtil.saveSubscriptionGiftType(activity, soundRecord);

				PlaySound.play("sound/qrcode_completed.mp3", assetManager);
				getSoundList();
				refreshSoundListUI();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("record", "录音完成");

		}

		@Override
		public void onStartRecord() {
			VibratorUtil.Vibrate(activity, (long) 100);
		}

	}

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			return false;
		}
	});

	private boolean isRepeat0 = true;
	private int currIndex = -1;
	private View v;

	@Override
	public void onTryClick(final View v, int position) {

		if (currIndex != -1 && currIndex != position) {
			PlaySound.stopVoice();
			handler.removeCallbacks(runnable);
			if (this.v != null) {
				((ImageButton) this.v).setImageResource(R.drawable.chatfrom_voice_playing_f3);
			}

		}
		Log.i("battery", "currIndex-->" + currIndex + ",position-->" + position + ",isRepeat-->" + isRepeat0);
		if (currIndex != position || (currIndex == position && !isRepeat0)) {
			this.v = v;
			currIndex = position;
			isRepeat0 = true;

			try {
				handler.post(runnable);
				PlaySound.playVoice(soundRecordList.get(position).path, activity.getResources().getAssets(),
						new FinishListen() {

							@Override
							public void onFinish() {
								isRepeat0 = false;
							}
						}, soundRecordList.get(position).type);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (isRepeat0) {
				PlaySound.stopVoice();
				handler.removeCallbacks(runnable);
				if (this.v != null) {
					((ImageButton) this.v).setImageResource(R.drawable.chatfrom_voice_playing_f3);
				}
				isRepeat0 = false;
			}
		}
	}

	private Runnable runnable = new Runnable() {
		int i = 0;

		@Override
		public void run() {
			if (isRepeat0 == true) {
				if (i == 0) {
					((ImageButton) v).setImageResource(R.drawable.chatfrom_voice_playing_f1);
				} else if (i == 1) {
					((ImageButton) v).setImageResource(R.drawable.chatfrom_voice_playing_f2);
				} else if (i == 3) {
					((ImageButton) v).setImageResource(R.drawable.chatfrom_voice_playing_f3);
					i = -1;
				}
				i++;
				handler.postDelayed(this, 250);
			} else {
				isRepeat0 = false;
				handler.removeCallbacks(this);
				((ImageButton) v).setImageResource(R.drawable.chatfrom_voice_playing_f3);
			}
		}
	};

	private void deleteSoundRecord(final int position){

		AlertDialog.Builder builder = new Builder(activity);
		String time = String.valueOf(soundRecordList.get(position).whichHour);
		if (time.length()<2) {
			time="0"+time+":00";
		}else{
			time= time+":00";
		}
		String message = activity.getString(R.string.delete)+time+activity.getString(R.string.record_sound)+"?";
		builder.setMessage(message);
		builder.setTitle(R.string.tips_1);
		builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SoundRecordUtil.deleteSound(activity, soundRecordList.get(position));
				soundRecordList.remove(position);
				soundAdapter.notifyDataSetChanged();
			}
		});
		builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialog.cancel();
			}
		});
		builder.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		currHour = hours[position];
		currHourTv.setText(currHour);
		hoursLv.setVisibility(View.GONE);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Log.i("record", "长按可删除");
		deleteSoundRecord(position);
		return false;
	}

}
