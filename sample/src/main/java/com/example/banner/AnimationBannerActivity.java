package com.example.banner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.*;
import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;
import com.example.banner.transformer.CustPagerTransformer;

import cn.ymex.banner.Banner;

public class AnimationBannerActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_banner_user);

        banner = (Banner) findViewById(R.id.banner);

        banner.bindView(new Banner.BindViewCallBack<AppCompatImageView, BanneModel>() {

            @Override
            public void bindView(AppCompatImageView view, BanneModel data, int position) {//图片处理
                //使用glide 加载图片到 view组件，data 是你的数据 。
                Glide.with(view.getContext()).load(data.getUrl()).into(view);
            }

        }).setOnClickBannerListener(new Banner.OnClickBannerListener<AppCompatImageView, BanneModel>() {

            @Override
            public void onClickBanner(AppCompatImageView view, BanneModel data, int position) {//点击事件
                Toast.makeText(view.getContext(), "click position ：" + position + "\n标题：" + data.getTitle(), Toast.LENGTH_SHORT).show();
            }

        }).setPageTransformer(new CustPagerTransformer(this))//动画
                .execute(DateBox.banneModels());//填充数据


        findViewById(R.id.btn_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchAnimation();
            }
        });
    }

    /**
     * 使用第三方的viewpage 动画效果
     * 水平动画效果：compile 'com.ToxicBakery.viewpager.transforms:view-pager-transforms:1.2.32@aar'
     */
    private void switchAnimation() {
        banner.setPageTransformer(getTransformer());
    }

    private int transIndex = 0;

    ViewPager.PageTransformer getTransformer() {
        ++transIndex;
        if (transIndex == 16) {
            transIndex = 0;
        }
        switch (transIndex) {
            case 0:
                return new AccordionTransformer();
            case 1:
                return new BackgroundToForegroundTransformer();
            case 2:
                return new CubeInTransformer();
            case 3:
                return new CubeOutTransformer();
            case 4:
                return new DefaultTransformer();
            case 5:
                return new DepthPageTransformer();
            case 6:
                return new FlipHorizontalTransformer();
            case 7:
                return new FlipVerticalTransformer();
            case 8:
                return new RotateUpTransformer();
            case 9:
                return new RotateDownTransformer();
            case 10:
                return new ScaleInOutTransformer();
            case 11:
                return new StackTransformer();
            case 12:
                return new TabletTransformer();
            case 13:
                return new ZoomInTransformer();
            case 14:
                return new ZoomOutTranformer();
            case 15:
                return new ZoomInTransformer();
            default:
                return new DefaultTransformer();
        }
    }
}
