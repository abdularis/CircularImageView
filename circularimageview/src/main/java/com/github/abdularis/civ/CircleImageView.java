package com.github.abdularis.civ;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    private Shader mBitmapShader;
    private Matrix mShaderMatrix;

    private RectF mBitmapDrawBounds;
    private RectF mStrokeBounds;

    private Bitmap mBitmap;

    private Paint mBitmapPaint;
    private Paint mStrokePaint;

    private boolean mInitialized;

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
        mBitmapDrawBounds.set(0, 0, getWidth(), getHeight());
        mStrokeBounds.set(mBitmapDrawBounds);
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth);

        updateBitmapSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawStroke(canvas);
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

        float dx = 0;
        float dy = 0;
        float scale;

        // scale up/down with respect to this view size and maintain aspect ratio
        if (mBitmap.getWidth() < mBitmap.getHeight()) {
            scale = (float)getWidth() / (float)mBitmap.getWidth();
            dy = (mBitmap.getHeight() * scale) * 0.5f - (getHeight() * 0.5f);
        } else {
            scale = (float)getHeight() / (float)mBitmap.getHeight();
            dx = (mBitmap.getWidth() * scale) * 0.5f - (getWidth() * 0.5f);
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(-dx, -dy);
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
}
