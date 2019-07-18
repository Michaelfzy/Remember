package com.sendcloud.jigsawpuzzle.util;

import android.graphics.Bitmap;

public class ImgBean {
    private int mItemId;
    private int mBitmapId;
    private Bitmap mBitmap;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public int getmItemId() {
        return mItemId;
    }

    public void setmItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    public int getmBitmapId() {
        return mBitmapId;
    }

    public void setmBitmapId(int mBitmapId) {
        this.mBitmapId = mBitmapId;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public ImgBean(){}

    public ImgBean(int mItemId, int nmBitmapId, boolean isSelected, Bitmap mBitmap){
        this.mItemId = mItemId;
        this.mBitmapId = nmBitmapId;
        this.mBitmap = mBitmap;
    }

    @Override
    public String toString() {
        return mBitmapId+"";
    }
}
