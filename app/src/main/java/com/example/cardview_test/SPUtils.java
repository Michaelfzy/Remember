package com.example.cardview_test;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cardview_test.aplication.CardApplication;

public class SPUtils {

    private static final String SP_FIlE = "sp_file";
    private static final String LOCATION_SWITCH = "location_switch";

    private static SharedPreferences preferences = CardApplication.mApplicationContext.getSharedPreferences(SP_FIlE, Context.MODE_PRIVATE);

    public static boolean getLocationSwitch(){
        return preferences.getBoolean(LOCATION_SWITCH,false);
    }

    public static void setLocationSwitch(boolean isOpen){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOCATION_SWITCH,isOpen);
        editor.apply();
    }

}
