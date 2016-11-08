package com.betaonly.transactionviewer;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DebugLogger {

	private static final String FILE_DATE_FORMAT = "dd-MMM-yyyy-HH-mm";
	private static final String LOG_DATE_FORMAT = "dd MMM yyyy h:mm a";

	private static String TAG = "TransactionViewer";
	public static boolean isDebug = BuildConfig.DEBUG;
	private static boolean isPrintStackTrace = false;
	public static boolean isLogToFile = false;
	private static String currentLogFile;

	/**
     * Send a Extreme easy to read message
     * @param msg The message you would like logged.
     */
    public static void wtf(String msg) {
    	if (isDebug)
    		Log.e(TAG, buildMessage("---------------->" + msg));
    }
    /**
     * Send a VERBOSE log message.
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
    	if (isDebug)
    		Log.v(TAG, buildMessage(msg));
    }

    /**
     * Send a VERBOSE log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void v(String msg, Throwable thr) {
    	if (isDebug)
    		Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a DEBUG log message.
     * @param msg
     */
    public static void d(String msg) {
    	if (isDebug)
    		Log.d(TAG, buildMessage(msg));
    }

    /**
     * Send a DEBUG log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void d(String msg, Throwable thr) {
    	if (isDebug)
    		Log.d(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an INFO log message.
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
    	if (isDebug)
    		Log.i(TAG, buildMessage(msg));
    }

    /**
     * Send a INFO log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void i(String msg, Throwable thr) {
    	if (isDebug)
    		Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an ERROR log message.
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
    	if (isDebug)
    		Log.e(TAG, buildMessage(msg));
    }

    /**
     * Send an ERROR log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void e(String msg, Throwable thr) {
    	if (isDebug)
    		Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a WARN log message
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
    	if (isDebug)
    		Log.w(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log message and log the exception.
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void w(String msg, Throwable thr) {
    	if (isDebug)
    		Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an empty WARN log message and log the exception.
     * @param thr An exception to log
     */
    public static void w(Throwable thr) {
    	if (isDebug)
    		Log.w(TAG, buildMessage(""), thr);
    }

    public static void logToFile(String message) {
    	i(message);
    	if (isLogToFile && isDebug) {
    		if (currentLogFile == null) {
    			generateNewLogFile();
    		}
	    	File root = new File(Environment.getExternalStorageDirectory(), "WeMail/Log");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File logFile = new File(root, currentLogFile);
	        if (!logFile.exists()) {
	        	try {
					logFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        try {
	        	// BufferedWriter for performance, true to set append to file flag
	        	BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
	        	buf.append("[" + dateToString(new Date(), LOG_DATE_FORMAT) + "] " + message);
	        	buf.newLine();
	        	buf.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
    	}
    }

    public static String generateNewLogFile() {
    	currentLogFile = dateToString(new Date(), FILE_DATE_FORMAT)
    		+ "_" + System.currentTimeMillis() + ".txt";
    	return currentLogFile;
    }

    /**
     * Building Message
     * @param msg The message you would like logged.
     * @return Message the formated message
     */
	protected static String buildMessage(String msg) {
		StackTraceElement[] traces =  new Throwable().fillInStackTrace().getStackTrace();
		StackTraceElement caller = traces[2];

        StringBuilder message =  new StringBuilder();
		if (isPrintStackTrace) {
			for (int i = traces.length - 1; i >= 3; i--) {
				message.append(" | ")
						.append(traces[i].getClassName().substring(traces[i].getClassName().lastIndexOf(".") + 1))
						.append(".")
						.append(traces[i].getMethodName())
						.append("\n");
			}
		}
		message.append(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1))
				.append(".")
				.append(caller.getMethodName())
				.append("(): ")
				.append(msg);
		return message.toString();
	}
	
	public static String dateToString(Date date, String format) {
		Locale locale = Locale.getDefault();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
//		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		dateFormat.setTimeZone(TimeZone.getDefault());
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
}
