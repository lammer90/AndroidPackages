package com.test.androidpackages;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;

public class RootHelper {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean uninstall(String delPackage) {
        return executeCommand("pm uninstall " + delPackage).contains("succes");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean uninstallSystem(File apk) {
        executeCommand("mount -o rw,remount /system");
        executeCommand("rm " + apk.getAbsolutePath());
        executeCommand("mount -o ro,remount /system");
        return executeCommand("ls " + apk.getAbsolutePath()).equals("");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static String executeCommand(String s) {
        try {
            return Shell.SU.run(s).stream().reduce((s1, s2) -> s1 + s2).orElse("");
        } catch (NullPointerException e) {
            return "";
        }
    }
}
