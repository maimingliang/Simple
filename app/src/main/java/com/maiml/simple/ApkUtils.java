package com.maiml.simple;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * author   maimingliang
 */


public class ApkUtils {

        /**
         * 获取已安装Apk文件的源Apk文件
         * 如：/data/app/my.apk
         *
         * @param context
         * @param packageName
         * @return
         */
        public static String getSourceApkPath(Context context, String packageName) {
            if (TextUtils.isEmpty(packageName))
                return null;

            try {
                ApplicationInfo appInfo = context.getPackageManager()
                        .getApplicationInfo(packageName, 0);
                return appInfo.sourceDir;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 安装Apk
         *
         * @param context
         * @param apkPath
         */
        public static void installApk(Context context, String apkPath) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + apkPath),
                    "application/vnd.android.package-archive");

            context.startActivity(intent);
        }
}
