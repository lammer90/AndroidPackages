package com.test.androidpackages;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

public class AppManager {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AppInfo> getinstalledApps(Context context) {
        return context.getPackageManager().getInstalledPackages(0).stream()
                .map(s -> convert(s, context))
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
