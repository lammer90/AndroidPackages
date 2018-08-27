package com.test.androidpackages;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppsAdapter appsAdapter = new AppsAdapter();

        recyclerView = findViewById(R.id.apps_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appsAdapter);

        appsAdapter.setInfos(AppManager.getinstalledApps(this));
        appsAdapter.notifyDataSetChanged();

        /*AppManager.getinstalledApps(this)
                .forEach(s -> Log.i(TAG, "App: " + s));*/
    }
}
