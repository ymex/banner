package com.example.banner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;

public class GuidePageActivity extends AppCompatActivity {

    List<Integer> guides = Arrays.asList(new Integer[]{R.mipmap.g1,R.mipmap.g2, R.mipmap.g3, R.mipmap.g4});

    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);

        banner = (Banner) findViewById(R.id.banner);

        banner.setPageTransformer(new StackTransformer())
                .bindView(new BindViewCallBack<AppCompatImageView, Integer>() {
            @Override
            public void bindView(AppCompatImageView view, Integer data, int position) {
                Glide.with(view.getContext()).load(data).into(view);
            }
        }).execute(guides);


        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (isLast(state,banner)) {
                    startActivity(new Intent(GuidePageActivity.this, MainActivity.class));
                    GuidePageActivity.this.finish();
                }
            }
        });
    }

    /**
     * 判断最后一条目的滑动方向
     *
     * @return
     */
    List<Integer> statuses = new ArrayList<Integer>();
    private boolean isLast(int state,Banner  banner) {
        this.statuses.add(state);
        int len = statuses.size();
        return  len >= 2
                && statuses.get(len - 1) == ViewPager.SCROLL_STATE_IDLE
                && statuses.get(len - 2) == ViewPager.SCROLL_STATE_DRAGGING
                && banner.getBannerPage().getCurrentItem() == banner.getBannerData().size() - 1;
    }
}
