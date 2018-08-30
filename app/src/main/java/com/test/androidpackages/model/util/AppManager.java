package com.test.androidpackages.model.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.test.androidpackages.model.AppInfo;

import java.util.List;
import java.util.stream.Collectors;

public class AppManager {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AppInfo> getinstalledApps(Context context, String str) {
        return context.getPackageManager().getInstalledPackages(0).stream()
                .map(s -> convert(s, context))
                .filter(s -> str.equals("") || s.getName().toLowerCase().contains(str.toLowerCase()))
                .collect(Collectors.toList());
    }

    private static AppInfo convert(PackageInfo packageInfo, Context context) {
        return new AppInfo(
                packageInfo.packageName, // Имя пакета
                packageInfo.versionCode, // Код версии
                packageInfo.versionName, // Имя версии
                packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString(),
                packageInfo.applicationInfo.loadIcon(context.getPackageManager())); // Иконка приложения
    }
}
