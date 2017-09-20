package com.example.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import cn.ymex.widget.banner.RecyclerBanner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;

public class RecyclerBannerActivity extends AppCompatActivity {

    RecyclerBanner recyclerBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_banner);
        recyclerBanner = (RecyclerBanner) findViewById(R.id.rccycler_banner);
        recycleBanner(recyclerBanner);
    }

    private void recycleBanner(RecyclerBanner bannerView) {
        bannerView.bindView(new BindViewCallBack() {
                    @Override
                    public void bindView(View view, Object data, int position) {
                        ImageView imageView = view.findViewById(R.id.imageView);
                        TextView textView = view.findViewById(R.id.title);
                        BanneModel entity = (BanneModel) data;

                        textView.setText(entity.getTitle());
                        Glide.with(view.getContext())
                                .load(entity.getUrl())
                                .into(imageView);
                    }
                })
                .createView(new CreateViewCallBack() {
                    @Override
                    public View createView(Context context, ViewGroup parent, int viewType) {
                        return LayoutInflater.from(context).inflate(R.layout.recycle_banner_item, parent, false);
                    }
                })
                .setOnClickBannerListener(new OnClickBannerListener() {
                    @Override
                    public void onClickBanner(View view, Object data, int position) {
                        Toast.makeText(RecyclerBannerActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                    }
                }).execute(DateBox.banneModels());
    }

}
