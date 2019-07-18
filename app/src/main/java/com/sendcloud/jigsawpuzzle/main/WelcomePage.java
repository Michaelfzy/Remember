package com.sendcloud.jigsawpuzzle.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cardview_test.MainActivity;
import com.example.cardview_test.R;
import com.sendcloud.jigsawpuzzle.page.PuzzlePage;
import com.sendcloud.jigsawpuzzle.page.TakePicturesPage;
import com.sendcloud.jigsawpuzzle.util.SizeHelper;
import com.sendcloud.jigsawpuzzle.util.StatusBarUtil;
import com.sendcloud.jigsawpuzzle.util.Typefaces;

public class WelcomePage extends Activity {

    private LinearLayout group ;
    private Button button1, button2, button3 ;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    AlphaAnimation animation = new AlphaAnimation(0, 1);//布局持续动画的渐变效果，开始透明度为0，最终透明度为1
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            setButtonEbable(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    animation.setDuration(1000);//动画持续效果
                    group.startAnimation(animation);
                    group.setAlpha(1);
                    break;
                case 2:

                    Intent intent2 = new Intent(WelcomePage.this, TakePicturesPage.class);
                    startActivity(intent2);
                    break;
            }
        }
    };







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(this);//修改状态栏为全透明
        setContentView(R.layout.activity_splash);

        initView();//initialize the button linearlayout

        setTextTypeface();//initialize the text display

        new Thread(start).start();
    }

    private void setTextTypeface() {
        TextView splashTitle = (TextView) findViewById(R.id.splash_title);
        TextView youKe = (TextView) findViewById(R.id.youke);
        TextView version = (TextView) findViewById(R.id.version);

        SizeHelper.prepare(this);
        splashTitle.setTextSize(SizeHelper.fromPx(30));
        Log.d("TAG","size " + splashTitle.getTextSize());
        youKe.setTextSize(SizeHelper.fromPx(12));
        youKe.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        version.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
    }

    private void initView() {
        group = (LinearLayout) findViewById(R.id.button_group) ;
        group.setAlpha(0);//设置布局透明度

        button1 = (Button) findViewById(R.id.button1) ;
        button2 = (Button) findViewById(R.id.button2) ;
        button3 = (Button) findViewById(R.id.button3) ;

        setButtonEbable(false);
    }

    private void setButtonEbable(boolean enable){
        button1.setEnabled(enable);
        button2.setEnabled(enable);
        button3.setEnabled(enable);
    }

    Runnable start = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 1 && data != null){

                Log.d("data","data+"+data.getData());
                Cursor cursor = this.getContentResolver().query(
                        data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(
                        cursor.getColumnIndex("_data"));
                Log.d("data","data-"+imagePath);
                Intent intent = new Intent(WelcomePage.this, PuzzlePage.class);//skip into the puzzle interface
                intent.putExtra("imagePath", imagePath);
                cursor.close();
                startActivity(intent);
            }
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button1:
                final AlertDialog.Builder builder = new AlertDialog.Builder(WelcomePage.this);
                builder.setTitle("难度选择：");
                builder.setSingleChoiceItems(new String[]{"初级", "中级", "高级"}, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which){
                                    case 0:
                                        PuzzlePage.GAME_TYPE = 3;
                                        break;
                                    case 1:
                                        PuzzlePage.GAME_TYPE = 4;
                                        break;
                                    case 2:
                                        PuzzlePage.GAME_TYPE = 5;
                                        break;
                                }
                            }
                        });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new AlertDialog.Builder(WelcomePage.this).setTitle("选择图片").setItems(
                                new String[] { "从图库中选择", "自带图片" }, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        switch(which){
                                            /**
                                             * 从图库获取图片，通配符*遍历图片
                                             */
                                            case 0:
                                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                        "image/*");
                                                startActivityForResult(intent, 1);	//回调函数，返回值
                                                break;
                                            /**
                                             * 自带图片，handler机制
                                             */
                                            case 1:
                                                mHandler.sendEmptyMessage(2);

                                            break;
                                        }
                                    }
                                }).show();


                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.button2:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomePage.this);
                builder1.setMessage("拼图游戏可以选择图片类型，从图片列表中选择一张开始，" +
                        "进入打乱的图片页面，点击一片要移动的图片，再选择紧邻的（上下左右均可，" +
                        "斜对不可）图片，两张图片就对换位置了，继续这样的操作，直到拼完。");
                builder1.setTitle("游戏帮助");

                builder1.create().show();
                break;
            case R.id.button3:
                new AlertDialog.Builder(this).setMessage("你确定要退出游戏？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Intent MyIntent = new Intent(WelcomePage.this, MainActivity.class);
                        startActivity(MyIntent);

                    }

                }).show();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if(keyCode == KeyEvent.KEYCODE_BACK) {

                    new AlertDialog.Builder(this).setMessage("你确定要退出游戏？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            Intent MyIntent = new Intent(WelcomePage.this, MainActivity.class);
                            startActivity(MyIntent);


                        }

                    }).show();

        }
        return true;
    }


}
