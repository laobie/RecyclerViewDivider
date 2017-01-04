package com.jaeger.recyclerviewdivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.util.TypedValue.applyDimension;

/**
 * Created by Jaeger on 16/12/29.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    /**
     * The divider drawable
     */
    private Drawable mDivider;

    private int mOrientation;

    private final Rect mBounds = new Rect();

    private Builder mBuilder;

    private RecyclerViewDivider(Builder builder) {
        mBuilder = builder;
        setOrientation();
        setDividerDrawable();
    }

    /**
     * Set Divider Drawable
     */
    private void setDividerDrawable() {
        Drawable drawable;
        if (mBuilder.mDrawable != null) {
            drawable = mBuilder.mDrawable;
        } else {
            drawable = new DividerDrawable(mBuilder.mColor);
        }
        mDivider = drawable;
    }

    /**
     * Set Divider Orientation
     */
    public void setOrientation() {
        int orientation = mBuilder.mOrientation;
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
        if (mOrientation == VERTICAL) {
            int height = mDivider.getIntrinsicHeight();
            if (position == 0 && mBuilder.mShowTopDivider) {
                outRect.set(0, height, 0, height);
            } else if (!needSkip(position, count)) {
                outRect.set(0, 0, 0, height);
            }
        } else {
            int width = mDivider.getIntrinsicWidth();
            if (position == 0 && mBuilder.mShowTopDivider) {
                outRect.set(width, 0, width, 0);
            } else if (!needSkip(position, count)) {
                outRect.set(0, 0, width, 0);
            }
        }
    }

    private boolean needSkip(int position, int count) {
        return position < mBuilder.mStartSkipCount || position >= count - mBuilder.mEndSkipCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * Draw vertical list divider
     *
     * @param canvas canvas
     * @param parent RecyclerView
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mBuilder.mMarginLeft;
            right = parent.getWidth() - parent.getPaddingRight() - mBuilder.mMarginRight;
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = mBuilder.mMarginLeft;
            right = parent.getWidth() - mBuilder.mMarginRight;
        }

        int childCount = parent.getChildCount();
        int top;
        int bottom;
        int count = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (needSkip(position, count)) {
                continue;
            }
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }

        if (childCount > 0 && mBuilder.mShowTopDivider) {
            final View child = parent.getChildAt(0);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            top = mBounds.top + Math.round(ViewCompat.getTranslationY(child));
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    /**
     * Draw horizontal list divider
     *
     * @param canvas canvas
     * @param parent RecyclerView
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            top = parent.getPaddingTop() + mBuilder.mMarginTop;
            bottom = parent.getHeight() - parent.getPaddingBottom() - mBuilder.mMarginBottom;
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = mBuilder.mMarginTop;
            bottom = parent.getHeight() - mBuilder.mMarginBottom;
        }

        int childCount = parent.getChildCount();
        int left;
        int right;
        int count = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (needSkip(position, count)) {
                continue;
            }
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        if (childCount > 0 && mBuilder.mShowTopDivider) {
            final View child = parent.getChildAt(0);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            left = mBounds.left + Math.round(ViewCompat.getTranslationX(child));
            right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    /**
     * RecyclerView Divider Builder
     */
    public static class Builder {
        private Context mContext;
        private Drawable mDrawable;
        private int mOrientation = VERTICAL;
        private int mSize = 1;
        private int mMarginLeft = 0;
        private int mMarginRight = 0;
        private int mMarginTop = 0;
        private int mMarginBottom = 0;
        private int mColor = 0xFFD1D1D1;
        private int mStartSkipCount = 0;
        private int mEndSkipCount = 0;
        private @Style int mStyle = Style.END;

        private boolean mShowTopDivider;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * Set divider drawable
         *
         * @param drawable Divider drawable
         */
        public Builder setDrawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        /**
         * Set divider drawable resource
         *
         * @param drawableRes Divider drawable resource
         */
        public Builder setDrawableRes(int drawableRes) {
            mDrawable = mContext.getResources().getDrawable(drawableRes);
            return this;
        }

        /**
         * Set divider style
         *
         * @param style divider style
         */
        public Builder setStyle(@Style int style) {
            mStyle = style;
            return this;
        }

        /**
         * Set divider orientation
         *
         * @param orientation divider orientation
         */
        public Builder setOrientation(int orientation) {
            mOrientation = orientation;
            return this;
        }

        /**
         * Set divider size
         *
         * @param size divider size
         */
        public Builder setSize(float size) {
            return setSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }

        /**
         * Set divider height
         *
         * @param unit   divider height unit
         * @param height divider height
         */
        public Builder setSize(int unit, float height) {
            mSize = getSizeValue(unit, height);
            return this;
        }

        /**
         * Set divider margin left
         *
         * @param marginLeft margin left value
         */
        public Builder setMarginLeft(float marginLeft) {
            return setMarginLeft(TypedValue.COMPLEX_UNIT_DIP, marginLeft);
        }

        /**
         * Set divider margin left
         *
         * @param unit       margin left value unit
         * @param marginLeft margin left value
         */
        public Builder setMarginLeft(int unit, float marginLeft) {
            mMarginLeft = getSizeValue(unit, marginLeft);
            return this;
        }

        /**
         * Set divider margin right
         *
         * @param marginRight margin right value
         */
        public Builder setMarginRight(float marginRight) {
            return setMarginRight(TypedValue.COMPLEX_UNIT_DIP, marginRight);
        }

        /**
         * Set divider margin right
         *
         * @param unit        margin right value unit
         * @param marginRight margin right value
         */
        public Builder setMarginRight(int unit, float marginRight) {
            mMarginRight = getSizeValue(unit, marginRight);
            return this;
        }

        /**
         * Set divider margin top
         *
         * @param marginTop margin top value
         */
        public Builder setMarginTop(int marginTop) {
            return setMarginTop(TypedValue.COMPLEX_UNIT_DIP, marginTop);
        }

        /**
         * Set divider margin right
         *
         * @param unit      margin right value unit
         * @param marginTop margin top value
         */
        public Builder setMarginTop(int unit, int marginTop) {
            mMarginTop = getSizeValue(unit, marginTop);
            return this;
        }

        /**
         * Set divider margin bottom
         *
         * @param marginBottom margin bottom value
         */
        public Builder setMarginBottom(float marginBottom) {
            return setMarginBottom(TypedValue.COMPLEX_UNIT_DIP, marginBottom);
        }

        /**
         * Set divider margin bottom
         *
         * @param unit         margin bottom value unit
         * @param marginBottom margin bottom value
         */
        public Builder setMarginBottom(int unit, float marginBottom) {
            mMarginBottom = getSizeValue(unit, marginBottom);
            return this;
        }

        /**
         * Set divider color
         *
         * @param color divider color
         */
        public Builder setColor(@ColorInt int color) {
            mColor = color;
            return this;
        }

        /**
         * Set divider color
         *
         * @param colorRes divider color resource
         */
        public Builder setColorRes(@ColorRes int colorRes) {
            mColor = mContext.getResources().getColor(colorRes);
            return this;
        }

        /**
         * Set skip count from start
         *
         * @param startSkipCount count from start
         */
        public Builder setStartSkipCount(int startSkipCount) {
            mStartSkipCount = startSkipCount;
            return this;
        }

        /**
         * Set skip count before end
         *
         * @param endSkipCount count before end
         */
        public Builder setEndSkipCount(int endSkipCount) {
            mEndSkipCount = endSkipCount;
            return this;
        }

        private int getSizeValue(int unit, float size) {
            return (int) applyDimension(unit, size, mContext.getResources().getDisplayMetrics());
        }

        public RecyclerViewDivider build() {
            switch (mStyle) {
                case Style.BETWEEN:
                    mEndSkipCount++;
                    break;
                case Style.BOTH:
                    mStartSkipCount--;
                    break;
                case Style.END:
                    break;
                case Style.START:
                    mEndSkipCount++;
                    break;
            }
            mShowTopDivider = (mStyle == Style.BOTH && mStartSkipCount < 0) || mStyle == Style.START;
            return new RecyclerViewDivider(this);
        }
    }

    /**
     * DividerDrawable
     */
    private class DividerDrawable extends ColorDrawable {
        DividerDrawable(int color) {
            super(color);
        }

        @Override
        public int getIntrinsicWidth() {
            return mBuilder.mSize;
        }

        @Override
        public int getIntrinsicHeight() {
            return mBuilder.mSize;
        }
    }

    /**
     * Divider Style
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ Style.BOTH, Style.START, Style.END, Style.BETWEEN })
    public @interface Style {
        int END = 0;
        int START = 1;
        int BOTH = 2;
        int BETWEEN = 3;
    }
}
