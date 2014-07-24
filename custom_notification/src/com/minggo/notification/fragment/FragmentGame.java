package com.minggo.notification.fragment;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.minggo.love.notification.R;
import com.minggo.notification.util.MinggoDate;
import com.minggo.notification.util.PlaySound;
import com.minggo.notification.util.ShakeListener;
import com.minggo.notification.util.ShakeListener.OnShakeListener;

/**
 * 甩色子
 * 
 * @author minggo
 * @time 2014-6-16 S下午9:18:35
 */
public class FragmentGame extends Fragment implements OnClickListener {


	public static AssetManager assetManager;
	private ShakeListener mShakeListener = null;
	private Vibrator mVibrator;
	private ImageView seZiiv;
	private int i = 0;
	private int j = 0;
	private Handler hdl;

	private boolean changejiawu;
	private int sourceIds[];
	private int sourceIds2[];
	private String telephone;
	private ImageView cancelAdIv;
	//private AdView adView;
	private Activity activity;
	private View gameView;
	
	private Button shuaiDianziBt;
	private Button shuaijiawuBt;
	private boolean isShaking;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("oncreate.....");
		assetManager = activity.getResources().getAssets();
		hdl = new Handler();
		mVibrator = (Vibrator) activity.getApplication().getSystemService(android.content.Context.VIBRATOR_SERVICE);

		sourceIds = new int[] { R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4,
				R.drawable.dice_5, R.drawable.dice_6 };
		sourceIds2 = new int[] { R.drawable.se_dice_1, R.drawable.se_dice_2, R.drawable.se_dice_3,
				R.drawable.se_dice_4, R.drawable.se_dice_5, R.drawable.se_dice_6 };

