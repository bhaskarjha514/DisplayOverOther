package com.androidllc.displayoverotherapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.androidllc.displayoverotherapp.Service.FloatingFaceBubbleService;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, FloatingFaceBubbleService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent2, 0);
            }else{
                openYoutube("https://www.youtube.com/watch?v=VTjF4mP9smU&ab_channel=Codeinger");
                startService(intent);
            }
        }else{
            openYoutube("https://www.youtube.com/watch?v=VTjF4mP9smU&ab_channel=Codeinger");
            startService(intent);
        }
        Log.d("TAG",String.valueOf(isMyServiceRunning(FloatingFaceBubbleService.class)));
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0){
            Intent intent = new Intent(MainActivity.this, FloatingFaceBubbleService.class);
            openYoutube("https://www.youtube.com/watch?v=VTjF4mP9smU&ab_channel=Codeinger");
            startService(intent);
        }
    }
    private void openYoutube(String link) {
        Intent intent = new Intent(

                Intent.ACTION_VIEW ,
                Uri.parse(link));
        intent.setPackage("com.google.android.youtube");
        PackageManager manager = getPackageManager();
        List<ResolveInfo> info = manager.queryIntentActivities(intent, 0);
        if (info.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Youtube is not installed", Toast.LENGTH_SHORT).show();
            //No Application can handle your intent
        }
    }
}