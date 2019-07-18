package com.example.cardview_test.bmob;

import cn.bmob.v3.BmobObject;

public class Location extends BmobObject {

    private String packageName;

    private double longitude;

    private double latitude;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
