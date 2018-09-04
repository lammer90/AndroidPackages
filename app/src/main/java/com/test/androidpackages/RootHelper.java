package com.test.androidpackages;

import android.os.Build;
import android.support.annotation.RequiresApi;

import eu.chainfire.libsuperuser.Shell;

public class RootHelper {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean uninstall(String delPackage){
        return executeCommand("pm uninstall " + delPackage);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean executeCommand(String s) {
        try {
            return Shell.SU.run(s).stream().anyMatch("succes"::equals);
        }
        catch (NullPointerException e){
            return false;
        }
    }
}
