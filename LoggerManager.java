package com.fred.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoggerManager {
	private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
	private static Boolean MYLOG_WRITE_TO_FILE = false;// 日志写入文件开关
	private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
	private static String MYLOG_PATH_SDCARD_DIR;// 日志文件在sdcard中的路径
	private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
	private static String MYLOGFILEName = ".log";// 本类输出的日志文件名称
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 日志的输出格式
	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
	private static File file;

	/**
	 * 获取SD卡根目录
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		String sdDirStr = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			sdDirStr = sdDir.toString();
		}
		return sdDirStr;
	}

	/**
	 * 获取打印信息所在方法名，行号等信息
	 * 
	 * @return
	 */
	private static String[] getAutoJumpLogInfos() {
		String[] infos = new String[] { "", "", "" };
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length < 5) {
			Log.e("MyLogger", "Stack is too shallow!!!");
			return infos;
		} else {
			infos[0] = elements[4].getClassName().substring(
					elements[4].getClassName().lastIndexOf(".") + 1);
			infos[1] = elements[4].getMethodName() + "()";
			infos[2] = " at (" + elements[4].getClassName() + ".java:"
					+ elements[4].getLineNumber() + ")";
			return infos;
		}
	}

	/**
	 * 警告信息
	 * 
	 * @param msg
	 */
	public static void w(Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(infos[0], getLogStr(msg) + "------" + infos[1] + infos[2], 'w');
	}

	public static void w(String tag, Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(tag, getLogStr(msg) + "------" + infos[0] + ":" + infos[1]
				+ infos[2], 'w');
	}

	/**
	 * 错误信息
	 * 
	 * @param msg
	 */

	public static void e(Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(infos[0], getLogStr(msg) + "------" + infos[1] + infos[2], 'e');
	}

	public static void e(String tag, Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(tag, getLogStr(msg) + "------" + infos[0] + ":" + infos[1]
				+ infos[2], 'e');
	}

	/**
	 * 调试信息
	 * 
	 * @param msg
	 */
	public static void d(Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(infos[0], getLogStr(msg) + "------" + infos[1] + infos[2], 'd');
	}

	public static void d(String tag, Object msg) {// 调试信息
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(tag, getLogStr(msg) + "------" + infos[0] + ":" + infos[1]
				+ infos[2], 'd');
	}

	/**
	 * 通告信息
	 * 
	 * @param msg
	 */
	public static void i(Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(infos[0], getLogStr(msg) + "------" + infos[1] + infos[2], 'i');
	}



	public static void i(String tag, Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(tag, getLogStr(msg) + "------" + infos[0] + ":" + infos[1]
				+ infos[2], 'i');
	}

	/**
	 * 详细信息
	 * 
	 * @param msg
	 */
	public static void v(Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(infos[0], getLogStr(msg) + "------" + infos[1] + infos[2], 'v');
	}

	public static void v(String tag, Object msg) {
		if (msg == null) {
			return;
		}
		String[] infos = getAutoJumpLogInfos();
		log(tag, getLogStr(msg) + "------" + infos[0] + ":" + infos[1]
				+ infos[2], 'v');
	}

	/**
	 * 根据tag, msg和等级，输出日志
	 * 
	 * @param tag
	 * @param msg
	 * @param level
	 * @return void
	 * @since v 1.0
	 */
	private static void log(String tag, String msg, char level) {
		if (MYLOG_SWITCH) {
			if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
				Log.e(tag, msg);
			} else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.w(tag, msg);
			} else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.d(tag, msg);
			} else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.i(tag, msg);
			} else {
				Log.v(tag, msg);
			}
			if (MYLOG_WRITE_TO_FILE)
				writeLogtoFile(String.valueOf(level), tag, msg);
		}
	}

	private static String getLogStr(Object msg) {
		String logStr;
		if(msg instanceof Byte[]){
			logStr = new String((byte[]) msg);
		}else{
			logStr = msg.toString();
		}
		return logStr;
	}

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
		MYLOG_PATH_SDCARD_DIR = getSDPath() + "/lefthand/log";
		Date nowtime = new Date();
		String needWriteFiel = logfile.format(nowtime);
		String needWriteMessage = myLogSdf.format(nowtime) + ":" + mylogtype
				+ ":" + tag + ":" + text;
		File filepath = new File(getSDPath() + "/lefthand/log");
		if (!filepath.exists() && !filepath.isDirectory()) {
			filepath.mkdir();

		} else {
			Environment.getExternalStorageDirectory();
			file = new File(MYLOG_PATH_SDCARD_DIR, "log-lefthand-"
					+ needWriteFiel + MYLOGFILEName);
		}
		try {
			FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除制定的日志文件
	 * */
	public static void delFile() {// 删除日志文件
		MYLOG_PATH_SDCARD_DIR = getSDPath() + "/lefthand/log";
		String needDelFiel = logfile.format(getDateBefore());
		File file = new File(MYLOG_PATH_SDCARD_DIR, needDelFiel + MYLOGFILEName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
	 * */
	private static Date getDateBefore() {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE)
				- SDCARD_LOG_FILE_SAVE_DAYS);
		return now.getTime();
	}
}
