package ir.ea2.android_services.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
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

import static ir.ea2.android_services.MainActivity.TAG;

public class DownloadBoundedService extends Service {
    private DownloadBinder downloadBinder = new DownloadBinder();
    private DownloadListener downloadListener = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "BoundedService : onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "BoundedService : onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "BoundedService : onBind");

        return downloadBinder;
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e(TAG, "BoundedService : onRebind");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "BoundedService : onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "BoundedService : onDestroy");

    }

    public class DownloadBinder extends Binder {
        public void startDownload(final String url) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloader(url);
                }
            }).start();
        }

       public void setDownloadListener(DownloadListener listener) {
            downloadListener = listener;
        }
    }

    public interface DownloadListener {
        void downloadedPercent(int percent);
    }

    public void downloader(String url) {
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
                    if (downloadListener != null) {
                        downloadListener.downloadedPercent(percent);
                    }
                }
                fos.write(data, 0, count);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
