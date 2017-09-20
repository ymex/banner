package com.example.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;

public class VerticalBannerActivity extends AppCompatActivity  implements View.OnClickListener{
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_banner_user);

        findViewById(R.id.btn_v).setOnClickListener(this);
        findViewById(R.id.btn_h).setOnClickListener(this);

        banner = (Banner) findViewById(R.id.banner);

        banner  //创建布局 (当不实现 CreateViewCallBack时使用的是默认布局
                //.createView()
                //布局处理
                .bindView(new BindViewCallBack() {
                    @Override
                    public void bindView(View view, Object data, int position) {
                        Glide.with(view.getContext()).load(((BanneModel) data)
                                .getUrl()).into((AppCompatImageView) view);
                    }
                })
                //点击事件
                .setOnClickBannerListener(new OnClickBannerListener<View, BanneModel>() {

                    @Override
                    public void onClickBanner(View view, BanneModel data, int position) {
                        Toast.makeText(view.getContext(), "click position ：" + position + "\n标题：" + data.getTitle(), Toast.LENGTH_SHORT).show();
                    }

                })
                //填充数据
                .execute(DateBox.banneModels());



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_v:
                banner.setOrientation(Banner.VERTICAL);
                banner.execute(DateBox.banneModels());
                break;
            case R.id.btn_h:
                banner.setOrientation(Banner.HORIZONTAL);
                banner.execute(DateBox.banneModels());
                break;
        }
    }
}
