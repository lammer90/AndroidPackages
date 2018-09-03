package com.test.androidpackages.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.test.androidpackages.R;
import com.test.androidpackages.handler.FileAdapter;
import com.test.androidpackages.model.util.FileManager;

public class FilePickerActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private FileManager fileManager;
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter = new FileAdapter(this);
    private final String FILE_NAME = "file_name";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_picker);

        if (chekPermission()) {
            initFileMenedger();
        } else {
            requestPermission();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        recyclerView = findViewById(R.id.files_rv);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(fileAdapter);

        fileAdapter.setFiles(fileManager.getFiles());
        fileAdapter.notifyDataSetChanged();
        fileAdapter.setFileClickListener(s -> {
            if (s.isDirectory()) {
                fileManager.navigateTo(s);
                updateFileList();
            } else {
                Intent intent = new Intent();
                intent.putExtra(FILE_NAME, s.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fileManager.navigateUp()) {
            updateFileList();
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initFileMenedger();
                } else {
                    requestPermission();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initFileMenedger() {
        fileManager = new FileManager(this);
    }

    private boolean chekPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void updateFileList() {
        fileAdapter.setFiles(fileManager.getFiles());
        fileAdapter.notifyDataSetChanged();
    }
}
