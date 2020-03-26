package ir.ea2.android_services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import ir.ea2.android_services.services.DownloadIntentService;

public class MainActivity extends AppCompatActivity {
    private Button btnRunService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setListener();

    }

    private void setListener() {
        btnRunService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this , DownloadIntentService.class));
            }
        });
    }

    private void setViews() {
        btnRunService=findViewById(R.id.ac_main_btn_runService);
    }
}

