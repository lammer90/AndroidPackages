package com.test.androidpackages;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.test.androidpackages.model.AppInfo;

import java.lang.ref.WeakReference;

public class AsyncTascUninstall extends AsyncTask<AppInfo, Void, Boolean> {
    private final WeakReference<UninstallListener> uninstallListenerWeakReference;

    public AsyncTascUninstall(WeakReference<UninstallListener> uninstallListenerWeakReference) {
        this.uninstallListenerWeakReference = uninstallListenerWeakReference;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Boolean doInBackground(AppInfo... appInfos) {
        AppInfo appInfo = appInfos[0];
        if (appInfo.isSystem()){
            return RootHelper.uninstallSystem(appInfo.getApk());
        }
        else {
            return RootHelper.uninstall(appInfo.getPackageName());
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        UninstallListener uninstallListener = uninstallListenerWeakReference.get();
        if (uninstallListener != null) {
            if (aBoolean) {
                uninstallListener.doUninstall();
            } else {
                uninstallListener.doNotUninstall();
            }
        }
    }

    public interface UninstallListener {
        void doUninstall();

        void doNotUninstall();
    }
}
