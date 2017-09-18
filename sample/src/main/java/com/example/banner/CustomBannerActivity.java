package com.example.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.widget.banner.Banner;

public class CustomBannerActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_banner_user);

        banner = (Banner) findViewById(R.id.banner);

        banner  //创建布局
                .createView(new Banner.CreateViewCallBack() {
                    @Override
                    public View createView(Context context) {

                        View view = LayoutInflater.from(context).inflate(R.layout.custom_banner_page, null);

                        return view;
                    }
                })
                //布局处理
                .bindView(new Banner.BindViewCallBack<View, BanneModel>() {

                    @Override
                    public void bindView(View view, BanneModel data, int position) {

                        ImageView imageView = view.findViewById(R.id.iv_image);
                        TextView tvTitle = view.findViewById(R.id.tv_title);

                        Glide.with(view.getContext()).load(data.getUrl()).into(imageView);
                        tvTitle.setText(data.getTitle());
                    }

                })
                //点击事件
                .setOnClickBannerListener(new Banner.OnClickBannerListener<View, BanneModel>() {

                    @Override
                    public void onClickBanner(View view, BanneModel data, int position) {
                        Toast.makeText(view.getContext(), "click position ：" + position + "\n标题：" + data.getTitle(), Toast.LENGTH_SHORT).show();
                    }

                })
                //填充数据
                .execute(DateBox.banneModels());
    }
}
