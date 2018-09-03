package com.test.androidpackages.model.util;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private static final String TAG = "Error";
    private File currentDirectory;
    private final File rootDirectory;

    public FileManager(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootDirectory = Environment.getExternalStorageDirectory();
        } else {
            rootDirectory = ActivityCompat.getDataDir(context);
        }
        navigateTo(rootDirectory);
    }

    public boolean navigateTo(File directory) {
        if (!directory.isDirectory()) {
            Log.e(TAG, directory.toString() + " is not a directory!");
            return false;
        }

        if (!directory.equals(rootDirectory) &&
                rootDirectory.getAbsolutePath().contains(directory.getAbsolutePath())) {
            Log.e(TAG, "Trying to navigate upper than root directory to " + directory);
            return false;
        }
        currentDirectory = directory;
        return true;
    }

    public boolean navigateUp() {
        return navigateTo(currentDirectory.getParentFile());
    }

    public List<File> getFiles() {
        return Arrays.asList(currentDirectory.listFiles());

    }
}
