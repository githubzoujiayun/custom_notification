package com.minggo.notification.util;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minggo.notification.dao.DBConfig;
import com.minggo.notification.dao.DaoUtils;
import com.minggo.notification.dao.DbOpenHelper;
import com.minggo.notification.model.User;

/**
 * 录音数据库操作类
 * @author minggo
 * @time 2014-6-23 S下午9:48:30
 */
public class UserUtil {
	
	/**
	 * 保存录音
	 * @param context
	 * @param user
	 */
	public static void saveUser(Context context,User user){
		try {
			SQLiteDatabase db = new DbOpenHelper(context).getWritableDatabase();
			db.replace(DBConfig.TABLE_USER, null, DaoUtils.object2ContentValues(user));
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	/**
	 * 获取录音列表
	 * @param context
	 * @return
	 */
	public static List<User> getUserList(Context context){
		List<User> userList = null;
		try {
			SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
			Cursor query = db.query(DBConfig.TABLE_USER,null,null,null,null, null, null);
			userList = DaoUtils.cursor2ObjectList(query, User.class);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userList;
	}
	/**
	 * 判断是否注册过
	 * @param context
	 * @return
	 */
	public static boolean isExistUser(Context context){
		List<User> userList = null;
		try {
			SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
			Cursor query = db.query(DBConfig.TABLE_USER,null,"type=?",new String[]{"1"},null, null, null);
			userList = DaoUtils.cursor2ObjectList(query, User.class);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (userList!=null&&!userList.isEmpty()) {
			return true;
		}
		return false;
	}
	/**
	 * 判断是否注册过
	 * @param context
	 * @return
	 */
	public static User getFirstUser(Context context){
		List<User> userList = null;
		try {
			SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
			Cursor query = db.query(DBConfig.TABLE_USER,null,"type=?",new String[]{"1"},null, null, null);
			userList = DaoUtils.cursor2ObjectList(query, User.class);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (userList!=null&&!userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * 获取某一时间的录音
	 * @param email
	 * @return
	 */
	public static User getUser(Context context,String email){
		List<User> userList = null;
		try {
			SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
			Cursor query = db.query(DBConfig.TABLE_USER,null,"email=?",new String[]{email},null, null, null);
			userList = DaoUtils.cursor2ObjectList(query, User.class);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (userList!=null&&!userList.isEmpty()) {
			return userList.get(0);
		}
		return null;
	}
}
