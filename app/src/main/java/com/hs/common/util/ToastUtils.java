package com.hs.common.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.hs.base.MyApplication;


public class ToastUtils {
	private static Toast toast = null;

	
	public static void showCenter(Context context, String message, int duration){
		toast = Toast.makeText(context, message, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static void showLongCenter(Context context, String message){
		showCenter(context, message, Toast.LENGTH_LONG);
	}
	
	public static void showCenter(Context context, String message){
		showCenter(context, message, Toast.LENGTH_SHORT);
	}
	
	public static void showLongCenter(Context context, int message){
		showCenter(context, message, Toast.LENGTH_LONG);
	}
	
	public static void showCenter(Context context, int message){
		showCenter(context, message, Toast.LENGTH_SHORT);
	}
	
	public static void showCenter(Context context, int message, int duration){
		toast = Toast.makeText(context, message, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void hide(){
		if(toast != null){
			toast.cancel();
		}
	}
	
	public static void showCenter(String message){
		showCenter(MyApplication.getContext(), message);
	}

}
