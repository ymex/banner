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

import cn.ymex.kits.widget.Toaster;
import cn.ymex.widget.banner.RecyclerBanner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;
import cn.ymex.widget.banner.pager.RecyclerViewPager;

public class RecyclerBannerActivity extends AppCompatActivity {

    RecyclerBanner recyclerBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_banner);
        recyclerBanner = (RecyclerBanner) findViewById(R.id.rccycler_banner);
        recycleBanner(recyclerBanner);
    }

    RecyclerViewPager mRecyclerView;

    private void recycleBanner(final RecyclerBanner bannerView) {
        //留边
        //marginSide(bannerView);


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
                        Toaster.show("position: " + position +
                                " title:" + ((BanneModel) data).getTitle() +
                                "   CurrentItem:" + bannerView.getCurrentItem());
                    }
                }).execute(DateBox.banneModels());
    }

    private void marginSide(RecyclerBanner bannerView) {


        mRecyclerView = bannerView.getPageView();
        mRecyclerView.setPadding(130, 0, 130, 0);

        mRecyclerView.setClipToPadding(false);

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerView.getChildCount() < 3) {
                    if (mRecyclerView.getChildAt(1) != null) {
                        if (mRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mRecyclerView.getChildAt(0) != null) {
                        View v0 = mRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mRecyclerView.getChildAt(2) != null) {
                        View v2 = mRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
//
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

    }

    public void onAnimationClick(View view) {

        final RecyclerBanner.LoopRecyclerViewPager recyclerView = (RecyclerBanner.LoopRecyclerViewPager) recyclerBanner.getPageView();

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

    public void onSelect3(View view) {
        recyclerBanner.setCurrentItem(3);
    }
}
