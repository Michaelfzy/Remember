package com.example.cardview_test;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cardview_test.service.LocationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private MyDatabaseHelper dbHelper;
    private static final String TAG = "MainActivity";
    public LocationService.MyBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (LocationService.MyBinder) iBinder;
            if (SPUtils.getLocationSwitch()){
                binder.startGetLocation();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content, new Home());
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content, new Remind());
                    transaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.content, new Game());
                    transaction.commit();
                    return true;
                case R.id.navigation_set:
                    transaction.replace(R.id.content, new Set());
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        dbHelper = new MyDatabaseHelper(MainActivity.this, "User.db", null, 2);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 开始组装初始化数据
        values.put("name", "未设置");
        values.put("phone", "未设置");
        values.put("more", "未设置");
//        db.insert("User", null, values); // 插入第1条数据
        values.clear();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //获取权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            bindService();
        }

    }

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new Home()).commit();

    }

    public void bindService() {
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                bindService();
            }
        }
    }
}
