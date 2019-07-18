package com.sendcloud.jigsawpuzzle.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;

import com.sendcloud.jigsawpuzzle.help.GameInterface;
import com.sendcloud.jigsawpuzzle.page.PuzzlePage;
import com.sendcloud.jigsawpuzzle.util.GameUtil;
import com.sendcloud.jigsawpuzzle.util.ImgBean;

public class ScrollGridView extends GridView {

    public static int Gesture_Top = 1;
    public static int Gesture_RIGHT = 2;
    public static int Gesture_DOWN = 3;
    public static int Gesture_LEFT = 4;

    private boolean isReady = true ;

    private GameInterface mInterface;

    int blankPosition = -1;

    public ScrollGridView(Context context) {
        this(context, null);
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPosition(int position){
        this.blankPosition = position ;
    }

    public void setInterface(GameInterface anInterface) {
        mInterface = anInterface;
    }

    public void setGesture(int gesture){
        int type = PuzzlePage.GAME_TYPE;

        int i = blankPosition / type ;
        int j = (blankPosition+1) % type ;

        if (gesture == Gesture_Top){
            if (i != (type-1)){//to judge the segment can be remove or not
                startAnimator(blankPosition+type, "translationY");
            }
        }
        if (gesture == Gesture_DOWN){
            if (i != 0){
                startAnimator(blankPosition-type, "translationY");
            }
        }
        if (gesture == Gesture_LEFT){
            if (j != 0){
                startAnimator(blankPosition+1, "translationX");
            }
        }
        if (gesture == Gesture_RIGHT){
            if (j != 1){
                startAnimator(blankPosition-1, "translationX");
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false ;
    }

    private void startAnimator(final int childId, String type) {
        if (!isReady){
            return;
        }
        getChildAt(blankPosition).setVisibility(INVISIBLE);
        float distance = 0f;
        if (type == "translationX"){
            distance = getChildAt(blankPosition).getX() - getChildAt(childId).getX();
        }
        if (type == "translationY"){
            distance = getChildAt(blankPosition).getY() - getChildAt(childId).getY();
        }

        final int x = (int) getChildAt(childId).getTranslationX();
        final int y = (int) getChildAt(childId).getTranslationY();

        ObjectAnimator animator = ObjectAnimator.ofFloat(getChildAt(childId), type,
                0, distance);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isReady = false;
                mInterface.addStep();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isReady = true;

                GameUtil.swapItems(GameUtil.mImgBeans.get(childId),
                        GameUtil.mBlankImgBean);

                PuzzlePage.adapter.notifyDataSetChanged();

                getChildAt(childId).setTranslationX(x);
                getChildAt(childId).setTranslationY(y);

                getChildAt(blankPosition).setVisibility(VISIBLE);

                blankPosition = childId ;

                if (isSuccess()){
                    mInterface.isSuccessful();
                }
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    private boolean isSuccess(){
        for (ImgBean tempBean : GameUtil.mImgBeans){
            Log.d("TAG","PuzzlePage.GAME_TYPE"+PuzzlePage.GAME_TYPE);
            if(tempBean.getmBitmapId() != 0 && tempBean.getmItemId() == tempBean.getmBitmapId()){
                continue;
            } else if (tempBean.getmBitmapId() == 0 && (tempBean.getmItemId() == (PuzzlePage.GAME_TYPE * PuzzlePage.GAME_TYPE))){
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}