package com.test.androidpackages;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class AsyncTascUninstall extends AsyncTask<String, Void, Boolean>{
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Boolean doInBackground(String... strings) {
        String delPackage = strings[0];
        return RootHelper.uninstall(delPackage);
    }
}
