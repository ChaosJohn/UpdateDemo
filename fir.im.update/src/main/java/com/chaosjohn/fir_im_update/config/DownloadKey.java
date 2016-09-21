package com.chaosjohn.fir_im_update.config;

import android.content.Context;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/21 15:56
 */
public class DownloadKey {

    public static Context FROMACTIVITY = null;
    public static Boolean ISManual = false;
    public static Boolean LoadManual = false;
    public static int TOShowDownloadView = 0;
    //    public static final String savePath = Environment.getExternalStorageDirectory() + "/UpdateFun/";
    public static String saveFileName = "newversion.apk";
    public static String name;
    public static String installUrl = "";
    public static String changeLog = "";
    public static String versionShort;
//    public static String build;
    public static String version;
    public static String updated_at = "";
    public static ChangelogInfo changelogInfo;
    public static boolean interceptFlag = false;
    public static boolean downloaded = false;
    public static long fsize;

    public static class ChangelogInfo {
        public int threshold = -1;
        public String cn = "";
        public String en = "";
    }
}
