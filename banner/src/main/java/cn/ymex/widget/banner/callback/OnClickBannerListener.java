package cn.ymex.widget.banner.callback;

/**
 * Created by ymexc on 2017/9/20.
 */


import android.view.View;

/**
 * 点击事件
 */
public interface OnClickBannerListener<V extends View, T extends Object> {
    void onClickBanner(V view, T data, int position);
}