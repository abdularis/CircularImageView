package com.github.abdularis.civ;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int strokeColor = Color.TRANSPARENT;
        float strokeWidth = 0;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);

            strokeColor = a.getColor(R.styleable.CircleImageView_strokeColor, Color.TRANSPARENT);
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_strokeWidth, 0);

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
        mPressedPaint.setColor(DEF_PRESS_HIGHLIGHT_COLOR);
        mPressedPaint.setStyle(Paint.Style.FILL);

        mInitialized = true;

        setupBitmap();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setupBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
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
        mBitmapDrawBounds.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mStrokeBounds.set(mBitmapDrawBounds);
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth);

        updateBitmapSize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean processed = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isCoordinateInCircle(event.getX(), event.getY())) {
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
                if (!isCoordinateInCircle(event.getX(), event.getY())) {
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
        if (mPressed) {
            canvas.drawOval(mBitmapDrawBounds, mPressedPaint);
        }
    }

    public int getStrokeColor() {
        return mStrokePaint.getColor();
    }

    public void setStrokeColor(int color) {
        mStrokePaint.setColor(color);
        invalidate();
    }

    public float getStrokeWidth() {
        return mStrokePaint.getStrokeWidth();
    }

    public void setStrokeWidth(float width) {
        mStrokePaint.setStrokeWidth(width);
        invalidate();
    }

    protected void drawStroke(Canvas canvas) {
        if (mStrokePaint.getStrokeWidth() > 0f) {
            canvas.drawOval(mStrokeBounds, mStrokePaint);
        }
    }

    protected void drawBitmap(Canvas canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint);
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
            dx = getPaddingLeft();
            dy = getPaddingTop() - (mBitmap.getHeight() * scale / 2f) + (mBitmapDrawBounds.width() / 2f);
        } else {
            scale = mBitmapDrawBounds.height() / (float)mBitmap.getHeight();
            dx = getPaddingLeft() - (mBitmap.getWidth() * scale / 2f) + (mBitmapDrawBounds.width() / 2f);
            dy = getPaddingTop();
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

    private boolean isCoordinateInCircle(float x, float y) {
        // tentukan apakah titik x/y berada pada lingkarang (circle image view) ini dengan euclidean
        // distance dan jari-jarinya
        //
        // karena titik x/y dari touch event tidak berpusat ditengah melainkan kiri atas, maka
        // kita ketengahkan titik tersebut dengan mengurangi x dengan titik tengah view
        // (setengan width view) (x - this.centerX) dan -y + this.centerY
        PointF centeredPoint = new PointF(x - mBitmapDrawBounds.centerX(), (-y) + mBitmapDrawBounds.centerY());
        return centeredPoint.length() <= (mBitmapDrawBounds.width() / 2f);
    }
}
