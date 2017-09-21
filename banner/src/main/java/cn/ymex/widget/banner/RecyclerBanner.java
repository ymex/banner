package cn.ymex.widget.banner;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewHolderDelegate;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.ymex.widget.banner.core.BaseBanner;
import cn.ymex.widget.banner.pager.RecyclerViewPager;

public class RecyclerBanner extends BaseBanner {


    private LoopRecyclerViewPager mRecyclerView;
    private RecyclerViewAdapter adapter;
    private HandlerTask task;


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

        mRecyclerView = new LoopRecyclerViewPager(context);
        mRecyclerView.setSinglePageFling(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);

        onPageChanged(mRecyclerView);
        runDirection(mRecyclerView);

        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void runDirection(RecyclerViewPager mRecyclerView) {
        if (mRecyclerView == null) {
            return;
        }
        if (isVertical) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public BaseBanner setOrientation(int orientation) {
        this.isVertical = orientation == VERTICAL;
        runDirection(mRecyclerView);
        return this;
    }


    @Override
    public BaseBanner setLoop(boolean loop) {
        this.isLoop = loop;
        return this;
    }

    @Override
    protected int positionIndex(int postion) {

        int count = getBannerData().size();
        int index = 0;
        if (count != 0) {
            index = mCurrentItem % getBannerData().size();
        }
        return index;
    }

    private void onPageChanged(RecyclerViewPager mRecyclerView) {
        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                int size = getBannerData().size();
                mCurrentItem = newPosition;
                if (mIndicatorAble != null && size > 0) {
                    int index = positionIndex(mCurrentItem);
                    mIndicatorAble.onBannerSelected(index, size, getBannerData().get(index));
                }
            }
        });
    }


    public LoopRecyclerViewPager getRecyclerView() {
        return mRecyclerView;
    }


    @Override
    public void startAutoPlay() {
        if (isAutoPlay && isLoop && getBannerData().size() > 2) {
            mHandler.removeCallbacks(task);
            mHandler.postDelayed(task, interval);
        }
    }

    @Override
    public void stopAutoPlay() {
        mHandler.removeCallbacks(task);
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
        stopAutoPlay();
        getBannerData().clear();
        if (data != null) {
            getBannerData().addAll(data);
        }
        if (getBannerData().size() < 2) {
            adapter.notifyDataSetChanged();
            return;
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.initIndicator(data.size());
        }

        mRecyclerView.setCanLoop(isLoop);
        mRecyclerView.setAdapter(adapter = new RecyclerViewAdapter());

        startAutoPlay();
        adapter.notifyDataSetChanged();
    }


    /**
     * RecyclerView适配器
     */
    private class RecyclerViewAdapter extends RecyclerView.Adapter {


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
                        int index = positionIndex(mCurrentItem);
                        onClickBannerListener.onClickBanner(v, getItemData(index), index);
                    }
                }
            });
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            if (bindViewCallBack != null) {
                bindViewCallBack.bindView(holder.itemView, getItemData(position), position);
            }
        }

        @Override
        public int getItemCount() {
            System.out.println("----------getItemCount:" + getBannerData().size());
            return getBannerData().size();
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
            ++banner.mCurrentItem;
            banner.mRecyclerView.smoothScrollToPosition(banner.mCurrentItem);
            banner.mHandler.postDelayed(this, banner.interval);
        }
    }


    public class LoopRecyclerAdapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerViewPager.RecyclerAdapter<VH> {


        public LoopRecyclerAdapter(RecyclerViewPager viewPager, RecyclerView.Adapter<VH> adapter) {
            super(viewPager, adapter);
        }


        public int getActualItemCount() {
            return super.getItemCount();
        }

        public int getActualItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public long getActualItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            if (getActualItemCount() > 0) {
                return Integer.MAX_VALUE;
            } else {
                return super.getItemCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (getActualItemCount() > 0) {
                return super.getItemViewType(getActualPosition(position));
            } else {
                return 0;
            }
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(getActualPosition(position));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            super.onBindViewHolder(holder, getActualPosition(position));
            // because of getCurrentPosition may return ViewHolder‘s position,
            // so we must reset mPosition if exists.
            ViewHolderDelegate.setPosition(holder, position);
        }

        public int getActualPosition(int position) {
            int actualPosition = position;
            if (getActualItemCount() > 0 && position >= getActualItemCount()) {
                actualPosition = position % getActualItemCount();
            }
            return actualPosition;
        }
    }


    public class LoopRecyclerViewPager extends RecyclerViewPager {
        private boolean canLoop = true;

        public void setCanLoop(boolean canLoop) {
            this.canLoop = canLoop;
        }

        public LoopRecyclerViewPager(Context context) {
            this(context, null);
        }

        public LoopRecyclerViewPager(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void setAdapter(Adapter adapter) {
            super.setAdapter(adapter);
            if (canLoop) {
                super.scrollToPosition(getMiddlePosition());
            }
        }

        @Override
        public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
            super.swapAdapter(adapter, removeAndRecycleExistingViews);
            if (canLoop) {
                super.scrollToPosition(getMiddlePosition());
            }
        }

        @Override
        @NonNull
        protected RecyclerViewPager.RecyclerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {

            if (canLoop) {
                return (adapter instanceof LoopRecyclerAdapter)
                        ? (LoopRecyclerAdapter) adapter
                        : new LoopRecyclerAdapter(this, adapter);
            }
            return super.ensureRecyclerViewPagerAdapter(adapter);


        }

        /**
         * Starts a smooth scroll to an adapter position.
         * if position < adapter.getActualCount,
         * position will be transform to right position.
         *
         * @param position target position
         */
        @Override
        public void smoothScrollToPosition(int position) {
            if (canLoop) {
                int transformedPosition = transformInnerPositionIfNeed(position);
                super.smoothScrollToPosition(transformedPosition);
                Log.e("test", "transformedPosition:" + transformedPosition);
                return;
            }
            super.smoothScrollToPosition(position);

        }

        /**
         * Starts a scroll to an adapter position.
         * if position < adapter.getActualCount,
         * position will be transform to right position.
         *
         * @param position target position
         */
        @Override
        public void scrollToPosition(int position) {
            if (canLoop) {
                super.scrollToPosition(transformInnerPositionIfNeed(position));
                return;
            }
            super.scrollToPosition(position);
        }

        /**
         * get actual current position in actual adapter.
         */
        public int getActualCurrentPosition() {
            int position = getCurrentPosition();
            return transformToActualPosition(position);
        }

        /**
         * Transform adapter position to actual position.
         *
         * @param position adapter position
         * @return actual position
         */
        public int transformToActualPosition(int position) {
            if (getAdapter() == null || getAdapter().getItemCount() < 0) {
                return 0;
            }
            return position % getActualItemCountFromAdapter();
        }

        private int getActualItemCountFromAdapter() {
            return ((LoopRecyclerAdapter) getWrapperAdapter()).getActualItemCount();
        }

        private int transformInnerPositionIfNeed(int position) {
            final int actualItemCount = getActualItemCountFromAdapter();
            if (actualItemCount == 0)
                return actualItemCount;
            final int actualCurrentPosition = getCurrentPosition() % actualItemCount;
            int bakPosition1 = getCurrentPosition()
                    - actualCurrentPosition
                    + position % actualItemCount;
            int bakPosition2 = getCurrentPosition()
                    - actualCurrentPosition
                    - actualItemCount
                    + position % actualItemCount;
            int bakPosition3 = getCurrentPosition()
                    - actualCurrentPosition
                    + actualItemCount
                    + position % actualItemCount;
            Log.e("test", bakPosition1 + "/" + bakPosition2 + "/" + bakPosition3 + "/" + getCurrentPosition());
            // get position which is closer to current position
            if (Math.abs(bakPosition1 - getCurrentPosition()) > Math.abs(bakPosition2 -
                    getCurrentPosition())) {
                if (Math.abs(bakPosition2 -
                        getCurrentPosition()) > Math.abs(bakPosition3 -
                        getCurrentPosition())) {
                    return bakPosition3;
                }
                return bakPosition2;
            } else {
                if (Math.abs(bakPosition1 -
                        getCurrentPosition()) > Math.abs(bakPosition3 -
                        getCurrentPosition())) {
                    return bakPosition3;
                }
                return bakPosition1;
            }
        }

        private int getMiddlePosition() {
            int middlePosition = Integer.MAX_VALUE / 2;
            final int actualItemCount = getActualItemCountFromAdapter();
            if (actualItemCount > 0 && middlePosition % actualItemCount != 0) {
                middlePosition = middlePosition - middlePosition % actualItemCount;
            }

            return middlePosition;
        }
    }
}