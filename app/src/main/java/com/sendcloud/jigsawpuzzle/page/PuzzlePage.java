package com.sendcloud.jigsawpuzzle.page;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cardview_test.R;
import com.sendcloud.jigsawpuzzle.help.GameInterface;
import com.sendcloud.jigsawpuzzle.help.PuzzleAdapter;
import com.sendcloud.jigsawpuzzle.main.WelcomePage;
import com.sendcloud.jigsawpuzzle.util.GameUtil;
import com.sendcloud.jigsawpuzzle.util.ImagesUtil;
import com.sendcloud.jigsawpuzzle.util.ScreenUtils;
import com.sendcloud.jigsawpuzzle.util.SizeHelper;
import com.sendcloud.jigsawpuzzle.view.ScrollGridView;

import java.util.Timer;
import java.util.TimerTask;

public class PuzzlePage extends Activity {

    public static int GAME_TYPE = 3;

    private int screenWidth ;

    private ScrollGridView gridView;
    private TextView timer, stepCount ;

    private ImageButton look ;

    private Bitmap mBitmap;

    private int time = -1 ;
    private int step = 0 ;

    private GestureDetector mGestureDetector ;
    public static PuzzleAdapter adapter;

    private SoundPool spool;
    private int id;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private ImageView mImageView;

    private MediaPlayer playerbg;
    private int playerbgstate = 0;

    private AudioManager audio;

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.d("successful", "true");
                    timer.setEnabled(false);

                    MediaPlayer mMediaPlayer;
                    mMediaPlayer=MediaPlayer.create(PuzzlePage.this, R.raw.gongxi);
                    mMediaPlayer.start();

