package com.example.banner.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.banner.R;
import com.example.banner.entity.BanneModel;
import com.example.banner.entity.DateBox;

import java.util.List;

import cn.ymex.widget.banner.Banner;

/**
 * Created by ymex on 2017/9/10.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final int TYPE_BANNER = 0x5;
    public static final int TYPE_ITEM = 0x6;

    private List<String> datas;


    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_BANNER;
        }
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_banner, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_title, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            fillBanner(holder.banner);
        } else if (getItemViewType(position) == TYPE_ITEM) {
            holder.tvTitle.setText(datas.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.itemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        Banner banner;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            banner = itemView.findViewById(R.id.banner);
        }
    }
    private boolean isVertical = false;
    public void setBannerDirection(boolean vertical) {
        isVertical = vertical;
        notifyItemChanged(0);
    }

    private void fillBanner(Banner banner) {

        banner.bindView(new Banner.BindViewCallBack<AppCompatImageView, BanneModel>() {
            @Override
            public void bindView(AppCompatImageView view, BanneModel data, int position) {
                //图片加载
                Glide.with(view.getContext())
                        .load(data.getUrl())
                        .into(view);
            }
        }).execute(DateBox.banneModels());
    }



    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void itemClick(int position);
    }
}

