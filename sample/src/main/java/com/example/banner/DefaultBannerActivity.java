package com.example.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.widget.viewpager.Banner;

public class DefaultBannerActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_banner_user);

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
                Toast.makeText(view.getContext(), "click position ：" + position +"\n标题："+ data.getTitle(), Toast.LENGTH_SHORT).show();
            }

        }).execute(DateBox.banneModels());//填充数据
    }
}
