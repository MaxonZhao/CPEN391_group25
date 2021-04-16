package com.cpen391.flappyVoiceRecording.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cpen391.flappyVoiceRecording.util.DbUtil;
import com.cpen391.flappyaccount.R;

public class SoundDiscView extends androidx.appcompat.widget.AppCompatImageView {
    private int width, height;
    private final Matrix mMatrix = new Matrix();
    private Bitmap bitmap;
    private Paint paint = new Paint();

    public SoundDiscView(Context context) {
        super(context);
    }

    public SoundDiscView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noise_index);
        int bitmapWidth = myBitmap.getWidth();
        int bitmapHeight = myBitmap.getHeight();
        width = getWidth();
        height = getHeight();
        float scaleWidth = ((float) width) / (float) bitmapWidth;
        float scaleHeight = ((float) height) / (float) bitmapHeight;
        mMatrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(myBitmap, 0, 0, bitmapWidth, bitmapHeight, mMatrix, true);

        paint = new Paint();
        paint.setTextSize(22 * getDensity(getContext()));
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
    }

    public void refresh() {
        postInvalidateDelayed(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            init();
        }
        mMatrix.setRotate(getAngle(DbUtil.dbCount), width / 2, height * 215 / 460);
        canvas.drawBitmap(bitmap, mMatrix, paint);
        canvas.drawText((int) DbUtil.dbCount + " DB", width / 2, height * 36 / 46, paint);
    }

    private float getAngle(float db) {
        return (db - 85) * 5 / 3;
    }

    public static float getDensity(Context context) {
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }


}
