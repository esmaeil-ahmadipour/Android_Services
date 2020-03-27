package ir.ea2.android_services.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import ir.ea2.android_services.R;

import static ir.ea2.android_services.MainActivity.CHANNEL_ID;
import static ir.ea2.android_services.MainActivity.TAG;
import static ir.ea2.android_services.MainActivity.URL_KEY;

public class DownloadForegroundService extends Service {
    private static final int NOTIF_ID = 700;
    private NotificationManager notificationManager;
    private Context context = this;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {

            final String url = intent.getExtras().getString(URL_KEY);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    startForeground(NOTIF_ID, getNotification(0));
                    downloader(url, "");
                    stopForeground(false);
                    stopSelf();
                }
            }).start();

        }
        return START_REDELIVER_INTENT;
    }

    public void downloader(String url, String path) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();
            DataInputStream stream = new DataInputStream(u.openStream());
            String filePath = Environment.getExternalStorageDirectory()
                    .getPath()
                    .toString() + "/Resume.jpg";
            File f = new File(filePath);
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(f, false));
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = stream.read(data)) != -1) {
                total += count;
                int percent = ((int) (total * 100 / contentLength));
                if (contentLength > 0) {
                    Log.e("Downloader", "Percent: " + percent);
                    notificationManager.notify(NOTIF_ID, getNotification(percent));
                }
                fos.write(data, 0, count);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Notification getNotification(int percent) {
        return new NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Download Manager")
                .setContentText("File In Downloading Progress")
                .setContentText(String.format("%d Percent Downloaded ..", percent))
                .setSmallIcon(R.drawable.ic_download)
                .build();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
