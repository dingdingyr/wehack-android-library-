package cn.wehax.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by howe on 14/12/29.
 * Email:howejee@gmail.com
 */
public class RequestHeaderUtil {

    private static final String NONE = "none";

    /**
     * 获取token
     * @param token
     * @return
     */
    public static String getHeaderCookieToken(String token){
        //TODO:获取token
        return "uid/"+token;
    }


    /**
     * 生成请求头 User-Agent 数据
     */
    public static String getHeaderUserAgent(Context context){
        //User-agent:hpclient/平台/版本号/版本内部号/渠道号/设备id/推送id/ip

        StringBuffer sb = new StringBuffer();
        sb.append("hpclient/android");

        sb.append("/");
        sb.append(getVersion(context));

        sb.append("/");
        sb.append(getInnerVersionCode(context));

        sb.append("/");
        sb.append(getChannelId());

        sb.append("/");
        sb.append(DeviceUtil.getDeviceId(context));

        sb.append("/");
        sb.append(getPushId());

        sb.append("/");
        sb.append(NetworkUtil.getIpAddress());

        return sb.toString();
    }

    /**
     * 获取版本号
     */
    private static String getVersion(Context context){
        String version = NONE;
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {

        }
        return version;
    }


    /**
     * 获取内部版本号
     */
    private static String getInnerVersionCode(Context context){
        String versionCode = NONE;
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {

        }
        return versionCode;
    }


    /**
     * 获取渠道号
     */
    private static String getChannelId(){
        //TODO: 获取渠道号
        return NONE;
    }

    /**
     * 获取推送
     */
    private static String getPushId(){
        //TODO:获取推送id
        return NONE;
    }


}
