package com.minggo.notification.view;

import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.minggo.love.notification.R;
import com.minggo.notification.util.ClippingSounds;
import com.minggo.notification.util.PlaySound;

/**
 * 录音按钮
 * 
 * @author minggo
 * @date 2014-6-23 下午1:21:42
 */
public class RecordButton extends Button {
	private boolean isEnable;
	private Context context;

	public RecordButton(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setOnEventListener(OnEventListener onEventListener, boolean isEnable) {
		this.onEventListener = onEventListener;
		this.isEnable = isEnable;
	}

	private String mFileName = null;
	private OnEventListener onEventListener;
	private static final int MIN_INTERVAL_TIME = 2000;// 2s
	private long startTime;
	private Dialog recordIndicator;

	private static int[] res = { R.drawable.voice_0, R.drawable.voice_1, R.drawable.voice_2, R.drawable.voice_3,
			R.drawable.voice_4, R.drawable.voice_5, R.drawable.voice_6, R.drawable.voice_7, R.drawable.voice_8,
			R.drawable.voice_9, R.drawable.voice_10, R.drawable.voice_11, R.drawable.voice_12 };

	private static ImageView view;

	private MediaRecorder recorder;

	private ObtainDecibelThread thread;

	private Handler volumeHandler;

	private void init() {
		volumeHandler = new ShowVolumeHandler();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isEnable) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
				setText("松手 保存");
				initDialogAndStartRecord();
				break;
			case MotionEvent.ACTION_UP:
				finishRecord();
				setText("按住 说话");
				// setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				break;
			case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
				cancelRecord();
				setText("按住 说话");
				// setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				Toast.makeText(getContext(), "已放弃录音", 2000).show();
				break;
			}
		}
		return true;
	}

	private void initDialogAndStartRecord() {
		startTime = System.currentTimeMillis();
		recordIndicator = new Dialog(getContext(), R.style.LoadingDialogStyle);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.voice_record_dialog, null);
		view = (ImageView) (layout.findViewById(R.id.voice_record_iv_heigher));
		view.setImageResource(res[0]);
		recordIndicator.setContentView(layout);
		recordIndicator.setOnDismissListener(onDismiss);
		recordIndicator.show();

		try {
			PlaySound.play("sound/qrcode_completed.mp3", context.getAssets());
			view.postDelayed(new Runnable() {

				@Override
				public void run() {
					try {
						startRecording();
						removeCallbacks(this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 500);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void finishRecord() {
		stopRecording();

		System.out.println(System.currentTimeMillis() + "==ddddddd===" + startTime + "ffff" + mFileName);
		long intervalTime = System.currentTimeMillis() - startTime;
		if (intervalTime < MIN_INTERVAL_TIME) {
			Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
			File f = new File(mFileName);
			f.delete();
		} else {
			view.setImageResource(R.drawable.success_icon);
			if (onEventListener != null)
				onEventListener.onFinishedRecord(mFileName, (int) intervalTime / 1000);
		}

		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				recordIndicator.dismiss();
			}
		}, 300);

	}

	private void cancelRecord() {
		stopRecording();
		recordIndicator.dismiss();
		Toast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT).show();
		File file = new File(mFileName);
		file.delete();
	}

	private void startRecording() throws Exception {
		mFileName = ClippingSounds.saveSounds();
		System.out.println("开始的时候-----》" + mFileName);
		
		File file = new File(mFileName);
		if (file.isFile()&&!file.exists()) {
			file.createNewFile();
		}
		
		recorder = new MediaRecorder();
		if (onEventListener != null) {
			onEventListener.onStartRecord();
		}
		try {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(mFileName);
			recorder.setMaxDuration(180 * 1000);
			recorder.prepare();
			// startRecordListener.onStartRecord();
			recorder.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		thread = new ObtainDecibelThread();
		thread.start();
	}

	private void stopRecording() {
		if (thread != null) {
			thread.exit();
			thread = null;
		}
		if (recorder != null) {
			try {
				recorder.stop();
				recorder.release();
				recorder = null;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private class ObtainDecibelThread extends Thread {

		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (recorder == null || !running) {
					break;
				}
				int x = recorder.getMaxAmplitude();
				if (x != 0) {
					System.out.println("有没有声音--->" + x);
					int f = (int) (10 * Math.log(x) / Math.log(10));
					if (f == 0) {
						volumeHandler.sendEmptyMessage(0);
					} else if (f < 10)
						volumeHandler.sendEmptyMessage(1);
					else if (f < 20)
						volumeHandler.sendEmptyMessage(2);
					else if (f < 25)
						volumeHandler.sendEmptyMessage(3);
					else if (f < 30)
						volumeHandler.sendEmptyMessage(4);
					else if (f < 35)
						volumeHandler.sendEmptyMessage(5);
					else if (f < 40)
						volumeHandler.sendEmptyMessage(6);
					else if (f < 42)
						volumeHandler.sendEmptyMessage(7);
					else if (f < 44)
						volumeHandler.sendEmptyMessage(8);
					else if (f < 46)
						volumeHandler.sendEmptyMessage(9);
					else if (f < 48)
						volumeHandler.sendEmptyMessage(10);
					else if (f < 50)
						volumeHandler.sendEmptyMessage(11);
					else if (f < 55)
						volumeHandler.sendEmptyMessage(12);
					else
						volumeHandler.sendEmptyMessage(12);
				}

			}
		}

	}

	private OnDismissListener onDismiss = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			stopRecording();
		}
	};

	static class ShowVolumeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			view.setImageResource(res[msg.what]);
		}
	}

	public interface OnEventListener {
		public void onFinishedRecord(String audioPath, int time);

		public void onStartRecord();
	}

}
