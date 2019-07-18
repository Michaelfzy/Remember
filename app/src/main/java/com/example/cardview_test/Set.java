package com.example.cardview_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.fragment.app.Fragment;



public class Set extends Fragment {
    private static final String TAG = "Set";
    private View sView;
    private MyDatabaseHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sView =  inflater.inflate(R.layout.fragment_set, container, false);
        Button update = sView.findViewById(R.id.button_update);
        Button ok = sView.findViewById(R.id.button_ok);
//        TextView textView_phone = sView.findViewById(R.id.EditText_set_phone);
        final EditText sname = sView.findViewById(R.id.EditText_set_name);
        final EditText sphone = sView.findViewById(R.id.EditText_set_phone);
        final EditText saddress = sView.findViewById(R.id.EditText_set_adress);
        final Switch aSwitch= sView.findViewById(R.id.switch_set);
//        dbHelper = new MyDatabaseHelper(getContext(),"User.db",null,2);
//        dbHelper.getWritableDatabase();
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        // 开始组装初始化数据
//        values.put("name", "未设置");
//        values.put("phone", "未设置");
//        values.put("address", "未设置");
//        db.insert("User", null, values); // 插入第一条数据
//        values.clear();
        sname.setEnabled(false);
        saddress.setEnabled(false);
        sphone.setEnabled(false);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname.setEnabled(true);
                saddress.setEnabled(true);
                sphone.setEnabled(true);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname.setEnabled(false);
                saddress.setEnabled(false);
                sphone.setEnabled(false);
            }
        });

        if (SPUtils.getLocationSwitch()){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    SPUtils.setLocationSwitch(true);
                    if (getActivity() instanceof MainActivity){
                        ((MainActivity) getActivity()).binder.startGetLocation();
                    }
                }else {
                    SPUtils.setLocationSwitch(false);
                    if (getActivity() instanceof MainActivity){
                        ((MainActivity) getActivity()).binder.endGetLocation();
                    }
                }
            }
        });


        return sView;
    }
}
