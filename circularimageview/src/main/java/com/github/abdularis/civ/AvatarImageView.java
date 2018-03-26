package com.github.abdularis.civ;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by abdularis on 22/03/18.
 */

public class AvatarImageView extends CircleImageView {

    public static final int SHOW_INITIAL_LETTER = 1;
    public static final int SHOW_IMAGE = 2;

    private static final String DEF_INITIAL_LETTER = "A";
    private static final int DEF_TEXT_SIZE = 90;
    private static final int DEF_BACKGROUND_COLOR = 0xE53935;
    private static final int DEF_STATE = SHOW_INITIAL_LETTER;

    private Paint mTextPaint;
    private Rect mTextBounds;

    private Paint mBackgroundPaint;
    private RectF mBackgroundBounds;

    private String mInitialLetter;

    private int mShowState;

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        String initWord = DEF_INITIAL_LETTER;
        int textColor = Color.WHITE;
        int textSize = DEF_TEXT_SIZE;
        int backgroundColor = DEF_BACKGROUND_COLOR;
        int showState = DEF_STATE;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0);

            initWord = a.getString(R.styleable.AvatarImageView_avatar_text);
            textColor = a.getColor(R.styleable.AvatarImageView_avatar_textColor, textColor);
            textSize = a.getDimensionPixelSize(R.styleable.AvatarImageView_avatar_textSize, textSize);
            backgroundColor = a.getColor(R.styleable.AvatarImageView_avatar_backgroundColor, backgroundColor);
            showState = a.getInt(R.styleable.AvatarImageView_avatar_state, showState);

            a.recycle();
        }

        mShowState = showState;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mTextBounds = new Rect();
        mInitialLetter = getFirstLetter(initWord);
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
        if (mShowState == SHOW_INITIAL_LETTER) {
            float textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY();
            canvas.drawOval(mBackgroundBounds, mBackgroundPaint);
            canvas.drawText(mInitialLetter, mBackgroundBounds.centerX(), textBottom, mTextPaint);
            drawStroke(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    public String getIntialLetter() {
        return mInitialLetter;
    }

    public void setInitialLetter(String letter) {
        mInitialLetter = getFirstLetter(letter);
        updateTextBounds();
        invalidate();
    }

    public int getState() {
        return mShowState;
    }

    public void setState(int state) {
        if (state != SHOW_INITIAL_LETTER && state != SHOW_IMAGE) {
            String msg = "Illegal avatar state value: " + state + ", use either SHOW_INITIAL_LETTER or SHOW_IMAGE constant";
            throw new IllegalArgumentException(msg);
        }
        mShowState = state;
        invalidate();
    }

    public float getLetterSize() {
        return mTextPaint.getTextSize();
    }

    public void setLetterSize(float size) {
        mTextPaint.setTextSize(size);
        updateTextBounds();
        invalidate();
    }

    public int getTextColor() {
        return mTextPaint.getColor();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public int getAvatarBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void setAvatarBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    private String getFirstLetter(String letter) {
        if (letter == null || letter.trim().length() <= 0) return "?";
        return String.valueOf(letter.charAt(0));
    }

    private void updateTextBounds() {
        mTextPaint.getTextBounds(mInitialLetter, 0, mInitialLetter.length(), mTextBounds);
    }
}
