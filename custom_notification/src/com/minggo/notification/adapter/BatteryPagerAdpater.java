package com.minggo.notification.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * 首页的viewpager适配器
 * 
 * @author minggo
 * @date 2014-2-24 上午11:20:01
 */
public class BatteryPagerAdpater extends FragmentStatePagerAdapter {

	private List<Fragment> fragmentList;
	
	public BatteryPagerAdpater(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	/**
	 * 得到每个页面
	 */
	 @Override 
	 public Fragment getItem(int arg0) {
		 return (fragmentList ==null || fragmentList.size() == 0) ? null: fragmentList.get(arg0); 
	 }
	 

	/**
	 * 页面的总个数
	 */
	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}

	
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		Log.i("pager", "No."+position+"被删除了");
	}

}
