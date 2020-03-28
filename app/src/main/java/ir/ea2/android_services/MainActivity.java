package ir.ea2.android_services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import ir.ea2.android_services.services.DownloadForegroundService;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="LOG_TAG";
    private Button btnRunService;
    public static final String URL_KEY = "URL_KEY";
    public static final String url = "https://ea2.ir/uploads/Resume_Esmaeil_Ahmadipour.jpg";
    public static final String CHANNEL_ID = "FOREGROUND_CHANNEL";
    private Button buttonGoSecondaryPage;

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
                Intent intentService = new Intent(MainActivity.this, DownloadForegroundService.class);
                intentService.putExtra(URL_KEY,url);
                startService(intentService);
            }
        });

        buttonGoSecondaryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navToSecondaryPage = new Intent(MainActivity.this , SecondaryActivity.class);

                startActivity(navToSecondaryPage);
            }
        });
    }

    private void setViews() {
        btnRunService = findViewById(R.id.ac_main_btn_runService);
        buttonGoSecondaryPage = findViewById(R.id.ac_main_btn_secondaryPage);

    }
}

