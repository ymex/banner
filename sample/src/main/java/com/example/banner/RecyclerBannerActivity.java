package com.example.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
                        Toast.makeText(RecyclerBannerActivity.this, "position: " + ((BanneModel) data).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }).execute(DateBox.banneModels());
    }

    public void onAnimationClick(View view) {

        final RecyclerBanner.LoopRecyclerViewPager recyclerView = recyclerBanner.getRecyclerView();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = recyclerView.getChildCount();
                int width = recyclerView.getChildAt(0).getWidth();
                int padding = (recyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });
    }
}
