package cn.ymex.widget.banner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
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

import java.lang.ref.WeakReference;
import java.util.List;

import cn.ymex.widget.banner.core.BaseBanner;

public class RecyclerBanner extends BaseBanner {


    private RecyclerView mRecyclerView;
    private PagerSnapHelper snapHelper;
    private RecyclerViewAdapter adapter;
    private HandlerTask task;

    private int startX, startY;
    private boolean isTouched;
    private boolean isPlaying;//是否在正滚动


    public RecyclerBanner(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        mHandler = new Handler();
        task = new HandlerTask(this);

        mRecyclerView = new RecyclerView(context);
        if (isVertical) {
            mRecyclerView.setLayoutManager(new SmoothLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }else {
            mRecyclerView.setLayoutManager(new SmoothLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
        mRecyclerView.setAdapter(adapter = new RecyclerViewAdapter());
        mRecyclerView.addOnScrollListener(new RecyclerOnScrollListener(this));
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        if (isAutoPlay) {
            if (!isPlaying && playing && adapter != null && adapter.getItemCount() > 2) {
                mHandler.postDelayed(task, interval);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeCallbacksAndMessages(null);
                isPlaying = false;
            }
        }
    }


    @Override
    public void execute(List datas) {
        setBannerData(datas);
    }


    /**
     * 设置数据
     *
     * @param data
     * @return
     */
    private void setBannerData(List data) {
        setPlaying(false);
        getBannerData().clear();
        if (data != null) {
            getBannerData().addAll(data);
        }
        if (getBannerData().size() < 2) {
            mCurrentItem = 0;

            adapter.notifyDataSetChanged();
            return;
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.initIndicator(data.size());
        }
        mCurrentItem = getBannerData().size() * 1000;
        adapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mCurrentItem);
        setPlaying(true);
        adapter.notifyDataSetChanged();
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


    private int positionIndex() {
        if (getBannerData().size() == 0) {
            return 0;
        }
        return mCurrentItem % getBannerData().size();
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
            if (createViewCallBack != null) {
                view = createViewCallBack.createView(parent.getContext(), parent, viewType);
            }
            if (view == null) {
                view = createImageView(parent.getContext());
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBannerListener != null) {
                        int position = positionIndex();
                        onClickBannerListener.onClickBanner(v, getItemData(position), position);
                    }
                }
            });
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            if (bindViewCallBack != null) {
                int pos = position % getBannerData().size();
                bindViewCallBack.bindView(holder.itemView, getItemData(pos), position);
            }
        }

        @Override
        public int getItemCount() {
            return getBannerData() == null ? 0 : getBannerData().size() < 2 ? getBannerData().size() : Integer.MAX_VALUE;
        }
    }

    /**
     * 获取数据
     *
     * @param postion
     * @return
     */
    public Object getItemData(int postion) {
        if (getBannerData() == null || getBannerData().size() <= postion || postion < 0) {
            return null;
        }
        return getBannerData().get(postion);
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
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
                if (firstIndex == lastIndex && banner.mCurrentItem != lastIndex) {
                    banner.mCurrentItem = lastIndex;
                    if (banner.isTouched) {
                        banner.isTouched = false;
                        if (banner.mIndicatorAble != null) {
                            int position = banner.positionIndex();
                            banner.mIndicatorAble.onBannerSelected(position,banner.getBannerData().size(),banner.getBannerData().get(position));
                        }
                    }
                }
            }
        }
    }


    /**
     * 定时轮播
     */
    private static class HandlerTask implements Runnable {

        WeakReference<RecyclerBanner> bannerWeakRef;

        HandlerTask(RecyclerBanner recyclerBanner) {
            bannerWeakRef = new WeakReference<RecyclerBanner>(recyclerBanner);
        }

        @Override
        public void run() {
            RecyclerBanner banner = bannerWeakRef.get();
            if (banner == null) {
                return;
            }
            banner.mRecyclerView.smoothScrollToPosition(++banner.mCurrentItem);

            if (banner.mIndicatorAble != null) {
                int position = banner.positionIndex();
                banner.mIndicatorAble.onBannerSelected(position,banner.getBannerData().size(),banner.getBannerData().get(position));
            }
            banner.mHandler.postDelayed(this, banner.interval);
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