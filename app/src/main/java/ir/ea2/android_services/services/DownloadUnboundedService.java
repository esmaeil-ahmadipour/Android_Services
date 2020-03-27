package ir.ea2.android_services.services;

import android.app.Service;
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
import static ir.ea2.android_services.MainActivity.TAG;
import static ir.ea2.android_services.MainActivity.URL_KEY;
public class DownloadUnboundedService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {

            final String url = intent.getExtras().getString(URL_KEY);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    download(url, "");
                }
            }).start();

        }
        // * After System Close Service , Can't Run Again Service.
        // return START_NOT_STICKY;
        // * After Close Service , Rerun Service Without (Last Intent) . Note : In This Situation Intent Is Null.
        // return START_STICKY;
        // * After System Close Service , Rerun Service With (Last Intent) .
        return START_REDELIVER_INTENT;
    }

    public void download(String url, String path) {
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
                if (contentLength > 0) {
                    Log.e("Downloader", "Percent: " + ((int) (total * 100 / contentLength)));
                }
                fos.write(data, 0, count);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
