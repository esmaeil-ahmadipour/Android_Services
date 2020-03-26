package ir.ea2.android_services.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.Nullable;

public class DownloadIntentService extends IntentService {
    public static final String TAG = "downloadIntentService";



    public DownloadIntentService() {
        super("downloadIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, " Starting .. ");
        download("https://ea2.ir/uploads/Resume_Esmaeil_Ahmadipour.jpg","");
        Log.e(TAG, " Finished ");
    }
    public void download(String url,String path){
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();
            DataInputStream stream = new DataInputStream(u.openStream());
            String filePath = Environment.getExternalStorageDirectory().getPath().toString() + "/Resume.jpg";
            File f = new File(filePath);
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(f, false));
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = stream.read(data)) != -1) {
                total += count;
                if (contentLength > 0) {
                    Log.e(TAG,"Percent: "+((int) (total * 100 / contentLength)));
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
