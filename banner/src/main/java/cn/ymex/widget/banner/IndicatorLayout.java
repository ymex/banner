package cn.ymex.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.ymex.banner.R;
import cn.ymex.widget.banner.core.IndicatorAble;

/**
 * 指示器
 */

public class IndicatorLayout extends LinearLayout implements IndicatorAble {

    private static final int DEF_SELECTED_COLOR = 0XFFFFFFFF, DEF_UNSELECTED_COLOR = 0X88FFFFFF;
    private Drawable mSelectedDrawable, mUnSelectedDrawable;

    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorCount;
    private int mIndicatorShap;

    private boolean mIndicatorFlow;//指示器跟随
    private View mFirstIndicator, mSecondIndicator;
    private int mMoveDistance = 0;
    private int mMarginSize;
    private int mMaxDotCount = 20;
    private int mMinDotCount = 1;


    private Paint mBitPaint;
    private RectF mRectF;
    private Rect mSrcRect;
    private Bitmap mSelectBitmap;


    private static int dip4 = (int) dp2px(4);
    private static int dip8 = (int) dp2px(8);




    public IndicatorLayout(Context context, int count) {
        this(context);
        this.mIndicatorCount = count;
    }

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        dealAttrs(attrs);
        if (isInEditMode()) {
            initIndicator(mMaxDotCount);
        }
    }

    private void dealAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorLayout);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorLayout_indicator_width, dip8);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorLayout_indicator_height, dip8);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.IndicatorLayout_indicator_margin, dip4);
        mSelectedDrawable = typedArray.getDrawable(R.styleable.IndicatorLayout_indicator_selected);
        mUnSelectedDrawable = typedArray.getDrawable(R.styleable.IndicatorLayout_indicator_unselected);
        mIndicatorFlow = typedArray.getBoolean(R.styleable.IndicatorLayout_indicator_flow, false);
        mIndicatorShap = typedArray.getInt(R.styleable.IndicatorLayout_indicator_shape, 0);
        mMinDotCount = typedArray.getInt(R.styleable.IndicatorLayout_indicator_min_dot, mMinDotCount);
        mMaxDotCount = typedArray.getInt(R.styleable.IndicatorLayout_indicator_max_dot, mMaxDotCount);


        if (mSelectedDrawable == null) {
            mSelectedDrawable = createDrawable(DEF_SELECTED_COLOR, mIndicatorWidth, mIndicatorHeight);
        } else if (mSelectedDrawable instanceof ColorDrawable) {
            mSelectedDrawable = createDrawable(((ColorDrawable) mSelectedDrawable).getColor(), mIndicatorWidth, mIndicatorHeight);
        }
        if (mUnSelectedDrawable == null) {
            mUnSelectedDrawable = createDrawable(DEF_UNSELECTED_COLOR, mIndicatorWidth, mIndicatorHeight);
        } else if (mUnSelectedDrawable instanceof ColorDrawable) {
            mUnSelectedDrawable = createDrawable(((ColorDrawable) mUnSelectedDrawable).getColor(), mIndicatorWidth, mIndicatorHeight);
        }
        typedArray.recycle();
    }


    /**
     * @param color  颜色
     * @param width  width
     * @param height height
     * @return Drawable
     */
    private GradientDrawable createDrawable(int color, int width, int height) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(width, height);
        if (mIndicatorShap == 0) {
            gradientDrawable.setCornerRadius(width > height ? width : height);
        }
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }


    public void setCurrentDrawable(Drawable currentDrawable) {
        if (currentDrawable == null) {
            return;
        }
        this.mSelectedDrawable = currentDrawable;
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        if (normalDrawable == null) {
            return;
        }
        this.mUnSelectedDrawable = normalDrawable;
    }

    public void setIndicatorSize(int indicatorSize) {
        this.mIndicatorWidth = indicatorSize;
    }

    public void setIndicatorSpace(int indicatorSpace) {
        this.mIndicatorMargin = indicatorSpace;
    }


    public boolean isIndicatorFlow() {
        return mIndicatorFlow;
    }

    public void setIndicatorFlow(boolean mIndicatorFlow) {
        this.mIndicatorFlow = mIndicatorFlow;
    }

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }


    @Override
    public void onBannerScrollStateChanged(int state) {

    }

    @Override
    public void onBannerSelected(int position, int size, Object object) {
        if (mIndicatorFlow) {//跟随处理
        } else {
            changeIndicator(position);
        }
    }

    @Override
    public void initIndicator(int size) {
        this.removeAllViews();
        this.mIndicatorCount = size;
        if (size < mMinDotCount) {
            return;
        }
        if (mIndicatorCount > mMaxDotCount) {
            mIndicatorCount = mMaxDotCount;
        }
        for (int i = 0; i < mIndicatorCount; i++) {
            AppCompatImageView imageView = new AppCompatImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = lp.leftMargin = lp.topMargin = lp.bottomMargin = mIndicatorMargin;

            if (mIndicatorWidth != dip8) lp.width = mIndicatorWidth;
            if (mIndicatorHeight != dip8) lp.height = mIndicatorHeight;

            imageView.setImageDrawable(mIndicatorFlow ? mUnSelectedDrawable : i == 0 ? mSelectedDrawable : mUnSelectedDrawable);

            if (i == 0) mFirstIndicator = imageView;
            if (i == 1) mSecondIndicator = imageView;
            addView(imageView, lp);
        }
        initPaint();
        mSelectBitmap = drawable2Bitmap(mSelectedDrawable);
        mSrcRect = new Rect(0, 0, mSelectBitmap.getWidth(), mSelectBitmap.getHeight());
    }

    private void initPaint() {
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
    }

    /**
     * 切换选中样式
     */
    public void changeIndicator(int currentIndex) {
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                ((AppCompatImageView) getChildAt(i)).setImageDrawable(
                        i == currentIndex % mIndicatorCount ? mSelectedDrawable : mUnSelectedDrawable);
            }
        }
    }


    @Override
    public void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!mIndicatorFlow) {
            return;
        }
        if (position == (mIndicatorCount - 1) && positionOffset > 0) {
            if (positionOffset >= 50) {
                mMoveDistance = 0;
            }
        } else {
            mMoveDistance = (int) (positionOffset * mMarginSize + position % mIndicatorCount * mMarginSize);
        }
        postInvalidate();
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mFirstIndicator == null || mSecondIndicator == null) {
            return;
        }
        int[] mFirstPos = new int[2];
        int[] mSecondPos = new int[2];
        int[] viewPos = new int[2];
        mFirstIndicator.getLocationOnScreen(mFirstPos);
        mSecondIndicator.getLocationOnScreen(mSecondPos);
        getLocationInWindow(viewPos);

        // 指示器间的距离
        if (getOrientation() == HORIZONTAL) {
            mMarginSize = mSecondPos[0] - mFirstPos[0];
        } else {
            mMarginSize = mSecondPos[1] - mFirstPos[1];
        }
        int firstIndicatorX = mFirstPos[0] - viewPos[0];
        int firstIndicatorY = mFirstPos[1] - viewPos[1];

        mRectF = new RectF(firstIndicatorX, firstIndicatorY,
                firstIndicatorX + mSelectBitmap.getWidth(), firstIndicatorY + mSelectBitmap.getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIndicatorFlow) {
            canvas.save();
            if (getOrientation() == HORIZONTAL) {
                canvas.translate(mMoveDistance, 0);
            } else {
                canvas.translate(0, mMoveDistance);
            }
            canvas.drawBitmap(mSelectBitmap, mSrcRect, mRectF, mBitPaint);
            canvas.restore();
        }
    }


    /**
     * drawable to bitmap
     *
     * @param drawable drawable
     * @return bitmap
     */
    Bitmap drawable2Bitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);


        //注意，若不使用canvas画入，在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
