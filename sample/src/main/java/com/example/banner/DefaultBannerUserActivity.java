package com.example.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.banner.Banner;

public class DefaultBannerUserActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_banner_user);

        banner = (Banner) findViewById(R.id.banner);
//        banner.bindView(new Banner.BindViewCallBack() {
//            @Override
//            public void bindView(View view, Object data, int position) {
//                不使用泛型时你需要强转你的组件 与 数据类型
//                Glide.with(view.getContext()).load(((BanneModel)data).getUrl()).into((AppCompatImageView)view);
//            }
//        });
        banner.bindView(new Banner.BindViewCallBack<AppCompatImageView, BanneModel>() {
            @Override
            public void bindView(AppCompatImageView view, BanneModel data, int position) {
                //使用glide 加载图片到 view组件，data 是你的数据 。
                Glide.with(view.getContext()).load(data.getUrl()).into(view);
            }
        }).execute(DateBox.banneModels());
    }
}
