package cn.wehax.common.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

/**
 * Created by howe on 15/2/5.
 * Email:howejee@gmail.com
 */
public class UpdateManager {

    public static void downloadApk(
            final Activity context,
            String url,
            String fileName,
            String showTitleStr,
            String showDescStr){
        final DownloadManager downloadManager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        url = "http://download.nvshengpai.com/android/beijing/app-baidu-debug.apk";
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS , fileName);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE |
                        DownloadManager.Request.NETWORK_WIFI);

        request.setAllowedOverRoaming(true);

        request.setMimeType("application/vnd.android.package-archive");

        request.setTitle(showTitleStr);
        request.setDescription(showDescStr);


        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);



        final long id = downloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent == null){
                    return;
                }

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
                if(id == downloadId){
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    Uri downloadFileUri = downloadManager
                            .getUriForDownloadedFile(downloadId);
                    install.setDataAndType(downloadFileUri,
                            "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
            }
        };

        context.registerReceiver(receiver,filter);


    }
}
