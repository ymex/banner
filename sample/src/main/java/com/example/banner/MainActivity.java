package com.example.banner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;

import java.util.ArrayList;
import java.util.List;

import cn.ymex.banner.Banner;

public class MainActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = (Banner) findViewById(R.id.banner);
        banner.bindView(new Banner.BindViewCallBack<AppCompatImageView, BanneModel>() {
            @Override
            public void bindView(AppCompatImageView view, BanneModel data, int position) {
                //图片加载
                Glide.with(view.getContext())
                        .load(data.getUrl())
                        .into(view);
            }
        }).execute(data());
    }

    private List<BanneModel> data() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-07/29/0_admin_upload_1501307922564.png", "推动合规建设"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-08/30/0_admin_upload_1504077511668.png", "会员体系大升级"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-06/27/0_admin_upload_1498548797754.jpg", "红包大派对"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-08/11/0_admin_upload_1502445455975.png", "兑换抽奖"));
        }};
    }
}
