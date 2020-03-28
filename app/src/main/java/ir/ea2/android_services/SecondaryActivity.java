package ir.ea2.android_services;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import ir.ea2.android_services.services.DownloadBoundedService;
import ir.ea2.android_services.services.DownloadBoundedService.DownloadBinder;

public class SecondaryActivity extends AppCompatActivity {
    private DownloadBinder binder = null;
    private boolean isBounded = false;
    private ProgressBar progressBar;
    private Button buttonRunService;
    public static final String url = "https://ea2.ir/uploads/Resume_Esmaeil_Ahmadipour.jpg";


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadBinder) service;
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
            binder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        setViews();
        setListeners();
    }

    private void setListeners() {
        buttonRunService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binder != null){
                    binder.startDownload(url);

                }
            }
        });
    }
//
    private void setViews() {
        progressBar = findViewById(R.id.ac_secondary_prg);
        buttonRunService = findViewById(R.id.ac_secondary_btn);
    }
//
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, DownloadBoundedService.class);

//        /*  Usable Flags in bindService :
//         *   BIND_AUTO_CREATE : Create Service For Once .
//         *   BIND_ABOVE_CLIENT & BIND_ADJUST_WITH_ACTIVITY : Try Save Service .
//         */
//
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
}
