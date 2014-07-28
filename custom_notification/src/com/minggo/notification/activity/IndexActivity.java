package com.minggo.notification.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.minggo.love.notification.R;
import com.minggo.notification.adapter.BatteryPagerAdpater;
import com.minggo.notification.fragment.FragmentFeelingSetting;
import com.minggo.notification.fragment.FragmentGame;
import com.minggo.notification.fragment.FragmentTimeSetting;
import com.minggo.notification.service.BatteryService;
import com.minggo.notification.service.LoveNotificationApplication;
import com.minggo.notification.util.ImageUtils;

/**
 * 首页
 * 
 * @author minggo
 * @time 2014-6-16 S下午9:15:33
 */
public class IndexActivity extends FragmentActivity implements OnClickListener {

	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private ImageView naviBottomIv;
	private int currIndex = 1;// 当前页卡编号
	private int defaultIndex = 1;// 默认的第一页面

	private int bmpW;// 动画图片宽度
	public ViewPager viewPager;
	private BatteryPagerAdpater pagerAdpater;

	private Button feelingBt;
	private Button seziBt;
	private Button alertBt;
	private FragmentFeelingSetting feelingSettingFgm;
	private FragmentGame gameFgm;
	private FragmentTimeSetting timeSettingFgm;

	private ImageButton menuBt;
	private View menuView;
	private View loginView;
	private View settingView;
	private View shareView;
	private View feedbackView;
	private View personalView;

	private boolean isFirst;// 判断是不是第一次打开运用
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		this.startService(new Intent(this, BatteryService.class));

		preferences = getSharedPreferences("first", Context.MODE_PRIVATE);
		isFirst = preferences.getBoolean("isfrist", true);
		makeShortCut(isFirst);

		initView();

