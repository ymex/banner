package cn.ymex.widget.banner.callback;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.ymex.widget.banner.callback.CreateViewCallBack;

/**
 * 默认加载布局
 */
public class CreateViewCaller implements CreateViewCallBack<FrameLayout> {
    private ImageView.ScaleType scaleType;

    public static CreateViewCaller build() {
        return new CreateViewCaller(ImageView.ScaleType.FIT_XY);
    }

    public static CreateViewCaller build(ImageView.ScaleType scaleType) {
        return new CreateViewCaller(scaleType);
    }


    public CreateViewCaller(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public static AppCompatImageView findImageView(View view) {
        if (view instanceof ViewGroup) {
            return (AppCompatImageView) ((ViewGroup) view).getChildAt(0);
        }
        return null;
    }

    @Override
    public FrameLayout createView(Context context, ViewGroup parent, int viewType) {
        return createImageView(context);
    }


    private FrameLayout createImageView(Context context) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        FrameLayout layout = new FrameLayout(context);
        layout.setLayoutParams(params);

        AppCompatImageView view = new AppCompatImageView(context);
        view.setLayoutParams(params);
        view.setScaleType(scaleType);

        layout.addView(view, params);
        return layout;
    }
}
