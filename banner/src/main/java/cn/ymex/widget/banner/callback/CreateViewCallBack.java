package cn.ymex.widget.banner.callback;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建自定义view
 */
public interface CreateViewCallBack<T extends View> {
    /**
     * 创建自定义view
     *
     * @param context context
     * @return view
     */
    T createView(Context context, ViewGroup parent,int viewType);
}