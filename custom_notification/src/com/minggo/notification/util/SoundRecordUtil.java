package com.minggo.notification.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minggo.notification.dao.DBConfig;
import com.minggo.notification.dao.DaoUtils;
import com.minggo.notification.dao.DbOpenHelper;
import com.minggo.notification.model.SoundRecord;

/**
 * 录音数据库操作类
 * @author minggo
 * @time 2014-6-23 S下午9:48:30
 */
public class SoundRecordUtil {
	
	/**
	 * 保存录音
	 * @param context
	 * @param soundRecord
	 */
	public static void saveSubscriptionGiftType(Context context,SoundRecord soundRecord){
		SQLiteDatabase db = new DbOpenHelper(context).getWritableDatabase();
		db.replace(DBConfig.TABLE_SOUND_RECORD, null, DaoUtils.object2ContentValues(soundRecord));
		db.close();
	}
	/**
	 * 获取录音列表
	 * @param context
	 * @return
	 */
	public static List<SoundRecord> getSoundRecordList(Context context){
		List<SoundRecord> giftTypeList = null;
		SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
		Cursor query = db.query(DBConfig.TABLE_SOUND_RECORD,null,null,null,null, null, null);
		giftTypeList = DaoUtils.cursor2ObjectList(query, SoundRecord.class);
		db.close();
		return giftTypeList;
	}
	/**
	 * 根据类型获取录音列表
	 * @param context
	 * @param type 1：系统,2:用户
	 * @return
	 */
	public static List<SoundRecord> getSoundRecordList(Context context,int type){
		List<SoundRecord> soundRecords = null;
		SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
		Cursor query = db.query(DBConfig.TABLE_SOUND_RECORD,null,"type=?",new String[]{String.valueOf(type)},null, null, null);
		soundRecords = DaoUtils.cursor2ObjectList(query, SoundRecord.class);
		db.close();
		return soundRecords;
	}
	
	/**
	 * 获取某一时间的录音
	 * @param whichHour
	 * @return
	 */
	public static SoundRecord getSoundRecord(Context context,int whichHour){
		List<SoundRecord> soundRecords = null;
		SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
		Cursor query = db.query(DBConfig.TABLE_SOUND_RECORD,null,"whichHour=?",new String[]{String.valueOf(whichHour)},null, null, null);
		soundRecords = DaoUtils.cursor2ObjectList(query, SoundRecord.class);
		db.close();
		if (soundRecords!=null&&!soundRecords.isEmpty()) {
			return soundRecords.get(0);
		}
		return null;
	}
	/**
	 * 删除录音
	 * @param context
	 * @param soundRecord
	 * @return
	 */
	public static boolean deleteSound(Context context,SoundRecord soundRecord){
		
		SQLiteDatabase db = new DbOpenHelper(context).getReadableDatabase();
		int count = db.delete(DBConfig.TABLE_SOUND_RECORD, "whichHour=?", new String[]{String.valueOf(soundRecord.whichHour)});
		File file = new File(soundRecord.path);
		if (file.isFile()&&file.exists()) {
			file.delete();
		}
		if (count>0) {
			return true;
		}
		return false;
	}
}
