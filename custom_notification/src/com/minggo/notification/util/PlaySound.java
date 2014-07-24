package com.minggo.notification.util;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
/**
 * 声音播放工具 
 * @author minggo
 * @date 2013-8-7上午09:17:00
 */
public class PlaySound {
	private static MediaPlayer player;
	private static PlaySound playSound;
	private static boolean flag;
	
	private PlaySound(){
		
	}
	
	public static PlaySound getInstance(){
		if (playSound==null) {
			playSound = new PlaySound();
			
		}
		return playSound;
	}
	/**
	* 调用方式:play("across.mp3",getResources().getAssets());
	* @param filename 要播放的音频文件名
	* @param asm AssetManager
	* @throws IOException
	*/
	public static final void play(String filename,AssetManager asm) throws IOException{
		if (!flag) {
			flag = true;
			AssetFileDescriptor afd = asm.openFd(filename);
			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			player.prepare();
			player.start();
			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					flag = false;
				}
			});
		}
		
	}
	
	/**
	 * 根据录音的路径播放
	 * @param soundpath
	 * @param asm
	 * @param finishListen
	 * @param type 1系统用户 2：自定义用户
	 * @throws IOException
	 */
	public static void playVoice(String soundpath,final AssetManager asm,FinishListen finishListen,int type) throws IOException{
		
		final FinishListen finishListenT = finishListen;
		
		player = new MediaPlayer();
		
		if (type==1) {
			AssetFileDescriptor afd = asm.openFd(soundpath);
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
		}else if (type==2) {
			player.setDataSource(soundpath);
		}
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				MediaPlayer player = new MediaPlayer();
				
				AssetFileDescriptor afd = null;
				try {
					afd = asm.openFd("sound/qrcode_completed.mp3");
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					player.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.start();
				
				player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						
						if (finishListenT!=null) {
							finishListenT.onFinish();
						}
					}
				});
			}
		});
	}
	/**
	 * 根据录音的路径播放[不带开始和结束音]
	 * @param soundpath
	 * @param asm
	 * @param finishListen
	 * @param type 1系统用户 2：自定义用户
	 * @throws IOException
	 */
	public static void playVoice2(String soundpath,final AssetManager asm,int type) throws IOException{
		
		
		player = new MediaPlayer();
		
		if (type==1) {
			AssetFileDescriptor afd = asm.openFd(soundpath);
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
		}else if (type==2) {
			player.setDataSource(soundpath);
		}
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}
	
	
	
	
	public static void stopVoice(){
		if (player!=null) {
			//player.stop();
			player.release();
		}
	}
	
	public interface FinishListen{
		public void onFinish();
	}
}
