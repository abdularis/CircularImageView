package com.github.abdularis.civ;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    private static final int DEF_PRESS_HIGHLIGHT_COLOR = 0x32000000;

    private Shader mBitmapShader;
    private Matrix mShaderMatrix;

    private RectF mBitmapDrawBounds;
    private RectF mStrokeBounds;

    private Bitmap mBitmap;

    private Paint mBitmapPaint;
    private Paint mStrokePaint;
    private Paint mPressedPaint;

    private boolean mInitialized;
    private boolean mPressed;
    private boolean mHighlightEnable;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int strokeColor = Color.TRANSPARENT;
        float strokeWidth = 0;
        boolean highlightEnable = true;
        int highlightColor = DEF_PRESS_HIGHLIGHT_COLOR;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);

            strokeColor = a.getColor(R.styleable.CircleImageView_strokeColor, Color.TRANSPARENT);
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_strokeWidth, 0);
            highlightEnable = a.getBoolean(R.styleable.CircleImageView_highlightEnable, true);
            highlightColor = a.getColor(R.styleable.CircleImageView_highlightColor, DEF_PRESS_HIGHLIGHT_COLOR);

            a.recycle();
        }

        mShaderMatrix = new Matrix();
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokeBounds = new RectF();
        mBitmapDrawBounds = new RectF();
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(strokeWidth);

        mPressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPressedPaint.setColor(highlightColor);
        mPressedPaint.setStyle(Paint.Style.FILL);

        mHighlightEnable = highlightEnable;
        mInitialized = true;

        setupBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        setupBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap();
    }

    @Override
    public void setImageBitmap(@Nullable Bitmap bm) {
        super.setImageBitmap(bm);
        setupBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        setupBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float halfStrokeWidth = mStrokePaint.getStrokeWidth() / 2f;
        updateCircleDrawBounds(mBitmapDrawBounds);
        mStrokeBounds.set(mBitmapDrawBounds);
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth);

        updateBitmapSize();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CircleImageViewOutlineProvider(mStrokeBounds));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean processed = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isInCircle(event.getX(), event.getY())) {
                    return false;
                }
                processed = true;
                mPressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                processed = true;
                mPressed = false;
                invalidate();
                if (!isInCircle(event.getX(), event.getY())) {
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event) || processed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawStroke(canvas);
        drawHighlight(canvas);
    }

    public boolean isHighlightEnable() {
        return mHighlightEnable;
    }

    public void setHighlightEnable(boolean enable) {
        mHighlightEnable = enable;
        invalidate();
    }

    @ColorInt
    public int getHighlightColor() {
        return mPressedPaint.getColor();
    }

    public void setHighlightColor(@ColorInt int color) {
        mPressedPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getStrokeColor() {
        return mStrokePaint.getColor();
    }

    public void setStrokeColor(@ColorInt int color) {
        mStrokePaint.setColor(color);
        invalidate();
    }

    @Dimension
    public float getStrokeWidth() {
        return mStrokePaint.getStrokeWidth();
    }

    public void setStrokeWidth(@Dimension float width) {
        mStrokePaint.setStrokeWidth(width);
        invalidate();
    }

    protected void drawHighlight(Canvas canvas) {
        if (mHighlightEnable && mPressed) {
            canvas.drawOval(mBitmapDrawBounds, mPressedPaint);
        }
    }

    protected void drawStroke(Canvas canvas) {
        if (mStrokePaint.getStrokeWidth() > 0f) {
            canvas.drawOval(mStrokeBounds, mStrokePaint);
        }
    }

    protected void drawBitmap(Canvas canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint);
    }

    protected void updateCircleDrawBounds(RectF bounds) {
        float contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        float left = getPaddingLeft();
        float top = getPaddingTop();
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f;
        } else {
            top += (contentHeight - contentWidth) / 2f;
        }

        float diameter = Math.min(contentWidth, contentHeight);
        bounds.set(left, top, left + diameter, top + diameter);
    }

    private void setupBitmap() {
        if (!mInitialized) {
            return;
        }
        mBitmap = getBitmapFromDrawable(getDrawable());
        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setShader(mBitmapShader);

        updateBitmapSize();
    }

    private void updateBitmapSize() {
        if (mBitmap == null) return;

        float dx;
        float dy;
        float scale;

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (mBitmap.getWidth() < mBitmap.getHeight()) {
            scale = mBitmapDrawBounds.width() / (float)mBitmap.getWidth();
            dx = mBitmapDrawBounds.left;
            dy = mBitmapDrawBounds.top - (mBitmap.getHeight() * scale / 2f) + (mBitmapDrawBounds.width() / 2f);
        } else {
            scale = mBitmapDrawBounds.height() / (float)mBitmap.getHeight();
            dx = mBitmapDrawBounds.left - (mBitmap.getWidth() * scale / 2f) + (mBitmapDrawBounds.width() / 2f);
            dy = mBitmapDrawBounds.top;
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(dx, dy);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private boolean isInCircle(float x, float y) {
        // find the distance between center of the view and x,y point
        double distance = Math.sqrt(
                Math.pow(mBitmapDrawBounds.centerX() - x, 2) + Math.pow(mBitmapDrawBounds.centerY() - y, 2)
        );
        return distance <= (mBitmapDrawBounds.width() / 2);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class CircleImageViewOutlineProvider extends ViewOutlineProvider {

        private Rect mRect;

        CircleImageViewOutlineProvider(RectF rect) {
            mRect = new Rect(
                    (int) rect.left,
                    (int) rect.top,
                    (int) rect.right,
                    (int) rect.bottom
            );
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(mRect);
        }

    }
}
