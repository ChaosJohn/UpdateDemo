package com.chaosjohn.fir_im_update.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.chaosjohn.fir_im_update.FirImUpdater;

import java.io.File;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/8/18 16:52
 */
public class InstallApk {

    public static void startInstall(Context context, File apkfile) {
//        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, FirImUpdater.providerName, apkfile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkfile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
//        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }
}
