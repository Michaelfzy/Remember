package com.example.cardview_test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class familyinfo extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private List<Family> students = new ArrayList<Family>();
    private SQLiteDatabase db;
    private String updateName;
    private String arg;
    private String phonecall;
    private ImageView stu_headimage;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    //    //拍照功能参数
//    private static final int TAKE_PHOTO = 1;
//    private static final int CHOOSE_PHOTO = 2;
//    private final static int CROP_IMAGE = 3;
//    //imageUri照片真实路径
//    private Uri imageUri;
//    //照片存储
//    File filePath;
//imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyinfo);
        dbHelper = new MyDatabaseHelper(familyinfo.this,"User.db",null,2);
        db = dbHelper.getWritableDatabase();
        Intent i = getIntent();
        arg = i.getStringExtra("id_number");
        stu_headimage = (ImageView) findViewById(R.id.stu_headimage);


        Button sxiugai = (Button) findViewById(R.id.button_update);
        Button sok = (Button) findViewById(R.id.button_ok);//
        final EditText sname = (EditText) findViewById(R.id.EditText_set_name);
        final EditText sphone = (EditText) findViewById(R.id.EditText_set_phone);
        final EditText smore = (EditText) findViewById(R.id.EditText_set_adress);
        final ImageView stu_headimage = (ImageView) findViewById(R.id.stu_headimage);
        ImageView callstu = (ImageView) findViewById(R.id.call);
        ImageView smsstu = (ImageView) findViewById(R.id.sendmes);
        Cursor cursor =  db.query("User",null,"id = ?",new String[]{arg},null,null,null);
        if(cursor.moveToFirst()){
            do {
                phonecall = cursor.getString(cursor.getColumnIndex("phone"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
                        imageBytes.length);
                stu_headimage.setImageBitmap(bitmap);

            }while(cursor.moveToNext());
        }
        cursor.close();

        stu_headimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        show(arg);
        sname.setEnabled(false);
        smore.setEnabled(false);
        sphone.setEnabled(false);
        sxiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname.setEnabled(true);
                smore.setEnabled(true);
                sphone.setEnabled(true);
            }
        });
        sok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)stu_headimage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                // 压缩bitmap到ByteArrayOutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOut);
                ContentValues values = new ContentValues();
                values.put("name", sname.getText().toString());
                values.put("phone", sphone.getText().toString());
                values.put("address", smore.getText().toString());
                values.put("image", byteOut.toByteArray());
                db.update("User", values, "id=?", new String[]{arg});
                sname.setEnabled(false);
                smore.setEnabled(false);
                sphone.setEnabled(false);
                Intent intent = new Intent(familyinfo.this,MainActivity.class);
                startActivity(intent);


            }
        });
        callstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phonecall));
                startActivity(intent);
            }
        });
        smsstu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
                        .parse("smsto:" + phonecall));
                startActivity(intent);
            }
        });

    }
    //剪切图片
    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( "Test", "startImageCrop: " + "执行到压缩图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限，如果不加的话，   会产生无法加载图片的问题
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 150 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 150 );//裁剪区的Y方向宽
        intent.putExtra( "scale", true );//是否保留比例
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.PNG.toString() );
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回dataParcelable相应的Bitmap数据，防止造成OOM，置位false
        //判断文件是否存在
        //File saveToFile = ImageUtils.getTempFile();
        if (!saveToFile.getParentFile().exists()) {
            saveToFile.getParentFile().mkdirs();
        }
        //将剪切后的图片存储到此文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveToFile));
        Log.i( "Test", "startImageCrop: " + "即将跳到剪切图片" );
        startActivityForResult( intent, CROP_IMAGE );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //需要对拍摄的照片进行处理编辑
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
                    Log.i( "Test", "onActivityResult TakePhoto : "+imageUri );
                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //takephoto.setImageBitmap( bitmap );
                    //设置照片存储文件及剪切图片
                    File saveFile = ImageUtils.getTempFile();
                    filePath = ImageUtils.getTempFile();
                    startImageCrop( saveFile,imageUri );
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
                    Log.i( "Test", "onActivityResult: 执行到打开相册了" );
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
                        Log.i( "Test", "onActivityResult: uriImage is " +imageUri );
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //                        photo_taken.setImageBitmap(bitmap);
                        //设置照片存储文件及剪切图片
                        File saveFile = ImageUtils.setTempFile( familyinfo.this );
                        filePath = ImageUtils.getTempFile();
                        startImageCrop( saveFile,imageUri );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_IMAGE:
                if(resultCode == RESULT_OK){
                    Log.i( "Test", "onActivityResult: CROP_IMAGE" + "进入了CROP");
                    // 通过图片URI拿到剪切图片
                    //bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile( filePath.toString() );
                    //把裁剪后的图片展示出来
                    stu_headimage.setImageBitmap( bitmap );
                    //ImageUtils.Compress( bitmap );
                }
                break;
            default:
                break;
        }
    }
    public static class ImageUtils {
        private static String TAG = "Test";
        public static File tempFile;

        public static Uri getImageUri(Context content) {
            File file = setTempFile( content );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT >= 24){
                //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = FileProvider.getUriForFile( content, "com.example.a15927.bottombardemo.fileprovider", file );
                return imageUri;
            }else{
                //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = Uri.fromFile( file );
                return imageUri;
            }
        }

        public static File setTempFile(Context content) {
            //自定义图片名称
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance( Locale.CHINA)) + ".png";
            Log.i( TAG, " name : "+name );
            //定义图片存放的位置
            tempFile = new File(content.getExternalCacheDir(),name);
            Log.i( TAG, " tempFile : "+tempFile );
            return tempFile;
        }

        public static File getTempFile() {
            return tempFile;
        }
    }
    //    public void showInformation(String temp) {
