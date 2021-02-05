package com.androidllc.displayoverotherapp.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import android.view.WindowManager.LayoutParams;

import com.androidllc.displayoverotherapp.R;

public class FloatingFaceBubbleService extends Service {
    IBinder myBinder = new MyBinder();

    private WindowManager windowManager;
    private ImageView floatingFaceBubble;

    @Override
    public void onCreate() {
        super.onCreate();

        floatingFaceBubble = new ImageView(this);

        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.ic_verified);
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        LayoutParams myParams;
        int LAYOUT_FLAG;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        myParams = new WindowManager.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);

        windowManager.addView(view, myParams);

        try{
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(System.currentTimeMillis()-touchStartTime> ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                        windowManager.removeView(view);
                        stopSelf();
                        return false;
                    }
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
    public class MyBinder extends Binder {
        public FloatingFaceBubbleService getService(){
            return FloatingFaceBubbleService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

}
