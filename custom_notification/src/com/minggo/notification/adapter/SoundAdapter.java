package com.minggo.notification.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.minggo.love.notification.R;
import com.minggo.notification.model.SoundRecord;
/**
 * 声音列表
 * @author minggo
 * @time 2014-6-23 S下午11:17:01
 */
public class SoundAdapter extends BaseAdapter {

	private Context context;
	private List<SoundRecord> list;
	private TryListener tryListener;
	
	public SoundAdapter(Context context,List<SoundRecord> list,TryListener tryListener){
		this.context = context;
		this.list = list;
		this.tryListener = tryListener;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sound_list, null);
			viewHolder.timeTv = (TextView) convertView.findViewById(R.id.tv_user_and_time);
			viewHolder.secondTv = (TextView) convertView.findViewById(R.id.tv_second);
			viewHolder.tryListenIb = (ImageButton) convertView.findViewById(R.id.ib_try_listen);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (list.get(position).whichHour<10) {
			viewHolder.timeTv.setText("0"+list.get(position).whichHour+":00");
		}else{
			viewHolder.timeTv.setText(list.get(position).whichHour+":00");
		}
		if (list.get(position).type==2) {
			viewHolder.secondTv.setVisibility(View.VISIBLE);
			viewHolder.secondTv.setText(list.get(position).longTime);
		}else{
			viewHolder.secondTv.setVisibility(View.GONE);
		}
		viewHolder.tryListenIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				tryListener.onTryClick(v,position);
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView timeTv;
		ImageButton tryListenIb;
		TextView secondTv;
	}
	
	public interface TryListener{
		public void onTryClick(View v,int position);
	}
}
