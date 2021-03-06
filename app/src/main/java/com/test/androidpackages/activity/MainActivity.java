package com.test.androidpackages.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.androidpackages.AsyncTascUninstall;
import com.test.androidpackages.BuildConfig;
import com.test.androidpackages.model.AppInfo;
import com.test.androidpackages.model.util.AppManager;
import com.test.androidpackages.handler.AppsAdapter;
import com.test.androidpackages.R;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppsAdapter appsAdapter = new AppsAdapter();
    private final int REQUEST_FILE = 1;
    private final String FILE_NAME = "file_name";
    private AsyncTascUninstall.UninstallListener uninstallListener = new AsyncTascUninstall.UninstallListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void doUninstall() {
            updateApps("");
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void doNotUninstall() {
            updateApps("");
        }
    };

    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            WeakReference<AsyncTascUninstall.UninstallListener> uninstallListenerWeakReference = new WeakReference<>(uninstallListener);
            new AsyncTascUninstall(uninstallListenerWeakReference).execute(((AppInfo)viewHolder.itemView.getTag()));
            // OR
            //uninstallPackage(((AppInfo)viewHolder.itemView.getTag()).getPackageName());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                updateApps(s);
                return true;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        recyclerView = findViewById(R.id.apps_rv);
        swipeRefreshLayout = findViewById(R.id.sw_la);

        swipeRefreshLayout.setOnRefreshListener(() -> updateApps(""));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(itemTouchHelper);
        recyclerView.setAdapter(appsAdapter);

        updateApps("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setup: {
                Intent intent = new Intent(this, FilePickerActivity.class);
                startActivityForResult(intent, REQUEST_FILE);
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_FILE) {
            assert data != null;
            if (data.getStringExtra(FILE_NAME).endsWith(".apk")) {
                installAppFromFile(new File(data.getStringExtra(FILE_NAME)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void installAppFromFile(File file) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            uri = Uri.fromFile(file);
        }
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        //installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(installIntent);
    }

    private void uninstallPackage(String packageName) {
        Intent unInstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        unInstallIntent.setData(Uri.parse("package:" + packageName));
        startActivity(unInstallIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateApps(String str) {
        appsAdapter.setInfos(AppManager.getinstalledApps(this, str));
        appsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void show(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
