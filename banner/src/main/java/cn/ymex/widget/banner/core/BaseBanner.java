package cn.ymex.widget.banner.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.ymex.banner.R;
import cn.ymex.widget.banner.callback.BindViewCallBack;
import cn.ymex.widget.banner.callback.CreateViewCallBack;
import cn.ymex.widget.banner.callback.OnClickBannerListener;


public abstract class BaseBanner<T extends BaseBanner> extends FrameLayout {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private List<Object> mData;
    protected Handler mHandler;
    protected int mCurrentItem;

    protected int interval = 5 * 1000;//间隔-毫秒
    protected boolean isAutoPlay = true;//自动播放
    protected boolean isVertical = false;//纵向滚动
    protected boolean isLoop = true;//是否循环滚动


    protected IndicatorAble mIndicatorAble;
    protected OnClickBannerListener onClickBannerListener;
    protected CreateViewCallBack createViewCallBack;
    protected BindViewCallBack bindViewCallBack;

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        dealAttrs(context, attrs);
    }

    public T setOnClickBannerListener(OnClickBannerListener onClickBannerListener) {
        this.onClickBannerListener = onClickBannerListener;
        return (T) this;
    }

    public T createView(CreateViewCallBack listener) {
        this.createViewCallBack = listener;
        return (T) this;
    }

    public T bindView(BindViewCallBack bindViewCallBack) {
        this.bindViewCallBack = bindViewCallBack;
        return (T) this;
    }

    public List<Object> getBannerData() {
        if (mData == null) {
            mData = new ArrayList<Object>();
        }
        return mData;
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    /**
     * 设置指示器
     *
     * @param mIndicatorAble
     * @return
     */
    public T setIndicatorable(IndicatorAble mIndicatorAble) {
        this.mIndicatorAble = mIndicatorAble;
        return (T) this;
    }

    public abstract T setOrientation(int orientation);

    public abstract T setLoop(boolean loop);

    private void dealAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        interval = typedArray.getInt(R.styleable.Banner_banner_interval, interval);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_banner_auto_play, isAutoPlay);
        isLoop = typedArray.getBoolean(R.styleable.Banner_banner_loop, isLoop);
        isVertical = (typedArray.getInt(R.styleable.Banner_banner_orientation, 0) == VERTICAL);
        typedArray.recycle();
    }

    /**
     * banner 默认布局
     *
     * @param context context
     * @return AppCompatImageView
     */
    protected AppCompatImageView createImageView(Context context) {
        AppCompatImageView view = new AppCompatImageView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setScaleType(AppCompatImageView.ScaleType.FIT_XY);
        return view;
    }

    public abstract <D extends Object> void execute(List<D> datas);

    protected abstract int positionIndex(int postion);

    public abstract void startAutoPlay();

    public abstract void stopAutoPlay();

    public BaseBanner(@NonNull Context context) {
        super(context);
    }

    public BaseBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof IndicatorAble) {
                setIndicatorable((IndicatorAble) view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://0
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL://3
                case MotionEvent.ACTION_UP://1
                case MotionEvent.ACTION_OUTSIDE://4
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    /**
     * 当视图不可见时不滚动
     *
     * @param visibility visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAutoPlay();
        } else if (visibility == VISIBLE) {
            startAutoPlay();
        }
        super.onWindowVisibilityChanged(visibility);
    }
}
