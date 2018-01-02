package com.lenovo.tvflowrecyclerview;

import android.util.Log;

public final class LogUtil {
	//控制项目是否显示log
	private static boolean isShowLog = true;
	private static String TAG = "appUpgrade_swj_";

	public static void i(String tag,String msg){
		if (isShowLog){
			Log.i(TAG +tag,msg);
		}
	}

	public static void i(Object tag,String msg){
		if (isShowLog){
			Log.i(TAG +tag.getClass().getSimpleName(),msg);
		}
	}
	public static void d(String tag,String msg){
		if (isShowLog){
			Log.d(TAG +tag, msg);
		}
	}

	public static void d(Object tag,String msg){
		if (isShowLog){
			Log.d(TAG +tag.getClass().getSimpleName(), msg);
		}
	}
	public static void w(String tag,String msg){
		if (isShowLog){
			Log.w(TAG +tag, msg);
		}
	}

	public static void w(Object tag,String msg){
		if (isShowLog){
			Log.w(TAG +tag.getClass().getSimpleName(), msg);
		}
	}
	public static void e(String tag,String msg){
		if (isShowLog){
			Log.e(TAG +tag, msg);
		}
	}

	public static void e(Object tag,String msg){
		if (isShowLog){
			Log.e(TAG +tag.getClass().getSimpleName(), msg);
		}
	}
}
