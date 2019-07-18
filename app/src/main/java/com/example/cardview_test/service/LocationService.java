package com.example.cardview_test.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.cardview_test.SPUtils;
import com.example.cardview_test.bmob.Location;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class LocationService extends Service{

    private static final String TAG = "LocationService";
    private Timer timer;
    private TimerTask countDownTask;
    private LocationManager mLocationManager;
    private volatile double latitude = 0.0;
    private volatile double longitude = 0.0;
    public static final String LOCATION_ID = "7e30572440";
    private MyBinder myBinder = new MyBinder();

    class CountDownTask extends TimerTask{
        @Override
        public void run() {
            Looper.prepare();
            Log.d(TAG, "run: " + SPUtils.getLocationSwitch());
            if (SPUtils.getLocationSwitch()){
                getLocation();
            }
            Looper.loop();
        }
    }

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        countDownTask = new CountDownTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder{
        public void startGetLocation(){
            countDownTask = new CountDownTask();
            timer.schedule(countDownTask, 1000, 3000);
        }

        public void endGetLocation(){
            countDownTask.cancel();
            countDownTask = null;
            timer.cancel();
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                android.location.Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.e(TAG, "GPS Location changed : Lat: "
                            + location.getLatitude() + " Lng: "
                            + location.getLongitude());

                    Location location1 = new Location();
                    location1.setPackageName(LocationService.this.getPackageName());
                    location1.setLongitude(longitude);
                    location1.setLatitude(latitude);
                    location1.update(LOCATION_ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.e(TAG, "done: success");
                            }else {
                                Log.e(TAG, "done: failed" + e.toString());
                            }
                        }
                    });
                }else {
                    getNetWorkLocation();
                }
            } else {
                getNetWorkLocation();
            }
        }
    }

    private void getNetWorkLocation(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        LocationListener locationListener = new LocationListener() {

            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {

            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {

            }

            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (location != null) {
                    Log.e(TAG, "Location changed : Lat: "
                            + location.getLatitude() + " Lng: "
                            + location.getLongitude());

                    Location location1 = new Location();
                    location1.setPackageName(LocationService.this.getPackageName());
                    location1.setLongitude(longitude);
                    location1.setLatitude(latitude);
                    location1.update(LOCATION_ID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.e(TAG, "done: success");
                            }else {
                                Log.e(TAG, "done: failed" + e.toString());
                            }
                        }
                    });
                }
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        android.location.Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude(); //经度
            longitude = location.getLongitude(); //纬度
        }
    }
}
