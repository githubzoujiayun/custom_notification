package com.minggo.notification.model;

import com.minggo.notification.annotation.Primarykey;

/**
 * 用户表
 * @author minggo
 * @date 2014-7-3 下午2:37:03
 */
public class User {
	
	@Primarykey
	public String email;
	public String name;
	public String telephone;
	public String password;
	/**
	 * 使用自定义录音1：是 0：不是
	 */
	public int useDefineSound;
	/**
	 * 使用自定义心情提醒1:是0:否
	 */
	public int useDefinFeeling;
	/**
	 * 用户类型1：注册用户 0：系统用户
	 */
	public int type;
	
	
}