                    new AlertDialog.Builder(PuzzlePage.this).setMessage("恭喜您拼图成功，您用了"+ time + "秒，" + step + "步").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(PuzzlePage.this, WelcomePage.class);
                            startActivity(intent);
                        }

                    }).setCancelable(false).show();
                    break;
                case 2:
                    timer.setText(time + "s");
                    break;
                case 3:
                    stepCount.setText(step + "");
                    break;
                case 4:
                    time = -1;
                    task.cancel();
                    startTimer();
                    step = 0;
                    stepCount.setText(step + "");
                    initGridView(i.getExtras().getString("imagePath"));


            }
        }
    };

    private Timer mTimer ;
    private TimerTask task ;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_maze);
        SizeHelper.prepare(this);

        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        startTimer();
        initView();
        id = initSoundpool();

        i = this.getIntent();
        initGridView(i.getExtras().getString("imagePath"));

        playerbg = MediaPlayer.create(this, R.raw.exciting);
        playerbg.setLooping(true);
        playerbg.start();
        playerbgstate = 1;
    }

    private void startTimer() {
        time = -1;
        mTimer = new Timer(true);

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                time++;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(task, 0, 1000);
    }

    private void initView() {
        mGestureDetector = new GestureDetector(this, listener);
        screenWidth = ScreenUtils.getScreenWidth(this);
        look = (ImageButton) findViewById(R.id.help1);
        look.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        creatMirrorImage();
                        break;
                    case MotionEvent.ACTION_UP:
                        deleteMirrorImage();
                        break;
                }
                return false;
            }
        });

        timer = (TextView) findViewById(R.id.timer) ;
        stepCount = (TextView) findViewById(R.id.step) ;

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.titlebar);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                SizeHelper.fromPx(74));
        layout.setLayoutParams(params);
    }

    private int initSoundpool() {
        //Sdk版本>=21时使用下面的方法
        if(Build.VERSION.SDK_INT>=21){
            SoundPool.Builder builder=new SoundPool.Builder();
            //设置最多容纳的流数
            builder.setMaxStreams(2);
            AudioAttributes.Builder attrBuilder=new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            spool=builder.build();
        }else{

            spool=new SoundPool(2,AudioManager.STREAM_MUSIC, 0);


        }
        //加载音频文件，返回音频文件的id
        int id=spool.load(getApplicationContext(),R.raw.huadong2,1);

        return id;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    GestureDetector.OnGestureListener listener = new
            GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
                    float x = e2.getX() - e1.getX() ;
                    float y = e2.getY() - e1.getY() ;

                    float x_abs = Math.abs(x);
                    float y_abs = Math.abs(y);

                    int gesture = 0;
                    if (x_abs >= y_abs){
                        if (x_abs >= screenWidth / 5){
                            if (x>0){
                                gesture = ScrollGridView.Gesture_RIGHT;
                            } else {
                                gesture = ScrollGridView.Gesture_LEFT;
                            }
                        }
                    } else {
                        if (y_abs >= screenWidth / 5){
                            if (y > 0){
                                gesture = ScrollGridView.Gesture_DOWN;
                            } else {
                                gesture = ScrollGridView.Gesture_Top;
                            }
                        }
                    }

                    gridView.setGesture(gesture);
                    spool.play(id, 1, 1, 0, 0, 1);

                    return true;
                }
            };

    private void initGridView(String imagePath) {
        gridView = (ScrollGridView) findViewById(R.id.grid_list);

        mBitmap = BitmapFactory.decodeFile(imagePath);//从文件中解析出一个位图对象，返回的位图是不可改变的。方法参数为位图文件的路径。返回的位图默认为不可改变的。

        ImagesUtil.createInitBitmaps(this, GAME_TYPE, mBitmap);//对传入图片进行处理，分割
        GameUtil.getPuzzleGenertor();//对分割完的图片进行打乱处理

        adapter = new PuzzleAdapter(this, GameUtil.mImgBeans);
        gridView.setNumColumns(GAME_TYPE);
        gridView.setAdapter(adapter);
//        Log.d("TAG","123456");
        gridView.setPosition(GameUtil.blankPosition);
        gridView.setInterface(new GameInterface() {
            @Override
            public void isSuccessful() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);

            }

            @Override
            public void addStep() {
                addStepCount();
                Log.d("TAG","123456");
            }
        });
    }

    private void addStepCount(){
        step ++ ;
        Message message = new Message();
        message.what = 3;
        mHandler.sendMessage(message);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.help:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PuzzlePage.this);
                builder1.setMessage("游戏图标操作功能依次为背景音乐控制、原图帮助、难度选择、生成新游戏，" +
                        "其中原图帮助需长按图标方能显示图片，松开后原图消失。"+
                        "进入打乱的图片页面，将空白处周围的图片移向空白处，继续这样的操作，直到拼完。"
                        );
                builder1.setTitle("游戏帮助");

                builder1.create().show();
                break;
            case R.id.fanhui:
                new AlertDialog.Builder(this).setTitle("确定要重新生成游戏么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                restartGames();
                            }
                        })
                        .setNegativeButton("返回",null).show();
                break;
            case R.id.jingyin:
                ImageButton bgm;
                bgm = (ImageButton)findViewById(R.id.jingyin);
                if(playerbgstate == 1){
                    bgm.setBackgroundDrawable(getResources().getDrawable(R.drawable.mute));
                    playerbgstate = -1;
                    playerbg.pause();
                }else if(playerbgstate == -1){
                    bgm.setBackgroundDrawable(getResources().getDrawable(R.drawable.withmusic));
                    playerbgstate = 1;
                    playerbg.start();
                }

                break;
            case R.id.guanqia:
                AlertDialog.Builder builder = new AlertDialog.Builder(PuzzlePage.this);
                builder.setTitle("难度选择：");
                builder.setSingleChoiceItems(new String[]{"3X3", "4X4", "5X5"}, 0,
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
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message message = new Message();
                        message.what = 4;
                        mHandler.sendMessage(message);
                    }
                }).show();



                break;
            case R.id.left_btn:
                if(playerbg != null){
                    playerbg.stop();
                    playerbg.release();
                    playerbg = null;
                }
                finish();
                Intent intent = new Intent(PuzzlePage.this, WelcomePage.class);
                startActivity(intent);
                break;
        }
    }

    private void creatMirrorImage() {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                gridView.setAlpha(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        animation.setDuration(800);
        gridView.startAnimation(animation);

        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        mWindowLayoutParams.x = gridView.getLeft() ;
        mWindowLayoutParams.y = gridView.getTop() ;

        mWindowLayoutParams.width = gridView.getWidth();
        mWindowLayoutParams.height = gridView.getHeight();
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        mImageView = new ImageView(this);
        mImageView.setImageBitmap(mBitmap);

        mWindowManager.addView(mImageView, mWindowLayoutParams);
    }

    private void deleteMirrorImage() {
        if (mImageView != null) {
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(500);
            gridView.startAnimation(animation);
            gridView.setAlpha(1);

            mWindowManager.removeView(mImageView);
        }
    }

    private void restartGames() {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                time = -1;
                task.cancel();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addStepCount();

                GameUtil.getPuzzleGenertor();
                gridView.setPosition(GameUtil.blankPosition);
                adapter.notifyDataSetChanged();
                AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        startTimer();
                        step = 0;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {}

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                animation1.setDuration(1000);
                gridView.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        animation.setDuration(1000);
        gridView.startAnimation(animation);
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 *退出程序
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP://增大音量
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN://减小音量
                audio.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_BACK:
                if(playerbg != null){
                    playerbg.stop();
                    playerbg.release();
                    playerbg = null;
                }
                finish();
                Intent intent = new Intent(PuzzlePage.this, WelcomePage.class);
                startActivity(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG","  onStart ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","  onStop ");
        if(playerbg != null){
            if (playerbgstate == 1) {
                playerbgstate = -1;
                playerbg.pause();
            }
        }

    }

}