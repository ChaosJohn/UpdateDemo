package com.chaosjohn.fir_im_update.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import com.chaosjohn.fir_im_update.R;
import com.chaosjohn.fir_im_update.config.DownloadKey;
import com.chaosjohn.fir_im_update.utils.GetAppInfo;
import com.chaosjohn.fir_im_update.view.UpdateDialog;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/8/19 10:44
 */
public class HandleUpdateResult implements Runnable {

    private String versionShort = "";
    private int versionCode;
    private Up_handler up_handler;
    private static String TAG = "fir.im.update";
    private Context context;

    public HandleUpdateResult(Context context) {
        this.context = context;
        up_handler = new Up_handler(context);
        this.versionShort = GetAppInfo.getAppVersionName(context);
        this.versionCode = GetAppInfo.getAppVersionCode(context);
    }

    private static class Up_handler extends Handler {
        WeakReference<Context> mActivityReference;

        public Up_handler(Context context) {
            mActivityReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mActivityReference.get();
            if (context != null) {
                switch (msg.arg1) {
                    case 1:
                        showNoticeDialog(context, false);
                        break;
                    case 2:
                        if (DownloadKey.ISManual) {
                            DownloadKey.LoadManual = false;
                            Toast.makeText(context, context.getString(R.string.network_unstable), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3:
                        if (DownloadKey.ISManual) {
                            DownloadKey.LoadManual = false;
                            Toast.makeText(context, context.getString(R.string.already_latest_version), Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 4:
                        showNoticeDialog(context, true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void run() {
        // 检测更新
        Update update = new Update();
        update.start();

        Message msg = new Message();
        try {
            update.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (DownloadKey.versionShort == null) {
            Log.i(TAG, context.getString(R.string.update_info_error));
            msg.arg1 = 2;
            up_handler.sendMessage(msg);
        } else if (!DownloadKey.versionShort.equals(versionShort) && Integer.parseInt(DownloadKey.version) >= versionCode) {
//        } else if (!DownloadKey.versionShort.equals(versionShort)) {
            Log.i(TAG, context.getString(R.string.update_available));
            msg.arg1 = 1;
            if (null != DownloadKey.changelogInfo &&
                    versionCode < DownloadKey.changelogInfo.threshold)
                msg.arg1 = 4;
            up_handler.sendMessage(msg);
        } else {
            Log.i(TAG, context.getString(R.string.already_latest_version));
            msg.arg1 = 3;
            up_handler.sendMessage(msg);
        }
    }

    public static void showNoticeDialog(Context context, boolean hideCancel) {
        Intent intent = new Intent();
        intent.setClass(context, UpdateDialog.class);
        intent.putExtra("hideCancel", hideCancel);
        ((Activity) context).startActivityForResult(intent, 100);
    }

}
