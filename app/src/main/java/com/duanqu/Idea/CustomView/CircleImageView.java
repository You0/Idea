package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/27.
 */
public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if(null == drawable){
            return ;
        }

        // 将drawable转换成bitmap==>网上找的
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);

        Canvas srcCanvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

        drawable.draw(srcCanvas);

        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        System.out.println("getwidth"+cx+"getheight"+cy);
        float radius = Math.min(getWidth(), getHeight()) / 2;

        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.WHITE);

        canvas.drawCircle(cx, cy, radius, borderPaint);


        Matrix matrix = new Matrix();
        System.out.println("thebitmapHeight"+bitmap.getHeight());
        float tch = getHeight();
        float tcw = getWidth();
        float scale = tch/bitmap.getHeight() < tcw/bitmap.getWidth() ? tch/bitmap.getHeight():tcw/bitmap.getWidth();

        System.out.println(scale);
        matrix.postScale(scale,scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        // 画图
        BitmapShader shader = new BitmapShader(resizeBmp, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, radius, paint);


    }
}
