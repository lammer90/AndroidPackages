package com.test.androidpackages.model.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileManager {
    private static final String TAG = "Error";
    private Path currentDirectory;
    private final Path rootDirectory;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FileManager(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            rootDirectory = Environment.getExternalStorageDirectory().toPath();
        }
        else {
            rootDirectory = Objects.requireNonNull(ActivityCompat.getDataDir(context)).toPath();
        }
        navigateTo(rootDirectory);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean navigateTo(Path directory) {
        if (!Files.isDirectory(directory)){
            Log.e(TAG, directory.toString() + " is not a directory!");
            return false;
        }

        if (directory.compareTo(rootDirectory) < 0){
            Log.e(TAG, "Trying to navigate upper than root directory to " + directory);
            return false;
        }
        currentDirectory = directory;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean navigateUp(){
        return navigateTo(currentDirectory.getParent());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Path> getFiles(){
        try {
            return Files.list(currentDirectory).collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
