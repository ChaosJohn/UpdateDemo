package com.chaosjohn.fir_im_update;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chaosjohn.fir_im_update.config.DownloadKey;
import com.chaosjohn.fir_im_update.config.UpdateKey;
import com.chaosjohn.fir_im_update.module.Download;
import com.chaosjohn.fir_im_update.module.HandleUpdateResult;
import com.chaosjohn.fir_im_update.utils.GetAppInfo;
import com.chaosjohn.fir_im_update.view.DownLoadDialog;


/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/12 16:47
 */
public class FirImUpdater {

    private static Thread download;
    private static Thread thread_update;

    private static volatile FirImUpdater sInst = null;

    private FirImUpdater(Context context) {
        DownloadKey.FROMACTIVITY = context;
        if (DownloadKey.TOShowDownloadView != 2) {
            thread_update = new Thread(new HandleUpdateResult(context));
            thread_update.start();
        }
    }

    public static void manualStart(Context context) {
        DownloadKey.ISManual = true;
        if (!DownloadKey.LoadManual) {
            DownloadKey.LoadManual = true;
            new FirImUpdater(context);
        } else
            Toast.makeText(context, context.getString(R.string.updating_now), Toast.LENGTH_LONG).show();
    }

    public static FirImUpdater init(Context context) {
        FirImUpdater inst = sInst;
        if (inst == null) {
            synchronized (FirImUpdater.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new FirImUpdater(context);
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public static void showDownloadView(Context context) {
//        DownloadKey.saveFileName = GetAppInfo.getAppPackageName(context) + ".apk";
        if (UpdateKey.DialogOrNotification == 1) {
            Intent intent = new Intent();
            intent.setClass(context, DownLoadDialog.class);
            ((Activity) context).startActivityForResult(intent, 0);
        } else if (UpdateKey.DialogOrNotification == 2) {
            Notification.Builder builder = notificationInit(context);
            download = new Download(context, builder);
            download.start();
        }
    }

    private static Notification.Builder notificationInit(Context context) {
        Intent intent = new Intent(context, context.getClass());
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker(context.getString(R.string.start_downloading))
                .setContentTitle(GetAppInfo.getAppName(context))
                .setContentText(context.getString(R.string.updating))
                .setContentIntent(pIntent)
                .setWhen(System.currentTimeMillis());
        return builder;
    }

    public static void onResume(Context context) {
        if (DownloadKey.TOShowDownloadView == 2) {
            showDownloadView(context);
        } else {
            if (sInst != null) sInst = null;
        }
    }

    public static void onStop(Context context) {
        if (DownloadKey.TOShowDownloadView == 2 && UpdateKey.DialogOrNotification == 2) {
            download.interrupt();
        }
        if (DownloadKey.FROMACTIVITY != null) {
            DownloadKey.FROMACTIVITY = null;
        }
        thread_update.interrupt();
        if (DownloadKey.ISManual) {
            DownloadKey.ISManual = false;
        }
        if (DownloadKey.LoadManual) {
            DownloadKey.LoadManual = false;
        }
    }

}
