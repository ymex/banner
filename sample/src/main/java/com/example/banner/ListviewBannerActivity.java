package com.example.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import java.util.ArrayList;
import java.util.List;

import cn.ymex.pure.adapter.ListViewAdapter;
import cn.ymex.widget.banner.Banner;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;

public class ListviewBannerActivity extends AppCompatActivity {
    private Banner banner;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_banner_user);
        listView = (ListView) findViewById(R.id.listview);
        ListViewAdapter adapter = new LAdapter();
        View header = LayoutInflater.from(this).inflate(R.layout.layout_item_header, null, false);





        banner = header.findViewById(R.id.banner);
        banner.bindView(new BindViewCallBack<AppCompatImageView, BanneModel>() {

            @Override
            public void bindView(AppCompatImageView view, BanneModel data, int position) {//图片处理
                //使用glide 加载图片到 view组件，data 是你的数据 。
                Glide.with(view.getContext()).load(data.getUrl()).into(view);
            }

        }).setOnClickBannerListener(new OnClickBannerListener<AppCompatImageView, BanneModel>() {

            @Override
            public void onClickBanner(AppCompatImageView view, BanneModel data, int position) {//点击事件
                Toast.makeText(view.getContext(), "click position ：" + position +"\n标题："+ data.getTitle(), Toast.LENGTH_SHORT).show();
            }

        }).execute(DateBox.banneModels());//填充数据

        adapter.setData(adapterData());
        listView.setAdapter(adapter);

        listView.addHeaderView(header);
    }



    private List<String> adapterData() {
        return new ArrayList<String>() {{
            add("banner");//0
            add("banner");//0
            add("banner");//0
        }};
    }

    public static class LAdapter extends ListViewAdapter<String, ListViewAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            return ViewHolder.create(parent, R.layout.recyclerview_item_title);
        }

        @Override
        public void onBindViewHolder(int position, View convertView, ViewGroup parent, ViewHolder hold) {
            TextView textView = convertView.findViewById(R.id.tv_title);
            textView.setText(getItem(position));
        }
    }

}
