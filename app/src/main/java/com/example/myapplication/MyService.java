package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    //定义好接收对象
    MyReceiver serviceReceiver;
    int status = 0;
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d("service", "service is created");
        super.onCreate();

        serviceReceiver = new MyReceiver();

        // 创建IntentFilter.所以配置文件中就不需要再配置action了
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.ACTION);
        registerReceiver(serviceReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "service is started");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "service is destroied");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            // 广播通知Activity更改文本框
            Intent sendIntent = new Intent(MainActivity.FLAGACTION);
            //获取一个随机数0-4
            status= (int)(Math.random()*5);
            sendIntent.putExtra("flag", status);
            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
            sendBroadcast(sendIntent);
        }
    }
}


