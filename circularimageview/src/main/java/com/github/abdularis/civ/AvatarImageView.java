package com.github.abdularis.civ;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by abdularis on 22/03/18.
 */

public class AvatarImageView extends CircleImageView {

    public static final int SHOW_INITIAL = 1;
    public static final int SHOW_IMAGE = 2;

    @IntDef({SHOW_INITIAL, SHOW_IMAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private static final String DEF_INITIAL = "A";
    private static final int DEF_TEXT_SIZE = 90;
    private static final int DEF_BACKGROUND_COLOR = 0xE53935;
    @State
    private static final int DEF_STATE = SHOW_INITIAL;

    private Paint mTextPaint;
    private Rect mTextBounds;

    private Paint mBackgroundPaint;
    private RectF mBackgroundBounds;

    @NonNull
    private String mInitial;
    @NonNull
    private String mText;

    private int mShowState;

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String text = DEF_INITIAL;
        int textColor = Color.WHITE;
        int textSize = DEF_TEXT_SIZE;
        int backgroundColor = DEF_BACKGROUND_COLOR;
        int showState = DEF_STATE;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0);

            text = a.getString(R.styleable.AvatarImageView_text);
            textColor = a.getColor(R.styleable.AvatarImageView_textColor, textColor);
            textSize = a.getDimensionPixelSize(R.styleable.AvatarImageView_textSize, textSize);
            backgroundColor = a.getColor(R.styleable.AvatarImageView_avatarBackgroundColor, backgroundColor);
            showState = a.getInt(R.styleable.AvatarImageView_view_state, showState);

            a.recycle();
        }

        mShowState = showState;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mTextBounds = new Rect();
        mText = text == null ? "" : text;
        mInitial = extractInitial(text);
        updateTextBounds();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mBackgroundBounds = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateCircleDrawBounds(mBackgroundBounds);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowState == SHOW_INITIAL) {
            float textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY();
            canvas.drawOval(mBackgroundBounds, mBackgroundPaint);
            canvas.drawText(mInitial, mBackgroundBounds.centerX(), textBottom, mTextPaint);
            drawStroke(canvas);
            drawHighlight(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    @NonNull
    public String getInitial() {
        return mInitial;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    public void setText(@Nullable String text) {
        mText = text == null ? "" : text;
        mInitial = extractInitial(text);
        updateTextBounds();
        invalidate();
    }

    @State
    public int getState() {
        return mShowState;
    }

    public void setState(@State int state) {
        if (state != SHOW_INITIAL && state != SHOW_IMAGE) {
            String msg = "Illegal avatar state value: " + state + ", use either SHOW_INITIAL or SHOW_IMAGE constant";
            throw new IllegalArgumentException(msg);
        }
        mShowState = state;
        invalidate();
    }

    @Dimension
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    public void setTextSize(@Dimension float size) {
        mTextPaint.setTextSize(size);
        updateTextBounds();
        invalidate();
    }

    @ColorInt
    public int getTextColor() {
        return mTextPaint.getColor();
    }

    public void setTextColor(@ColorInt int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getAvatarBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void setAvatarBackgroundColor(@ColorInt int color) {
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    @NonNull
    private String extractInitial(@Nullable String letter) {
        if (letter == null || letter.trim().length() <= 0) return "?";
        return String.valueOf(letter.charAt(0));
    }

    private void updateTextBounds() {
        mTextPaint.getTextBounds(mInitial, 0, mInitial.length(), mTextBounds);
    }
}
