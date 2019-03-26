package com.example.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;

public class DefaultBannerActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_banner_user);

        banner = (Banner) findViewById(R.id.banner);

        banner.bindView(new BindViewCallBack<ImageView, BanneModel>() {
            @Override
            public void bindView(ImageView view, BanneModel data, int position) {//图片处理
                Glide.with(view.getContext()).load(data.getUrl()).into(view);
            }

        }).setOnClickBannerListener(new OnClickBannerListener<View, BanneModel>() {

            @Override
            public void onClickBanner(View view, BanneModel data, int position) {//点击事件
                Toast.makeText(view.getContext(), "click position ：" + position + "\n标题：" + data.getTitle(), Toast.LENGTH_SHORT).show();
            }

        }).execute(DateBox.banne1Model());//填充数据
    }

    public void onSelect3(View view) {
        banner.setCurrentItem(3);
    }
}
