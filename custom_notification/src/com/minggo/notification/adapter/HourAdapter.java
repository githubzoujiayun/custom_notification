package com.minggo.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minggo.love.notification.R;
/**
 * 时间列表适配器
 * @author minggo
 * @date 2014-6-27 下午1:19:21
 */
public class HourAdapter extends BaseAdapter {

	private Context context;
	private String[] hours;
	
	public HourAdapter(Context context,String[] hours){
		this.context = context;
		this.hours = hours;
	}
	
	@Override
	public int getCount() {
		return hours.length;
	}

	@Override
	public Object getItem(int position) {
		return hours[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_hours, null);
			viewHolder.hourTv = (TextView) convertView.findViewById(R.id.tv_hour);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.hourTv.setText(hours[position]);
		return convertView;
	}
	
	private class ViewHolder{
		TextView hourTv;
		
	}
	
	public interface TryListener{
		public void onTryClick(View v,int position);
	}
}
