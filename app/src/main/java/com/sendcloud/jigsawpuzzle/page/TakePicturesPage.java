package com.sendcloud.jigsawpuzzle.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import com.example.cardview_test.R;
import com.sendcloud.jigsawpuzzle.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 胡志远 on 2018/4/9.
 */

public class TakePicturesPage extends Activity {

    private ListView listview;
    private ListAdapter adapter1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpictrue);

        listview = (ListView) findViewById(R.id.listview1);
        final String pictures[] = new String[]{"图片1","图片2","图片3","图片4","图片5","图片6","图片7","图片8"
                ,"图片9","图片10","图片11","图片12","图片13","图片14","图片15","图片16","图片17","图片18"};
        final int images[] = new int[]{
                R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,
                R.drawable.image5,R.drawable.image6,R.drawable.image7,R.drawable.image8,
                R.drawable.image9,R.drawable.image10,R.drawable.image11,R.drawable.image12,
                R.drawable.image13,R.drawable.image14,R.drawable.image15,R.drawable.image16,
                R.drawable.image17,R.drawable.image18
        };

        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        for(int i=0; i<pictures.length ; i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("icon", images[i]);
            map.put("picture", pictures[i]);
            data.add(map);
        }


        adapter1 = new SimpleAdapter(TakePicturesPage.this, data, R.layout.listxml, new String[]{"icon","picture"}, new int[]{R.id.image1,R.id.textview});
        listview.setAdapter(adapter1);
        listview.setOnItemClickListener(new myitemlistener());
    }

     class myitemlistener implements AdapterView.OnItemClickListener {

        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
         *列表获取点击事件
         */
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            ImageView image =(ImageView) view.findViewById(R.id.image1);
            bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

            String imagePath = FileUtils.getPath(TakePicturesPage.this,bitmap2uri(TakePicturesPage.this,bitmap));
            Intent intent = new Intent(TakePicturesPage.this, PuzzlePage.class);//skip into the puzzle interface
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
            finish();
        }

    }

    public static Uri bitmap2uri(Context c, Bitmap b) {
        File path = new File(c.getCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
        try {
            OutputStream os = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            return Uri.fromFile(path);
        } catch (Exception ignored) {
        }
        return null;
    }
}