		LoveNotificationApplication.allActivities.add(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {

		initImageView();

		viewPager = (ViewPager) findViewById(R.id.index_viewPager);
		alertBt = (Button) findViewById(R.id.bt_index_alert);
		seziBt = (Button) findViewById(R.id.bt_index_seii);
		feelingBt = (Button) findViewById(R.id.bt_index_feeling);
		menuView = findViewById(R.id.lo_menu);
		loginView = findViewById(R.id.lo_menu_1);
		settingView = findViewById(R.id.lo_menu_2);
		shareView = findViewById(R.id.lo_menu_3);
		feedbackView = findViewById(R.id.lo_menu_4);
		personalView = findViewById(R.id.lo_menu_5);
		
		menuBt = (ImageButton) findViewById(R.id.bt_index_menu);

		feedbackView.setOnClickListener(this);
		loginView.setOnClickListener(this);
		settingView.setOnClickListener(this);
		shareView.setOnClickListener(this);
		personalView.setOnClickListener(this);
		alertBt.setOnClickListener(this);
		seziBt.setOnClickListener(this);
		feelingBt.setOnClickListener(this);
		menuBt.setOnClickListener(this);
		timeSettingFgm = new FragmentTimeSetting();
		gameFgm = new FragmentGame();
		feelingSettingFgm = new FragmentFeelingSetting();

		fragmentList.add(timeSettingFgm);
		fragmentList.add(gameFgm);
		fragmentList.add(feelingSettingFgm);

		pagerAdpater = new BatteryPagerAdpater(getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(pagerAdpater);
		viewPager.setCurrentItem(currIndex);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOffscreenPageLimit(2);

		StatService.onPageStart(this, getFragment(currIndex));
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int position) {
		}

		public void onPageScrolled(int position, float arg1, int arg2) {

		}

		public void onPageSelected(int position) {
			StatService.onPageEnd(IndexActivity.this, getFragment(currIndex));
			StatService.onPageStart(IndexActivity.this, getFragment(position));
			translate(position);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	/**
	 * 获取fragment名字
	 * 
	 * @param index
	 * @return
	 */
	private String getFragment(int index) {

		String labelId = "";
		switch (index) {
		case 0:
			labelId = "page_alert";
			break;
		case 1:
			labelId = "page_game";
			break;
		case 2:
			labelId = "page_feeling";
			break;

		default:
			break;
		}
		return labelId;

	}

	/**
	 * 导航底层图片移动当当前选择页面
	 * 
	 * @param tab
	 */
	private void translate(final int tab) {

		float startX = bmpW * (currIndex - defaultIndex);
		float endX = bmpW * (tab - defaultIndex);

		alertBt.setSelected(tab == 0 ? true : false);
		seziBt.setSelected(tab == 1 ? true : false);
		feelingBt.setSelected(tab == 2 ? true : false);

		Animation animation = new TranslateAnimation(startX, endX, 0, 0);
		currIndex = tab;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		naviBottomIv.startAnimation(animation);
	}

	/**
	 * 初始化导航条
	 */
	private void initImageView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度

		naviBottomIv = (ImageView) findViewById(R.id.cursor);
		// Bitmap bm = ((BitmapDrawable)naviBottomIv.getDrawable()).getBitmap();
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.index_navi_bottom);
		bm = ImageUtils.zoomBitmap(bm, screenW / 3, bm.getHeight());
		bmpW = bm.getWidth();// 获取图片宽度
		Matrix matrix = new Matrix();
		matrix.postTranslate(defaultIndex * (screenW / 3), 0);
		naviBottomIv.setImageMatrix(matrix);// 设置动画初始位置
		naviBottomIv.setImageBitmap(bm);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.lo_menu_5:
			startActivity(new Intent(this, OrderActivity.class));
			menuView.setVisibility(View.GONE);
			StatService.onEvent(this, "menu_order", "order_tips");
			break;
		case R.id.lo_menu_4:
			
			Intent intent4 = new Intent(Intent.ACTION_SENDTO);
			intent4.setType("text/plain");
			intent4.setData(Uri.parse("mailto:minggo8en@gmail.com"));
			intent4.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.email_title));
			intent4.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.email_content));
			startActivity(Intent.createChooser(intent4, this.getString(R.string.email_select_tips)));
			menuView.setVisibility(View.GONE);
			StatService.onEvent(this, "menu_send_email", "send_email");
			break;
		case R.id.lo_menu_3:
			Intent intent3 = new Intent(Intent.ACTION_SEND);
			intent3.setType("text/plain");
			intent3.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.share_title));
			intent3.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.share_content));
			startActivity(Intent.createChooser(intent3, this.getString(R.string.share_select_tips)));
			menuView.setVisibility(View.GONE);
			StatService.onEvent(this, "menu_share", "share_apk");
			break;
		case R.id.bt_index_menu:
			if (menuView.getVisibility() == View.GONE) {
				menuView.setVisibility(View.VISIBLE);
			} else {
				menuView.setVisibility(View.GONE);
			}
			
			break;
		case R.id.lo_menu_1:
			startActivity(new Intent(this, LoginActivity.class));
			menuView.setVisibility(View.GONE);
			StatService.onEvent(this, "menu_login", "user_login");
			break;
		case R.id.lo_menu_2:
			startActivity(new Intent(this, SettingActivity.class));
			menuView.setVisibility(View.GONE);
			StatService.onEvent(this, "menu_setting", "setting_application");
			break;
		case R.id.bt_index_alert:
			viewPager.setCurrentItem(0);
			break;
		case R.id.bt_index_seii:
			viewPager.setCurrentItem(1);
			break;
		case R.id.bt_index_feeling:
			viewPager.setCurrentItem(2);
			break;
		default:
			if (menuView.getVisibility() == View.GONE) {
				menuView.setVisibility(View.VISIBLE);
			} else {
				menuView.setVisibility(View.GONE);
			}
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPageEnd(IndexActivity.this, getFragment(currIndex));
	}
	
	/**
	 * 设置快捷键
	 * 
	 * @param isFirst
	 */
	private void makeShortCut(boolean isFirst) {

		if (isFirst) {
			Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			shortcutIntent.putExtra("duplicate", false);
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
			Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.logo);
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			sendBroadcast(shortcutIntent);
			// 设置不是第一次进入标尺
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("isfrist", false);
			editor.commit();
		}
	}
}
