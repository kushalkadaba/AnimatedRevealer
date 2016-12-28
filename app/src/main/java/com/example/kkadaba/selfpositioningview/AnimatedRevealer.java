package com.example.kkadaba.selfpositioningview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * @author Kushal Kadaba(Fuzz).
 */

public class AnimatedRevealer extends View implements Animatable {

    private static final long ANIMATION_DURATION = 500;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    String[] options;
    private int measuredHeight;
    Matrix transformMatrix;
    Paint paint;
    int rootLayoutId;
    private RectF clipRect;
    private float xTranslation;
    private static final Property<AnimatedRevealer, Float> translationProp =
            new Property<AnimatedRevealer, Float>(Float.class, "xTranslation") {
                @Override
                public Float get(AnimatedRevealer object) {
                    return object.xTranslation;
                }

                @Override
                public void set(AnimatedRevealer object, Float value) {
                    object.xTranslation = value;
                    object.invalidate();
                }
            };
    private int measuredWidth;
    private Bitmap rootView;
    private ObjectAnimator animator;
    private boolean shouldAnimate;
    private int orientation;
    private boolean isReveal;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private boolean bitmapCopied;

    public AnimatedRevealer(Context context) {
        this(context, null);
    }

    public AnimatedRevealer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedRevealer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnimatedRevealer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRevealer);
        int optionsId = typedArray.getResourceId(R.styleable.AnimatedRevealer_options, 0);
        if (optionsId != 0) {
            options = getResources().getStringArray(optionsId);
        }
        int viewId = typedArray.getResourceId(R.styleable.AnimatedRevealer_rootLayout, 0);
        if (viewId != 0) {
            rootLayoutId = viewId;
        }
        orientation = typedArray.getInt(R.styleable.AnimatedRevealer_orientation, VERTICAL);
        isReveal = typedArray.getBoolean(R.styleable.AnimatedRevealer_reveal, false);
        typedArray.recycle();
        transformMatrix = new Matrix();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clipRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        measuredHeight = h;
        measuredWidth = w;
        if (!isReveal) {
            createBitmapsBasedOnOrientation(w, h);
        }

    }

    private void createBitmapsBasedOnOrientation(int w, int h) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rootView == null) {
            View view = getRootView().findViewById(rootLayoutId);
            if (view != null) {
                rootView = view.getDrawingCache();
                view.setDrawingCacheEnabled(true);
            }
            invalidate();
        } else {
            canvas.save();
            if (isReveal) {
                configureClipRect();
                clearCanvas(canvas);
                canvas.clipRect(clipRect, Region.Op.DIFFERENCE);
                canvas.drawBitmap(rootView, transformMatrix, paint);
            } else {
                if (!bitmapCopied) {
                    copyOnToBitmapBasedOnOrientation(rootView);
                    bitmapCopied = true;
                }
                clearCanvas(canvas);
                translateAndDraw(canvas);
            }
            canvas.restore();
            if (shouldAnimate) {
                shouldAnimate = false;
                animator.start();
            }
        }
    }

    private void translateAndDraw(Canvas canvas) {
        switch (orientation) {
            case HORIZONTAL:
                float midWay = getHeight() / 2f;
                canvas.translate(0, -xTranslation);
                canvas.drawBitmap(bitmap1, 0, 0, paint);
                canvas.restore();
                canvas.translate(0, xTranslation);
                canvas.drawBitmap(bitmap2, 0, midWay, paint);
                break;
            case VERTICAL:
                midWay = getWidth() / 2f;
                canvas.translate(-xTranslation, 0);
                canvas.drawBitmap(bitmap1, 0, 0, paint);
                canvas.restore();
                canvas.translate(xTranslation, 0);
                canvas.drawBitmap(bitmap2, midWay, 0, paint);
                break;
        }
    }

    private void copyOnToBitmapBasedOnOrientation(Bitmap rootView) {
        int w = rootView.getWidth();
        int h = rootView.getHeight();
        switch (orientation) {
            case HORIZONTAL:
                bitmap1 = Bitmap.createBitmap(rootView,0, 0, w, h / 2);
                bitmap2 = Bitmap.createBitmap(rootView, 0, h / 2, w, h/2);
                break;
            case VERTICAL:
                bitmap1 = Bitmap.createBitmap(rootView, 0, 0, w / 2, h);
                bitmap2 = Bitmap.createBitmap(rootView, w / 2, 0, w/ 2, h);
                break;
        }
    }

    private void configureClipRect() {
        switch (orientation) {
            case HORIZONTAL:
                float midWay = getHeight() / 2f;
                float yTop = midWay - xTranslation;
                float yBottom = midWay + xTranslation;
                clipRect.set(0f, yTop, measuredWidth, yBottom);
                break;
            case VERTICAL:
                midWay = getWidth() / 2f;
                float xLeft = midWay - xTranslation;
                float xRight = midWay + xTranslation;
                clipRect.set(xLeft, 0f, xRight, measuredHeight);
                break;
        }
    }

    private void clearCanvas(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(0f, 0f, (float) measuredWidth, (float) measuredHeight), paint);
    }

    @Override
    public void start() {
        animator = ObjectAnimator.ofFloat(this, translationProp, 0, getEndValue());
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateInterpolator());
        shouldAnimate = true;
    }

    private float getEndValue() {
        switch (orientation) {
            case HORIZONTAL:
                return measuredHeight;
            case VERTICAL:
                return measuredWidth;
        }
        return 0;
    }

    @Override
    public void stop() {
        if (animator.isRunning()) {
            animator.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return animator.isRunning();
    }
}