		TelephonyManager tm = (TelephonyManager) activity.getSystemService(android.content.Context.TELEPHONY_SERVICE);
		if (tm.getLine1Number() != null && !tm.getLine1Number().equals("")) {
			telephone = tm.getLine1Number();
			StatService.onEvent(activity, "user", "telephone_" + telephone);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		gameView = inflater.inflate(R.layout.fragment_game, container, false);
		seZiiv = (ImageView) gameView.findViewById(R.id.game_shack_iv);
		cancelAdIv = (ImageView) gameView.findViewById(R.id.cancel_ad_iv);
		shuaiDianziBt = (Button) gameView.findViewById(R.id.bt_sezi_dian);
		shuaijiawuBt = (Button) gameView.findViewById(R.id.bt_sezi_jiawu);
		//adView = (AdView) gameView.findViewById(R.id.adView);

		//adView.setListener(new AdListener());
		shuaiDianziBt.setOnClickListener(this);
		shuaijiawuBt.setOnClickListener(this);
		seZiiv.setOnClickListener(this);
		//cancelAdIv.setOnClickListener(this);
		
		onClick(shuaiDianziBt);
		
		return gameView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initShock();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (mShakeListener != null) {
				mShakeListener.start();
			}
		} else {
			if (mShakeListener != null) {
				mShakeListener.stop();
			}
		}
	}

	@Override
	public boolean getUserVisibleHint() {

		System.out.println("getUserVisibleHint()");

		return super.getUserVisibleHint();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		System.out.println("onHiddenChanged");
		super.onHiddenChanged(hidden);
	}
	
	
	
	/**
	 * 初始化色子甩过程
	 */
	public void initShock() {

		seZiiv.setImageResource(sourceIds2[0]);

		mShakeListener = new ShakeListener(activity);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				StatService.onEvent(activity, "play_type", changejiawu == true ? "甩家务" : "甩点子");
				StatService.onEvent(activity, "shake", telephone);// 统计用户使用甩色子

				mShakeListener.stop();
				
				isShaking = true;
				
				if (startVibrato()) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mVibrator.cancel();
							mShakeListener.start();
						}
					}, 800);
				}
				if (!changejiawu) {
					hdl.post(new Runnable() {
						@Override
						public void run() {
							if (i <= 50) {
								i += j;
								if (j == 0) {
									seZiiv.setImageResource(R.drawable.se_dice_action_0);
								} else if (j == 1) {
									seZiiv.setImageResource(R.drawable.se_dice_action_1);

								} else if (j == 2) {
									seZiiv.setImageResource(R.drawable.se_dice_action_2);

								} else if (j == 3) {
									seZiiv.setImageResource(R.drawable.se_dice_action_3);
									j = -1;
								}
								j++;
								// 让色子逐渐变慢
								if (i < 35) {
									hdl.postDelayed(this, 100);
								} else if (35 <= i && i < 58) {
									hdl.postDelayed(this, 100 + i);
								} else {
									hdl.postDelayed(this, 160);
								}
							} else {
								isShaking = false;
								i = 0;
								seZiiv.setImageResource(sourceIds2[(int) (Math.random() * 6)]);
								hdl.removeCallbacks(this);
							}
						}
					});
				} else {
					hdl.post(new Runnable() {
						@Override
						public void run() {
							if (i <= 50) {
								i += j;
								if (j == 0) {
									seZiiv.setImageResource(R.drawable.dice_action_0);
								} else if (j == 1) {
									seZiiv.setImageResource(R.drawable.dice_action_1);

								} else if (j == 2) {
									seZiiv.setImageResource(R.drawable.dice_action_2);

								} else if (j == 3) {
									seZiiv.setImageResource(R.drawable.dice_action_3);
									j = -1;
								}
								j++;
								// 让色子逐渐变慢
								if (i < 35) {
									hdl.postDelayed(this, 100);
								} else if (35 <= i && i < 58) {
									hdl.postDelayed(this, 100 + i);
								} else {
									hdl.postDelayed(this, 160);
								}

							} else {
								isShaking = false;
								i = 0;
								seZiiv.setImageResource(sourceIds[(int) (Math.random() * 6)]);
								hdl.removeCallbacks(this);
							}
						}
					});
				}
			}
		});
	}

	/**
	 * 开启震动
	 * 
	 * @return
	 */
	public boolean startVibrato() { // 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1则从pattern的指定下标开始重复
		try {
			PlaySound.play("sound/shake_sound_male.mp3", assetManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 百度广告监听器
	 * 
	 * @author minggo
	 * @time 2014-6-7 S下午6:03:45
	 */
	/*private class AdListener extends BaiduAdListener {

		@Override
		public void onAdReady(AdView arg0) {
			super.onAdReady(arg0);
			System.out.println("准备显示");
			adView.setVisibility(View.VISIBLE);
			hdl.postDelayed(new Runnable() {

				@Override
				public void run() {
					cancelAdIv.setVisibility(View.VISIBLE);
					hdl.removeCallbacks(this);
				}
			}, 1500);

		}

		@Override
		public void onAdClick(JSONObject arg0) {
			super.onAdClick(arg0);
			System.out.println("点击");
			cancelAdIv.setOnClickListener(FragmentGame.this);
			// 统计用户点击删除广告
			StatService.onEvent(activity, "click_baidu_ad", telephone + "_" + new MinggoDate().toString());
		}

		@Override
		public void onAdSwitch() {
			super.onAdSwitch();
			System.out.println("切换");
			adView.setVisibility(View.VISIBLE);
			cancelAdIv.setVisibility(View.VISIBLE);
		}

	}*/

	@Override
	public void onResume() {
		if (mShakeListener != null) {
			mShakeListener.start();
		}
		super.onResume();

	}

	@Override
	public void onPause() {

		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		super.onPause();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.game_shack_iv:
			// 统计用户点击删除广告
			StatService.onEvent(activity, "hit_sezi", telephone + "_" + new MinggoDate().toString());
			if (isShaking) {
				i=51;
			}
			break;
		case R.id.cancel_ad_iv:
			// 统计用户点击删除广告
			StatService.onEvent(activity, "cancel_baidu_ad", telephone + "_" + new MinggoDate().toString());
			//adView.setVisibility(View.GONE);
			cancelAdIv.setVisibility(View.GONE);
			break;
		case R.id.bt_sezi_jiawu:
			if (!isShaking) {
				changejiawu = true;
				seZiiv.setImageResource(sourceIds[0]);
				
				shuaiDianziBt.setSelected(false);
				shuaijiawuBt.setSelected(true);
			}
			
			break;
		case R.id.bt_sezi_dian:
			if (!isShaking) {
				changejiawu = false;
				seZiiv.setImageResource(sourceIds2[0]);
				shuaiDianziBt.setSelected(true);
				shuaijiawuBt.setSelected(false);
			}
			break;
		default:
			break;
		}
	}
}
