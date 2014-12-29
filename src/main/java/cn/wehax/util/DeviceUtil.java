package cn.wehax.util;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	private final static String PREFS_FILE = "device_info.xml";
	private final static String PREFS_DEVICE_ID = "device_id";
	private final static String PREFS_DEVICE_UUID = "device_uuid";
	private final static String PREFS_DEVICE_MAC = "device_mac";

	private static String deviceUuid = null;
	private static String deviceId = null;
	private static String deviceMac = null;

	/**
	 * Returns a unique UUID for the current android device.
	 * 
	 * @see
	 * 
	 * @return a UUID that may be used to uniquely identify your device for most
	 *         purposes.
	 */
	public static String getDeviceUuid(Context context) {
		if (deviceUuid == null) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			final String prefUuid = prefs.getString(PREFS_DEVICE_UUID, null);
			if (prefUuid != null) {
				deviceUuid = prefUuid;
			} else {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String tmDevice = "" + tm.getDeviceId();
				String tmSerial = "" + tm.getSimSerialNumber();
				String androidId = ""
						+ android.provider.Settings.Secure.getString(
								context.getContentResolver(),
								android.provider.Settings.Secure.ANDROID_ID);
				UUID uuid = new UUID(androidId.hashCode(),
						((long) tmDevice.hashCode() << 32)
								| tmSerial.hashCode());
				deviceUuid = uuid.toString();
				prefs.edit().putString(PREFS_DEVICE_UUID, deviceUuid).commit();
			}
		}
		return deviceUuid;
	}

	/**
	 * 获取手机设备ID
	 */
	public static String getDeviceId(Context context) {
		if (deviceId == null) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			final String prefId = prefs.getString(PREFS_DEVICE_ID, null);
			if (prefId != null) {
				deviceId = prefId;
			} else {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = tm.getDeviceId();
				prefs.edit().putString(PREFS_DEVICE_ID, deviceId).commit();
			}
		}
		return deviceId;
	}

	/**
	 * 获取设备Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceMAC(Context context) {
		if (deviceMac == null) {
			final SharedPreferences prefs = context.getSharedPreferences(
					PREFS_FILE, 0);
			final String prefMac = prefs.getString(PREFS_DEVICE_MAC, null);
			if (prefMac != null) {
				deviceMac = prefMac;
			} else {
				WifiManager wifi = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				deviceMac = info.getMacAddress();

				prefs.edit().putString(PREFS_DEVICE_MAC, deviceMac).commit();
			}
		}
		return deviceMac;
	}
}
