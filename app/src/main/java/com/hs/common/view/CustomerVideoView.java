package com.hs.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @Author: ljf
 * 时间:2019/7/2
 * 简述:<功能简述>
 */
public class CustomerVideoView extends VideoView {

    public CustomerVideoView(Context context) {
        super(context);
    }

    public CustomerVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 其实就是在这里做了一些处理。
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
