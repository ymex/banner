package com.example.banner.indicator;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.banner.R;
import com.example.banner.entity.BanneModel;

import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.core.IndicatorAble;

/**
 * 实现Banner.IndicatorAble 接口的布局都可以作为 指示器
 */

public class TitleIndicatorLayout extends FrameLayout implements IndicatorAble {

    private TextView tvTitle;
    private TextView tvPage;
    private View view;

    public TitleIndicatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public TitleIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleIndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

            view = inflate(getContext(), R.layout.custom_banner_indicator, null);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(view, params);

    }


    @Override
    public void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onBannerScrollStateChanged(int state) {

    }

    @Override
    public void onBannerSelected(int position, int size, Object object) {
        tvTitle.setText(((BanneModel)object).getTitle());
        tvPage.setText((position+1)+"/"+size);
    }

    /**
     *
     * @param size
     */
    @Override
    public void initIndicator(int size) {

        tvTitle = view.findViewById(R.id.tv_title);
        tvPage = view.findViewById(R.id.tv_page);
    }
}
