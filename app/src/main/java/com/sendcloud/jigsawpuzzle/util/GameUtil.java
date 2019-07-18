package com.sendcloud.jigsawpuzzle.util;

import android.graphics.Bitmap;

import com.sendcloud.jigsawpuzzle.page.PuzzlePage;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {

    public static List<ImgBean> mImgBeans = new ArrayList<>();
    public static Bitmap mLastBitmap;
    public static ImgBean mBlankImgBean;
    public static int blankPosition = -1;

    private static int getInversion(List<Integer> list){
        int inversion = 0;
        int inversionCount = 0;

        for (int i=0; i<list.size(); i++){
            for(int j=i+1; j<list.size(); j++){
                int index = list.get(i);
                if(list.get(j) != 0 && list.get(j) < index) {
                    inversionCount++;
                }
            }
            inversion += inversionCount;
            inversionCount = 0;
        }
        return inversion;
    }

    public static boolean canSolve(List<Integer> list){//是否可以完成拼图，拼图算法的判断
        int blankId = mBlankImgBean.getmItemId();
        if(list.size() % 2 ==1) {
            return getInversion(list) % 2 == 0;
        } else {
            if(((blankId -1)/PuzzlePage.GAME_TYPE)%2 == 1){
                return getInversion(list)%2 == 0;
            }else{
                return getInversion(list)%2 == 1;
            }
        }
    }

    public static void getPuzzleGenertor(){//the initial state will be disorganized
        int index = 0;

        for(int i=0; i<mImgBeans.size() * 2; i++){
            index = (int)(Math.random() * PuzzlePage.GAME_TYPE * PuzzlePage.GAME_TYPE);
            swapItems(mImgBeans.get(index), GameUtil.mBlankImgBean);
        }

        blankPosition = index ;

        List<Integer> list = new ArrayList<>();

        for (int i=0; i<mImgBeans.size(); i++){
            list.add(mImgBeans.get(i).getmBitmapId());
        }

        if (canSolve(list)){
            return;
        } else {
            getPuzzleGenertor();
        }
    }

    public static void swapItems(ImgBean from, ImgBean blank){
        ImgBean tempImgBean = new ImgBean();

        tempImgBean.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(tempImgBean.getmBitmapId());

        tempImgBean.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(tempImgBean.getmBitmap());

        GameUtil.mBlankImgBean = from;
    }
}
