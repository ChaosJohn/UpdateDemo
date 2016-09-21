package com.chaosjohn.fir_im_update.module;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.chaosjohn.fir_im_update.config.DownloadKey;
import com.chaosjohn.fir_im_update.config.UpdateKey;
import com.chaosjohn.fir_im_update.utils.StorageUtils;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/13 14:38
 */
public class Update extends Thread {

    private String result;
    private String url = "http://api.fir.im/apps/latest/" + UpdateKey.APP_ID
            + "?api_token=" + UpdateKey.API_TOKEN;

    public void run() {
        try {
            URL httpUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) httpUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;

                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                result = new String(sb.toString().getBytes(), "utf-8");

                interpretingData(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    private void interpretingData(String result) {
        try {
            JSONObject object = new JSONObject(result);
            DownloadKey.name = object.getString("name");
            DownloadKey.versionShort = object.getString("versionShort");
//            DownloadKey.build = object.getString("build");
            DownloadKey.version = object.getString("version");
            DownloadKey.installUrl = object.getString("installUrl");
            DownloadKey.changeLog = object.getString("changelog");
            DownloadKey.updated_at = object.getString("updated_at");
            DownloadKey.fsize = new JSONObject(object.getString("binary")).getLong("fsize");
            if (!DownloadKey.updated_at.isEmpty()) {
                DownloadKey.saveFileName = DownloadKey.name + "_" + DownloadKey.versionShort + "_" + DownloadKey.updated_at + ".apk";
                File file = StorageUtils.getCacheDirectory(DownloadKey.FROMACTIVITY);
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(file, DownloadKey.saveFileName);
                try {
                    long apkFileSize = getFileSize(apkFile);
                    Log.e("apkFileSize", String.valueOf(apkFileSize));
                    if (apkFileSize == DownloadKey.fsize)
                        DownloadKey.downloaded = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (DownloadKey.changeLog.startsWith("{")) {
                JSONObject changeLogJson = new JSONObject(DownloadKey.changeLog);
                DownloadKey.changelogInfo = new DownloadKey.ChangelogInfo();
                DownloadKey.changelogInfo.threshold = changeLogJson.getInt("threshold");
                DownloadKey.changelogInfo.cn = changeLogJson.getString("cn");
                DownloadKey.changelogInfo.en = changeLogJson.getString("en");
            }
            Log.i("UpdateFun TAG",
                    String.format("ChangeLog:%s, Version:%s, ApkDownloadUrl:%s",
                            DownloadKey.changeLog, DownloadKey.versionShort, DownloadKey.installUrl));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    {
      "name": "fir.im",
      "version": "1.0",
      "changelog": "更新日志",
      "versionShort": "1.0.5",
      "build": "6",
      "installUrl": "http://download.fir.im/v2/app/install/xxxxxxxxxxxxxxxxxxxx?download_token=xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
      "install_url": "http://download.fir.im/v2/app/install/xxxxxxxxxxxxxxxx?download_token=xxxxxxxxxxxxxxxxxxxxxxxxxxxx",   # 新增字段
      "update_url": "http://fir.im/fir",  # 新增字段
      "binary": {
        "fsize": 6446245
      }
    }
     */

}
