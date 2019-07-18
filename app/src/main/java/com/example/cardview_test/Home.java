package com.example.cardview_test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

public class Home extends Fragment {
    private View mView;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ImageView headimage;
    private String phone1;
    private String phone2;
    private String phone3;
    private String phone4;
    private String phone5;
    private String phone6;
    private ImageView stu_headimage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new MyDatabaseHelper(getContext(),"User.db",null,2);
        db = dbHelper.getWritableDatabase();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        // 压缩bitmap到ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut);
        ContentValues values = new ContentValues();
        // 开始组装初始化数据
        values.put("name", "姓名");
        values.put("phone", "未设置");
        values.put("address", "关系");
        values.put("image", byteOut.toByteArray());
        db.insert("User", null, values);
        db.insert("User", null, values);
        db.insert("User", null, values);
        db.insert("User", null, values);
        db.insert("User", null, values);
        db.insert("User", null, values);// 初始化数据
        values.clear();
        db.delete("User", "id > ?", new String[] { "6" });
        ImageView imageView1 = mView.findViewById(R.id.imageView1_home);
        showimage("1",imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","1");
                startActivity(intent);
            }
        });
        ImageView imageView2 = mView.findViewById(R.id.imageView2_home);
        showimage("2",imageView2);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","2");
                startActivity(intent);
            }
        });
        ImageView imageView3 = mView.findViewById(R.id.imageView3_home);
        showimage("3",imageView3);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","3");
                startActivity(intent);
            }
        });
        ImageView imageView4 = mView.findViewById(R.id.imageView4_home);
        showimage("4",imageView4);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","4");
                startActivity(intent);
            }
        });
        ImageView imageView5 = mView.findViewById(R.id.imageView5_home);
        showimage("5",imageView5);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","5");
                startActivity(intent);
            }
        });
        ImageView imageView6 = mView.findViewById(R.id.imageView6_home);
        showimage("6",imageView6);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number","6");
                startActivity(intent);
            }
        });

        Button button_call1 = mView.findViewById(R.id.button1_call);
        Button button_more1 = mView.findViewById(R.id.button1_more);
        TextView textView_name1 = mView.findViewById(R.id.textView1_name);
        TextView textView_cate1 = mView.findViewById(R.id.textView1_cate);

        Button button_call2 = mView.findViewById(R.id.button2_call);
        Button button_more2 = mView.findViewById(R.id.button2_more);
        TextView textView_name2 = mView.findViewById(R.id.textView2_name);
        TextView textView_cate2 = mView.findViewById(R.id.textView2_cate);

        Button button_call3 = mView.findViewById(R.id.button3_call);
        Button button_more3 = mView.findViewById(R.id.button3_more);
        TextView textView_name3 = mView.findViewById(R.id.textView3_name);
        TextView textView_cate3 = mView.findViewById(R.id.textView3_cate);

        Button button_call4 = mView.findViewById(R.id.button4_call);
        Button button_more4 = mView.findViewById(R.id.button4_more);
        TextView textView_name4 = mView.findViewById(R.id.textView4_name);
        TextView textView_cate4 = mView.findViewById(R.id.textView4_cate);

        Button button_call5 = mView.findViewById(R.id.button5_call);
        Button button_more5 = mView.findViewById(R.id.button5_more);
        TextView textView_name5 = mView.findViewById(R.id.textView5_name);
        TextView textView_cate5 = mView.findViewById(R.id.textView5_cate);

        Button button_call6 = mView.findViewById(R.id.button6_call);
        Button button_more6 = mView.findViewById(R.id.button6_more);
        TextView textView_name6 = mView.findViewById(R.id.textView6_name);
        TextView textView_cate6 = mView.findViewById(R.id.textView6_cate);

        cardview_show("1",textView_name1,textView_cate1,button_more1,button_call1);
        cardview_show("2",textView_name2,textView_cate2,button_more2,button_call2);
        cardview_show("3",textView_name3,textView_cate3,button_more3,button_call3);
        cardview_show("4",textView_name4,textView_cate4,button_more4,button_call4);
        cardview_show("5",textView_name5,textView_cate5,button_more5,button_call5);
        cardview_show("6",textView_name6,textView_cate6,button_more6,button_call6);

        return mView;
    }
    public void cardview_show(final String temp,TextView name,TextView cate,Button more,Button call){
        shownameandcate(temp,name,cate);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),familyinfo.class);
                intent.putExtra("id_number",temp);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + getphone(temp)));
                startActivity(intent);
            }
        });

    }
    public  String getphone(String temp){
        Cursor cursor =  db.query("User",null,"id = ?",new String[]{temp},null,null,null);
        String phonenum = "00000000";
        if(cursor.moveToFirst()){
            do {
                phonenum = cursor.getString(cursor.getColumnIndex("phone"));
                return phonenum;

            }while(cursor.moveToNext());
        }
        cursor.close();
        return phonenum;

    }
    public void showimage(String temp,ImageView image){
        Cursor cursor =  db.query("User",null,"id = ?",new String[]{temp},null,null,null);
        if(cursor.moveToFirst()){
            do {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
                        imageBytes.length);
                image.setImageBitmap(bitmap);

            }while(cursor.moveToNext());
        }
        cursor.close();

    }
    public void shownameandcate(String temp,TextView Textname,TextView Textcate){
        Cursor cursor =  db.query("User",null,"id = ?",new String[]{temp},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String cate = cursor.getString(cursor.getColumnIndex("address"));
                Textname.setText(name);
                Textcate.setText(cate);

            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onStart() {
        super.onStart();
//        dbHelper = new MyDatabaseHelper(getContext(),"User.db",null,2);
//        dbHelper.getWritableDatabase();
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image);
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        // 压缩bitmap到ByteArrayOutputStream
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut);
//        ContentValues values = new ContentValues();
//        // 开始组装初始化数据
//        values.put("name", "姓名");
//        values.put("phone", "未设置");
//        values.put("address", "关系");
//        values.put("image", byteOut.toByteArray());
//        db.insert("User", null, values);
//        db.insert("User", null, values);
//        db.insert("User", null, values);
//        db.insert("User", null, values);
//        db.insert("User", null, values);
//        db.insert("User", null, values);// 初始化数据
//        values.clear();
//        db.delete("User", "id > ?", new String[] { "6" });
//
//
   }


}

