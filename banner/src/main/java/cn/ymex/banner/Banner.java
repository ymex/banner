package cn.ymex.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * viewpage  banner
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {

    private BannerPager mBannerPage;
    private List<Object> mData;
    private List<View> mItemViews;
    private Handler mHandler;
    private HandlerTask mHandlerTask;
    private int mCurrentItem;

    private ViewPager.OnPageChangeListener onPageChangeListener;

    private int interval = 5 * 1000;//间隔-毫秒
    private boolean isAutoPlay = true;//自动播放
    private boolean isVertical = false;//纵向滚动

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        dealAttrs(attrs);
        mHandler = new Handler();
        mHandlerTask = new HandlerTask(this);
        mBannerPage = new BannerPager(getContext());
        mBannerPage.setVertical(isVertical);
        mBannerPage.setFocusable(true);
        mBannerPage.addOnPageChangeListener(this);
        addView(mBannerPage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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

    private void dealAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Banner);
        interval = typedArray.getInt(R.styleable.Banner_banner_interval, interval);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_banner_auto_play, isAutoPlay);
        isVertical = typedArray.getBoolean(R.styleable.Banner_banner_is_vertical, isVertical);
        typedArray.recycle();
    }


    public Banner createView(CreateViewCallBack listener) {
        this.createViewCallBack = listener;
        return this;
    }

    public Banner bindView(BindViewCallBack bindViewCallBack) {
        this.bindViewCallBack = bindViewCallBack;
        return this;
    }

    public Banner setOnClickBannerListener(OnClickBannerListener onClickBannerListener) {
        this.onClickBannerListener = onClickBannerListener;
        return this;
    }

    /**
     * 设置转换动画
     * @param reverseDrawingOrder  reverseDrawingOrder
     * @param transformer transformer
     * @return Banner
     */
    public Banner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        if (mBannerPage != null) {
            mBannerPage.setPageTransformer(reverseDrawingOrder,transformer);
        }
        return this;
    }

    public Banner setPageTransformer(ViewPager.PageTransformer transformer) {

        return setPageTransformer(true, transformer);
    }

    /**
     * 设置垂直滚动 ，此时PageTransformer会被重置
     * （原因，水平的PageTransformer可能不是你需要的,所以会重置成banner提供的垂直的PageTransformer。）
     * @param isVertical 垂直滚动
     * @return Banner
     */
    public Banner setVerticalModel(boolean isVertical) {
        this.isVertical = isVertical;
        if (mBannerPage != null) {
            mBannerPage.setVertical(this.isVertical);
        }
        return this;
    }

    /**
     * 设置指示器
     *
     * @param mIndicatorAble
     * @return
     */
    public Banner setIndicatorable(IndicatorAble mIndicatorAble) {
        this.mIndicatorAble = mIndicatorAble;
        return this;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 开始运行
     */
    public <T extends Object> void execute(List<T> imagesData) {
        stopAutoPlay();
        getItemViews().clear();
        getBannerData().clear();
        if (imagesData != null && imagesData.size() > 0) {
            getBannerData().addAll(imagesData);
        }
        generateItemViews();
        mBannerPage.setAdapter(new BannerPagerAdapter());
        if (getBannerData().size() > 0) {
            mCurrentItem = 1;
            mBannerPage.setCurrentItem(mCurrentItem);
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.initIndicator(getBannerData().size());
        }
        if (getBannerData().size() <= 1) {
            mBannerPage.setCanScroll(false);
        } else {
            mBannerPage.setCanScroll(true);
        }
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    public void startAutoPlay() {
        mHandler.removeCallbacks(mHandlerTask);
        mHandler.postDelayed(mHandlerTask, interval);
    }

    public void stopAutoPlay() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    private void generateItemViews() {
        boolean def = true;
        if (this.createViewCallBack != null && createViewCallBack.createView(getContext()) != null) {
            def = false;
        }

        int size = getBannerData().size();
        if (size <= 0) {
            return;
        }
        for (int i = 0; i <= size + 1; i++) {
            if (def) {
                this.createViewCallBack = new CreateViewCallBack<AppCompatImageView>() {
                    @Override
                    public AppCompatImageView createView(Context context) {
                        return createImageView(context);
                    }
                };
            }
            View view =  createViewCallBack.createView(getContext());
            getItemViews().add(view);
            int index = positionIndex(i);

            if (bindViewCallBack != null && getBannerData().size() > 0) {
                bindViewCallBack.bindView(view, getBannerData().get(index), index);
            }
            if (onClickBannerListener != null && getBannerData().size() > 0) {
                final int finalIndex = index;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickBannerListener.onClickBanner(view, getBannerData().get(finalIndex), finalIndex);
                    }
                });
            }
        }
    }

    private int positionIndex(int postion) {
        int count = getBannerData().size();
        int index = postion - 1;
        if (postion == 0) {
            index = count - 1;
        } else if (postion == count + 1) {
            index = 0;
        }
        return index;
    }

    /**
     * banner 默认布局
     *
     * @param context context
     * @return AppCompatImageView
     */
    private AppCompatImageView createImageView(Context context) {
        AppCompatImageView view = new AppCompatImageView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setScaleType(AppCompatImageView.ScaleType.FIT_XY);
        return view;
    }

    public List<Object> getBannerData() {
        if (mData == null) {
            mData = new ArrayList<Object>();
        }
        return mData;
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
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    /**
     * @param visibility visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAutoPlay();
        } else if (visibility == VISIBLE && isAutoPlay) {
            startAutoPlay();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.onBannerScrollStateChanged(state);
        }
        mCurrentItem = mBannerPage.getCurrentItem();
        int count = getBannerData().size();
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (mCurrentItem == 0) {
                    mBannerPage.setCurrentItem(mCurrentItem=count, false);
                } else if (mCurrentItem == count + 1) {
                    mBannerPage.setCurrentItem(mCurrentItem= 1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (mCurrentItem == count + 1) {
                    mBannerPage.setCurrentItem(mCurrentItem =1, false);
                } else if (mCurrentItem == 0) {
                    mBannerPage.setCurrentItem(mCurrentItem = count, false);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        position = positionIndex(position);

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.onBannerScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = positionIndex(position);
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        if (mIndicatorAble != null && getBannerData().size() > 0) {
            mIndicatorAble.onBannerSelected(position, getBannerData().size(), getBannerData().get(position));
        }
    }


    private static class HandlerTask implements Runnable {
        WeakReference<Banner> bannerRef;

        HandlerTask(Banner banner) {
            bannerRef = new WeakReference<Banner>(banner);
        }

        @Override
        public void run() {
            Banner banner = bannerRef.get();
            if (banner == null) {
                return;
            }
            int size = banner.getBannerData().size();
            if (size > 1 && banner.isAutoPlay) {
                banner.mCurrentItem = banner.mCurrentItem % (size + 1) + 1;
                if (banner.mCurrentItem == 1) {
                    banner.mBannerPage.setCurrentItem(banner.mCurrentItem, false);
                    banner.mHandler.post(this);
                } else {
                    banner.mBannerPage.setCurrentItem(banner.mCurrentItem);
                    banner.mHandler.postDelayed(this, banner.interval);
                }
            }
        }
    }

    /**
     * PagerAdapter
     */

    private List<View> getItemViews() {
        if (mItemViews == null) {
            mItemViews = new ArrayList<>();
        }
        return mItemViews;
    }

    private class BannerPagerAdapter extends PagerAdapter {
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return getItemViews().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = container.findViewWithTag(position);
            if (view == null) {
                view = getItemViews().get(position);
                view.setTag(position);
                container.addView(getItemViews().get(position));
            }

            view.setVisibility(VISIBLE);
            return view;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            View view = container.findViewWithTag(mBannerPage.getCurrentItem());
            if (view != null) {
                view.setEnabled(true);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            view.setVisibility(INVISIBLE);
        }
    }


    private CreateViewCallBack createViewCallBack;

    /**
     * 创建自定义view
     */
    public interface CreateViewCallBack<T extends View> {
        T createView(Context context);
    }

    private BindViewCallBack bindViewCallBack;

    /**
     * 绑定view
     */
    public interface BindViewCallBack<V extends View, T> {
        void bindView(V view, T data, int position);
    }


    private OnClickBannerListener onClickBannerListener;

    /**
     * 点击事件
     */
    public interface OnClickBannerListener<V extends View, T extends Object> {
        void onClickBanner(V view, T data, int position);
    }


    private IndicatorAble mIndicatorAble;

    /**
     * 自定义导航器
     */
    public interface IndicatorAble {
        void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onBannerScrollStateChanged(int state);

        void onBannerSelected(int position, int size, Object object);

        void initIndicator(int size);
    }
}