//        Button sxiugai = (Button) findViewById(R.id.button_update);
//        Button sok = (Button) findViewById(R.id.button_ok);
//        final EditText sname = (EditText) findViewById(R.id.EditText_set_name);
//        final EditText sphone = (EditText) findViewById(R.id.EditText_set_phone);
//        final EditText smore = (EditText) findViewById(R.id.EditText_set_adress);
//        final ImageView stu_headimage = (ImageView) findViewById(R.id.stu_headimage);
//        ImageView callstu = (ImageView) findViewById(R.id.call);
//        ImageView smsstu = (ImageView) findViewById(R.id.sendmes);
//        sname.setText();
//        sphone.setText(students.get(temp).getPhone().toString());
//        smore.setText(students.get(temp).getMore().toString());
//        sname.setEnabled(false);
//        smore.setEnabled(false);
//        sphone.setEnabled(false);
//        Cursor cursor = db.rawQuery(
//                "select * from student where name = '"
//                        + students.get(temp).getName().toString() + "'", null);
//        // 知道只有一条记录 直接moveToNext
//        cursor.moveToNext();
//        // cursor.getColumnIndex("image")获取列的索引
//        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
//                imageBytes.length);
//        stu_headimage.setImageBitmap(bitmap);
//        updateName = students.get(temp).getName().toString();
//        stu_headimage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                chooseImage();
//            }
//        });
//        sxiugai.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                sname.setEnabled(true);
//                sphone.setEnabled(true);
//                smore.setEnabled(true);
//            }
//        });
//        sok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Bitmap bitmap = ((BitmapDrawable)stu_headimage.getDrawable()).getBitmap();
//                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//                // 压缩bitmap到ByteArrayOutputStream
//                bitmap.compress(CompressFormat.PNG, 100, byteOut);
//                // Cursor cursor = db.rawQuery("update student set name = '"
//                // + sname.getText().toString() + "',sex = '"
//                // + ssex.getText().toString() + "',mingzu = '"
//                // + smingzu.getText().toString() + "',id = '"
//                // + sid.getText().toString() + "',birthday = '"
//                // + sbir.getText().toString() + "',phone = '"
//                // + sphone.getText().toString() + "',more = '"
//                // + smore.getText().toString() + "' ,image = '"
//                // + byteOut.toByteArray()+"' where name = '"
//                // + updateName + "'", null);
//
//                ContentValues values = new ContentValues();
//                values.put("name", sname.getText().toString());
//                values.put("phone", sphone.getText().toString());
//                values.put("more", smore.getText().toString());
//                values.put("image", byteOut.toByteArray());
//                db.update("Family", values, "name=?", new String[]{updateName});
//                //cursor.moveToNext();
//                // Toast.makeText(StudentInformationManagerActivity.this,
//                // "修改成功！",
//                // Toast.LENGTH_SHORT).show();
//            }
//        });
//        callstu.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//                        + sphone.getText().toString()));
//                startActivity(intent);
//            }
//        });
//        smsstu.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
//                        .parse("smsto:" + sphone.getText().toString()));
//                startActivity(intent);
//            }
//        });
//    }
    private void chooseImage() {
        // 选择相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    private void show(String temp){
        Cursor cursor =  db.query("User",null,"id = ?",new String[]{temp},null,null,null);
        final EditText sname = (EditText) findViewById(R.id.EditText_set_name);
        final EditText sphone = (EditText) findViewById(R.id.EditText_set_phone);
        final EditText smore = (EditText) findViewById(R.id.EditText_set_adress);
        if(cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                sname.setText(name);
                sphone.setText(phone);
                smore.setText(address);

            }while(cursor.moveToNext());
        }
        cursor.close();

    }
}
