package cn.ymex.widget.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.ymex.banner.R;
import cn.ymex.widget.banner.core.IndicatorAble;

/**
 * 指示器
 */

public class IndicatorLayout extends LinearLayout implements IndicatorAble {

    private static final int DEF_SELECTED_COLOR = 0XFFFFFFFF;
    private static final int DEF_UNSELECTED_COLOR = 0X88FFFFFF;

    private Drawable mSelectedDrawable;
    private Drawable mUnSelectedDrawable;

    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorCount;
    private int mIndicatorShap;

    private static int dip4 = dp2px(4);
    private static int dip8 = dp2px(8);

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
            initIndicator(3);
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

        mIndicatorShap = typedArray.getInt(R.styleable.IndicatorLayout_indicator_shape, 0);


        if (mSelectedDrawable == null) {
            mSelectedDrawable = createDrawable(DEF_SELECTED_COLOR, mIndicatorWidth, mIndicatorHeight);
        } else if (mSelectedDrawable instanceof ColorDrawable) {
            mSelectedDrawable = createDrawable(((ColorDrawable) mSelectedDrawable).getColor(), mIndicatorWidth, mIndicatorHeight);
        }
        if (mUnSelectedDrawable == null) {
            mUnSelectedDrawable = createDrawable(DEF_UNSELECTED_COLOR, mIndicatorWidth, mIndicatorHeight);
        } else if (mUnSelectedDrawable instanceof ColorDrawable) {
            mSelectedDrawable = createDrawable(((ColorDrawable) mUnSelectedDrawable).getColor(), mIndicatorWidth, mIndicatorHeight);
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

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    @Override
    public void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onBannerScrollStateChanged(int state) {

    }

    @Override
    public void onBannerSelected(int position, int size, Object object) {
        changeIndicator(position);
    }

    @Override
    public void initIndicator(int size) {
        this.removeAllViews();
        this.mIndicatorCount = size;
        if (size < 2) {
            return;
        }
        for (int i = 0; i < mIndicatorCount; i++) {
            AppCompatImageView imageView = new AppCompatImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = lp.leftMargin = lp.topMargin = lp.bottomMargin = mIndicatorMargin;

            if (mIndicatorWidth != dip8) {
                lp.width = mIndicatorWidth;
            }
            if (mIndicatorHeight != dip8) {
                lp.height = mIndicatorHeight;
            }

            imageView.setImageDrawable(i == 0 ? mSelectedDrawable : mUnSelectedDrawable);

            addView(imageView, lp);
        }
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
}
