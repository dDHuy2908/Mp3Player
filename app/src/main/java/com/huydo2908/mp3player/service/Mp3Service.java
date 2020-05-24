package com.huydo2908.mp3player.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.controller.MediaController;
import com.huydo2908.mp3player.controller.MediaListener;
import com.huydo2908.mp3player.models.Song;
import com.huydo2908.mp3player.models.SongInfo;

import java.util.ArrayList;

public class Mp3Service extends Service implements MediaListener, Runnable {

    private MediaController controller;
    private MutableLiveData<SongInfo> info = new MutableLiveData<>();
    private Thread t;

    private final String ACTION_NEXT = "action.NEXT";
    private final String ACTION_PREVIOUS = "action.PREVIOUS";
    private final String ACTION_CLOSE = "action.CLOSE";
    private final String ACTION_PAUSE = "action.PAUSE";

    public MutableLiveData<SongInfo> getInfo() {
        return info;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        info.postValue(new SongInfo());

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PREVIOUS);
        filter.addAction(ACTION_CLOSE);
        filter.addAction(ACTION_PAUSE);
        registerReceiver(receiver, filter);
//        Log.e(getClass().getSimpleName(), "onCreate");
    }

    private void pushNotification() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
        SongInfo value = info.getValue();
        value.setPlaying(controller.isPlaying());
        value.setStarting(true);
        value.setName(controller.getName());
        value.setDuration(controller.getDuration());
        value.setArtist(controller.getArtist());
        info.postValue(value);

        Intent intent = new Intent(this, getClass());
        startService(intent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channel = "Mp3Service";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel c = new NotificationChannel(
                    channel, channel, NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(c);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.tv_name, controller.getName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, channel
        );
        remoteViews.setImageViewResource(R.id.im_play, controller.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);

        regisAction(remoteViews, ACTION_CLOSE, R.id.im_close);
        regisAction(remoteViews, ACTION_PREVIOUS, R.id.im_prev);
        regisAction(remoteViews, ACTION_NEXT, R.id.im_next);
        regisAction(remoteViews, ACTION_PAUSE, R.id.im_play);

        builder.setSmallIcon(R.drawable.logo);
        builder.setCustomBigContentView(remoteViews);
        startForeground(100000, builder.build());
    }

    private void regisAction(RemoteViews remoteViews, String action, int id) {
        Intent intent = new Intent(action);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(id, pending);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(), "onBind");
        return new Mp3Binder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(getClass().getSimpleName(), "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Log.e(Mp3Service.this.getClass().getSimpleName(), System.currentTimeMillis() + "");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.e(getClass().getSimpleName(), "onDestroy");
    }

    public void setSongData(ArrayList<Song> arrSong) {
        if (controller != null) {
            controller.release();
        }
        controller = new MediaController(this, arrSong, this);
    }

    public MediaController getController() {
        return controller;
    }

    @Override
    public void onPlay() {
        pushNotification();
    }

    @Override
    public void onPause() {
        pushNotification();
    }

    @Override
    public void run() {
        while (true) {
            try {
                SongInfo value = info.getValue();
                value.setPosition(controller.getCurrentPosition());
                info.postValue(value);

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Mp3Binder extends Binder {

        private Mp3Service service;

        public Mp3Binder(Mp3Service service) {
            this.service = service;
        }

        public Mp3Service getService() {
            return service;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_CLOSE:
                    info.postValue(new SongInfo());
                    t.interrupt();
                    controller.release();
                    controller = null;
                    stopForeground(true);
                    stopSelf();
                    break;
                case ACTION_NEXT:
                    controller.change(1);
                    break;
                case ACTION_PREVIOUS:
                    controller.change(-1);
                    break;
                case ACTION_PAUSE:
                    if (controller.isPlaying()) {
                        controller.pause();
                    } else {
                        controller.start();
                    }
                    break;
            }
        }
    };
}
