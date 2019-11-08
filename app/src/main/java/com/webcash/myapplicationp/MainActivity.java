package com.webcash.myapplicationp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private Paint mPaint;
    private MaskFilter mEmboos;
    private MaskFilter mBlur;
    private boolean aBoolean=true;

    Button btnClear;

    Canvas mCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_main, null);
        addContentView(v, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

        Button button = v.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {

            Paint clearPaint  = new Paint();
            Xfermode xmode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);


            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "색변경", Toast.LENGTH_SHORT).show();

                if(aBoolean) mPaint.setColor(Color.WHITE);
                else mPaint.setColor(Color.RED);
                aBoolean = !aBoolean;
            }
        });


        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "지우기", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();

            }
        });
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mEmboos = new EmbossMaskFilter(new float[] { 1, 1, 1}, 0.4f, 6,3.5f);
        mBlur= new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

    }
    public class MyView extends View {

        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;


        public MyView(Context c) {
            super(c);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        }

        public MyView(Context c, AttributeSet att) {
            super(c, att);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        public MyView(Context c, AttributeSet att, int ref) {
            super(c, att, ref);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFAAAAAA);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }


        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

    }
}
