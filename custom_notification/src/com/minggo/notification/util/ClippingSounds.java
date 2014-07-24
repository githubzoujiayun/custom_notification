package com.minggo.notification.util;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.os.Environment;

/**
 * 剪切声音
 * 
 * @author minggo
 * @date 2014-6-23 下午1:29:11
 */
public class ClippingSounds {
	public static final String TALKSOUND_FILE = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/minggo.battery/sounds/";

	public static String talkSoundName;

	public static String saveSounds() {
		File myCaptureFile;
		try {
			myCaptureFile = new File(TALKSOUND_FILE);
			if (!myCaptureFile.exists())
				myCaptureFile.mkdirs();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return TALKSOUND_FILE + saveTalkSoundsFileNames();
	}

	/**
	 * 做剪裁准备
	 * 
	 * @return
	 */
	public static String saveSounds2() {

		File myCaptureFile;
		try {

			myCaptureFile = new File(TALKSOUND_FILE);
			if (!myCaptureFile.exists())
				myCaptureFile.mkdirs();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return TALKSOUND_FILE + saveTalkSoundsFileNames();

	}

	/**
	 * 保存文件的文件名设置
	 * 
	 * @return
	 */
	public static String saveTalkSoundsFileNames() {

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		talkSoundName = dateFormat.format(date) + "_sound" + ".mp3";
		return talkSoundName;
	}
}
