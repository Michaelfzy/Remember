package com.sendcloud.jigsawpuzzle.help;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cardview_test.R;
import com.sendcloud.jigsawpuzzle.util.GameUtil;
import com.sendcloud.jigsawpuzzle.util.ImgBean;

import java.util.List;

public class PuzzleAdapter extends BaseAdapter{
    private Context context;
    private List<ImgBean> picList;

    public PuzzleAdapter(Context context, List<ImgBean> picList){
        super();
        this.context = context;
        this.picList = picList;
    }

    @Override
    public int getCount() {
        if(null != GameUtil.mImgBeans)
            return GameUtil.mImgBeans.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return GameUtil.mImgBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView pic = null;

        if (null == convertView){
            pic = new ImageView(context);
            pic.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            pic.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            pic = (ImageView) convertView;
        }

        pic.setBackgroundResource(R.drawable.img_item_bg);

        pic.setImageBitmap(picList.get(position).getmBitmap());

        return pic ;
    }
}
