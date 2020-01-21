package com.noavaran.system.vira.baryab.utils.uncaughtexception;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.util.Log;

import com.noavaran.system.vira.baryab.activities.CrashActivity;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private ExceptionInfo info;

	private Context myContext;
	private String versionName;
	private String packageName;
	private String filePath;
	private String phoneModel;
	private String androidVersion;
	private String board;
	private String brand;

	// String CPU_ABI;
	private String device;
	private String display;
	private String fingerPrint;
	private String host;
	private String ID;
	private String manufacturer;
	private String model;
	private String product;
	private String tags;
	private long time;
	private String type;
	private String user;

	private static String TAG = "Crash Analytics";

	public ExceptionHandler(Context context) {
		myContext = context;
		info = new ExceptionInfo();
	}

	/**
	 * Append the error log with phone & app details
	 */
	public void uncaughtException(Thread thread, Throwable exception) {
		recoltInformations(myContext);

		Date curDate = new Date();
		info.setExceptionDate(curDate.toString());
		info.setExceptionMessage(exception.getMessage());
		createInformationString();

		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		exception.printStackTrace(printWriter);
		String stacktrace = result.toString();
		info.setStacktrace(stacktrace);

		printWriter.close();

		Intent intent = new Intent(myContext, CrashActivity.class);
		intent.putExtra("STACKTRACE", info.toString());

		myContext.startActivity(intent);

		Process.killProcess(Process.myPid());
		System.exit(10);
	}

	/**
	 * It gets the android phone & application details
	 *
	 * @param context
	 */
	void recoltInformations(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;

			// Package name
			packageName = pi.packageName;

			// Files dir for storing the stack traces
			filePath = context.getFilesDir().getAbsolutePath();

			// Device model
			phoneModel = android.os.Build.MODEL;

			// Android version
			androidVersion = android.os.Build.VERSION.RELEASE;

			board = android.os.Build.BOARD;
			brand = android.os.Build.BRAND;

			// CPU_ABI = android.os.Build.;
			device = android.os.Build.DEVICE;
			display = android.os.Build.DISPLAY;
			fingerPrint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			ID = android.os.Build.ID;

			// Manufacturer = android.os.Build.;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			tags = android.os.Build.TAGS;
			time = android.os.Build.TIME;
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * It creates a complete string of about the android phone + app
	 * information.
	 *
	 * @return
	 */
	public void createInformationString() {
		info.setVersionName(versionName);
		info.setPackageName(packageName);
		info.setPhoneModel(phoneModel);
		info.setAndroidVersion(androidVersion);
		info.setBoard(board);
		info.setBrand(brand);
		info.setDevice(device);
		info.setDisplay(display);
		info.setFingerPrint(fingerPrint);
		info.setHost(host);
		info.setId(ID);
		info.setModel(model);
		info.setProduct(product);
		info.setTags(tags);
		info.setTime(String.valueOf(time));
		info.setType(type);
		info.setUser(user);
		info.setTotalInternalMemorySize(String.valueOf(getTotalInternalMemorySize()));
		info.setAvailableInternalMemorySize(String.valueOf(getAvailableInternalMemorySize()));
	}

	/**
	 * Find the available internal memory
	 * 
	 * @return available internal memory
	 */
	public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Find the total internal memory
	 * 
	 * @return total internal memory
	 */
	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}
}