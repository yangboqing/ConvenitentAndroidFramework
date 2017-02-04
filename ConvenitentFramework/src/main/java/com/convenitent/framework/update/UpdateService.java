package com.convenitent.framework.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.convenitent.framework.R;
import com.convenitent.framework.app.$;
import com.convenitent.framework.http.DownloadUtils;
import com.convenitent.framework.utils.AppUtils;
import com.convenitent.framework.utils.LogUtils;
import com.convenitent.framework.utils.ToastUtils;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by yangboqing on 16/8/16.
 * 检查更新后台下载服务
 */

public class UpdateService extends Service {

    private static final int DOWNLOAD_STATE_FAILURE = -1;
    private static final int DOWNLOAD_STATE_SUCCESS = 0;
    private static final int DOWNLOAD_STATE_START = 1;
    private static final int DOWNLOAD_STATE_INSTALL = 2;
    private static final int DOWNLOAD_STATE_ERROR_SDCARD = 3;
    private static final int DOWNLOAD_STATE_ERROR_URL = 4;
    private static final int DOWNLOAD_STATE_ERROR_FILE = 5;

    private static final int NOTIFICATION_ID = 3956;
    private NotificationManager mNotificationManager = null;
    private PendingIntent mPendingIntent = null;
    private NotificationCompat.Builder mBuilder;

    private String mDownloadSDPath;
    private String mDownloadUrl;
    private String mPackageNames;
    private File mDestDir;
    private File mDestFile;

    private boolean mIsDownloading = false;

    private Handler.Callback mHandlerCallBack = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_STATE_SUCCESS:
                    Toast.makeText(getApplicationContext(), R.string.app_download_success, Toast.LENGTH_LONG).show();
                    install(mDestFile);
                    break;
                case DOWNLOAD_STATE_FAILURE:
                    Toast.makeText(getApplicationContext(), R.string.app_download_failure, Toast.LENGTH_LONG).show();
                    mNotificationManager.cancel(NOTIFICATION_ID);
                    break;
                case DOWNLOAD_STATE_START:
                    Toast.makeText(getApplicationContext(), R.string.app_download_start, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_INSTALL:
                    Toast.makeText(getApplicationContext(), R.string.app_download_install, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_ERROR_SDCARD:
                    Toast.makeText(getApplicationContext(), R.string.app_download_error_sdcard, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_ERROR_URL:
                    Toast.makeText(getApplicationContext(), R.string.app_download_error_url, Toast.LENGTH_LONG).show();
                    break;
                case DOWNLOAD_STATE_ERROR_FILE:
                    Toast.makeText(getApplicationContext(), R.string.app_download_error_file, Toast.LENGTH_LONG).show();
                    mNotificationManager.cancel(NOTIFICATION_ID);
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private Handler mHandler = new Handler(mHandlerCallBack);

    private DownloadUtils.DownloadCallBack mDownloadCallBack = new DownloadUtils.DownloadCallBack() {

        private int mCurrentProgress = 0;

        @Override
        public void onDownloading(int progress) {
            if (progress != mCurrentProgress || progress == 100) {
                    mCurrentProgress = progress;
                mBuilder.setProgress(100,mCurrentProgress,false);
                mBuilder.setContentText(getString(R.string.app_download_downloading) + progress + "%");
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }

        @Override
        public void onDownloaded() {
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setContentText(getText(R.string.app_download_notification_success));
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            if (mDestFile.exists() && mDestFile.isFile() && checkApkFile(mDestFile.getPath())) {
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_STATE_SUCCESS;
                mHandler.sendMessage(msg);
            }
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // check downloading state
        if (mIsDownloading) {
            ToastUtils.$(this, R.string.app_download_downloading);
            return super.onStartCommand(intent, flags, startId);
        }

        mDownloadUrl = intent.getStringExtra($.KEY_DOWNLOAD_URL);
        mPackageNames = intent.getStringExtra($.KEY_DOWNLOAD_PACKAGE);
        if (TextUtils.isEmpty($.sDownloadSDPath)) {
            mDownloadSDPath = getPackageName() + "/download";
        } else {
            mDownloadSDPath = $.sDownloadSDPath;
        }

        if (TextUtils.isEmpty(mDownloadUrl)) {
            sendMessage(DOWNLOAD_STATE_ERROR_URL);
            return super.onStartCommand(intent, flags, startId);
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mDestDir = new File(Environment.getExternalStorageDirectory().getPath()
                    + "/" + mDownloadSDPath);
            if (mDestDir.exists()) {
                File destFile = new File(mDestDir.getPath() + "/" + URLEncoder.encode(mDownloadUrl));
                if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                    sendMessage(DOWNLOAD_STATE_INSTALL);
                    install(destFile);
                    stopSelf();
                    return super.onStartCommand(intent, flags, startId);
                }
            }
        } else {
            sendMessage(DOWNLOAD_STATE_ERROR_SDCARD);
            return super.onStartCommand(intent, flags, startId);
        }

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(AppUtils.$appname());
        mBuilder.setContentText(getText(R.string.app_download_notification_start));
        mBuilder.setProgress(100, 0, false);
        mBuilder.setAutoCancel(true);
        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplicationContext(), UpdateService.class);

        mPendingIntent = PendingIntent.getActivity(UpdateService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setTicker(getText(R.string.app_download_notification_start));
        mBuilder.setContentIntent(mPendingIntent);
        if ($.sUpdateIcon != 0) {
            mBuilder.setSmallIcon($.sUpdateIcon);
        }
        mNotificationManager.cancel(NOTIFICATION_ID);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        // 启动线程开始下载
        new UpdateThread().start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 检查apk文件是否有效(是正确下载,没有损坏的)
     * @param apkFilePath
     * @return
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 调用系统Intent安装apk包
     * @param apkFile
     */
    private void install(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        //android N 7.0系统个判断
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, mPackageNames+".fileprovider", apkFile);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void sendMessage(int what) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 下载线程
     */
    class UpdateThread extends Thread {

        @Override
        public void run() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (mDestDir == null) {
                    mDestDir = new File(Environment.getExternalStorageDirectory().getPath()
                            + "/" + mDownloadSDPath);
                }

                if (mDestDir.exists() && !mDestDir.isDirectory()) {
                    mDestDir.delete();
                }

                if (mDestDir.exists() || mDestDir.mkdirs()) {
                    LogUtils.$d("start download apk to sdcard download apk.");
                    download();
                }else{
                    sendMessage(DOWNLOAD_STATE_ERROR_FILE);
                }
            } else {
                sendMessage(DOWNLOAD_STATE_ERROR_SDCARD);
            }
            mIsDownloading = false;
            stopSelf();
        }
    }

    private void download() {
        mDestFile = new File(mDestDir.getPath() + "/" + URLEncoder.encode(mDownloadUrl));

        if (mDestFile.exists()
                && mDestFile.isFile()
                && checkApkFile(mDestFile.getPath())) {
            sendMessage(DOWNLOAD_STATE_INSTALL);
            install(mDestFile);
        } else {
            try {
                sendMessage(DOWNLOAD_STATE_START);
                mIsDownloading = true;
                DownloadUtils.$download(mDownloadUrl, mDestFile, false, mDownloadCallBack);
            } catch (Exception e) {
                sendMessage(DOWNLOAD_STATE_FAILURE);
                e.printStackTrace();
            }
        }
    }
}
