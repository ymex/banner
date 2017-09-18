package cn.ymex.widget.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.ymex.banner.R;

public class RecyclerBanner extends FrameLayout {

    private List<Object> mData = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private Banner.IndicatorAble mIndicatorLayout;
    private PagerSnapHelper snapHelper;
    private RecyclerViewAdapter adapter;

    private OnCreateBannerItemViewListener onCreateBannerItemViewListener;
    private OnBannerItemClickListener onBannerItemClickListener;
    private OnBindBannerItemViewListener onBindBannerItemViewListener;


    private int startX, startY;
    private int mInterval = 5000;
    private boolean isTouched;
    private int currentIndex;
    private boolean isAutoPlaying = true;
    private boolean isPlaying;//是否在正滚动

    private Handler handler = new Handler();
    private AutoPlayTask task;


    public RecyclerBanner(Context context) {
        this(context, null);
    }

    public RecyclerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);

        mInterval = attrArray.getInt(R.styleable.Banner_banner_interval, mInterval);

        isAutoPlaying = attrArray.getBoolean(R.styleable.Banner_banner_auto_play, true);


        attrArray.recycle();


        task = new AutoPlayTask(this);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(new SmoothLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter = new RecyclerViewAdapter());
        mRecyclerView.addOnScrollListener(new RecyclerOnScrollListener(this));
        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof Banner.IndicatorAble) {
                mIndicatorLayout = (Banner.IndicatorAble) view;
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    private synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying) {
            if (!isPlaying && playing && adapter != null && adapter.getItemCount() > 2) {
                handler.postDelayed(task, mInterval);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                handler.removeCallbacksAndMessages(null);
                isPlaying = false;
            }
        }
    }


    /**
     * 设置数据
     *
     * @param data
     * @return
     */
    public RecyclerBanner setBannerData(List data) {
        setPlaying(false);
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        if (mData.size() < 2) {
            currentIndex = 0;
            mIndicatorLayout.initIndicator(data.size());
            adapter.notifyDataSetChanged();
            return this;
        }
        currentIndex = mData.size() * 1000;
        adapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(currentIndex);
        setPlaying(true);
        adapter.notifyDataSetChanged();
        return this;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = moveX - startX;
                int disY = moveY - startY;
                boolean hasMoved = 2 * Math.abs(disX) > Math.abs(disY);
                getParent().requestDisallowInterceptTouchEvent(hasMoved);
                if (hasMoved) {
                    setPlaying(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isPlaying) {
                    isTouched = true;
                    setPlaying(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    /**
     * 当视图不可见时不滚动
     *
     * @param visibility visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            setPlaying(false);
        } else if (visibility == VISIBLE) {
            setPlaying(true);
        }
        super.onWindowVisibilityChanged(visibility);
    }


    /**
     * RecyclerView适配器
     */
    private class RecyclerViewAdapter extends RecyclerView.Adapter {

        private AppCompatImageView createImageView(Context context) {
            AppCompatImageView view = new AppCompatImageView(context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            view.setScaleType(AppCompatImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (onCreateBannerItemViewListener != null) {
                view = onCreateBannerItemViewListener.onCreateBannerItemView(parent, viewType);
            }
            if (view == null) {
                view = createImageView(parent.getContext());
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.onItemClick(currentIndex % mData.size());
                    }
                }
            });
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            if (onBindBannerItemViewListener != null) {
                onBindBannerItemViewListener.onBindBannerItemView(holder.itemView, getItemData(position % mData.size()), position % mData.size());
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size() < 2 ? mData.size() : Integer.MAX_VALUE;
        }
    }

    /**
     * 获取数据
     *
     * @param postion
     * @return
     */
    public Object getItemData(int postion) {
        if (mData == null || mData.size() <= postion || postion < 0) {
            return null;
        }
        return mData.get(postion);
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }


    public interface OnCreateBannerItemViewListener {
        View onCreateBannerItemView(ViewGroup parent, int viewType);
    }

    public RecyclerBanner createBannerItemView(OnCreateBannerItemViewListener onCreateBannerItemViewListener) {
        this.onCreateBannerItemViewListener = onCreateBannerItemViewListener;
        return this;
    }

    public interface OnBindBannerItemViewListener {
        void onBindBannerItemView(View itemView, Object ob, int position);
    }

    public RecyclerBanner bindBannerItemView(OnBindBannerItemViewListener listener) {
        this.onBindBannerItemViewListener = listener;
        return this;
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerBanner setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
        return this;
    }


    /**
     * recyclerView 滚支监听
     */
    private static class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        WeakReference<RecyclerBanner> bannerRef;

        public RecyclerOnScrollListener(RecyclerBanner recyclerBanner) {
            bannerRef = new WeakReference<RecyclerBanner>(recyclerBanner);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int firstIndex = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastIndex = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                RecyclerBanner banner = bannerRef.get();
                if (banner == null) {
                    return;
                }
                if (firstIndex == lastIndex && banner.currentIndex != lastIndex) {
                    banner.currentIndex = lastIndex;
                    if (banner.isTouched) {
                        banner.isTouched = false;
                        //banner.mIndicatorLayout.changeIndicator(banner.currentIndex);
                    }
                }
            }
        }
    }


    /**
     * 定时轮播
     */
    private static class AutoPlayTask implements Runnable {

        WeakReference<RecyclerBanner> bannerWeakRef;

        AutoPlayTask(RecyclerBanner recyclerBanner) {
            bannerWeakRef = new WeakReference<RecyclerBanner>(recyclerBanner);
        }

        @Override
        public void run() {
            RecyclerBanner banner = bannerWeakRef.get();
            if (banner == null) {
                return;
            }
            banner.mRecyclerView.smoothScrollToPosition(++banner.currentIndex);
//            if (banner.isShowIndicator) {
//                banner.mIndicatorLayout.changeIndicator(banner.currentIndex);
//            }
            banner.handler.postDelayed(this, banner.mInterval);
        }
    }


    /**
     * 控制位置
     */
    private class PagerSnapHelper extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
            final View currentView = findSnapView(layoutManager);

            if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                int currentPos = layoutManager.getPosition(currentView);
                int firstIndex = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                int lastIndex = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                currentPos = targetPos < currentPos ? lastIndex : (targetPos > currentPos ? firstIndex : currentPos);
                targetPos = targetPos < currentPos ? currentPos - 1 : (targetPos > currentPos ? currentPos + 1 : currentPos);
            }
            return targetPos;
        }
    }


    /**
     * 控制滚动速度
     */
    private class SmoothLinearLayoutManager extends LinearLayoutManager {
        private float MILLISECONDS_PER_INCH = 0.66f;

        public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return super.computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.density;
                        }

                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

}