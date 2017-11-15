package com.example.banner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.banner.adapter.RecyclerViewAdapter;
import com.example.banner.entity.ItemActionModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContent;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setAdapter(adapter = new RecyclerViewAdapter());
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        adapter.setDatas(recyclerData());
    }

    private List<ItemActionModel> recyclerData() {
        return new ArrayList<ItemActionModel>() {{
            add(new ItemActionModel("banner"));
            add(new ItemActionModel("默认banner使用").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(DefaultBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("Gallery Banner").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(GalleryActivity.class.getName());
                }
            }));
            add(new ItemActionModel("定制banner").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(CustomBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("banner滚动方向/垂直/水平").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(VerticalBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("动画支持").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(AnimationBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("默认指示器使用").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(DefaultBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("定制指示器").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(IndicatorBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("RecyclerBanner使用").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(RecyclerBannerActivity.class.getName());
                }
            }));
            add(new ItemActionModel("ListView Header").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/ymex/banner");
                    intent.setData(content_url);
                }
            }));
            add(new ItemActionModel("bann方法及参数介绍").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAction(ListviewBannerActivity.class.getName());
                }
            }));

        }};
    }

    private void startAction(String act) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), act);
        startActivity(intent);
    }

}
