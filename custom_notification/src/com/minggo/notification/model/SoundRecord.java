package com.minggo.notification.model;

import com.minggo.notification.annotation.Primarykey;

/**
 * 录音model
 * @author minggo
 * @date 2014-7-3 下午2:36:52
 */
public class SoundRecord {
	
	@Primarykey
	public int whichHour;
	/**
	 * 保存路劲
	 */
	public String path;
	/**
	 * 录音时间
	 */
	public String longTime;
	/**
	 * 录音类型1：系统,2:用户
	 */
	public int type;
}
