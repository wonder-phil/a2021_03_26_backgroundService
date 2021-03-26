package me.pgb.a2021_03_26_boundbackgroundservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.pgb.a2021_03_26_boundbackgroundservice.R;
import me.pgb.a2021_03_26_boundbackgroundservice.service.RadioService;

import android.os.IBinder;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_DEFAULT_IMPORTANCE = "notification action high";
    private static final Object ONGOING_NOTIFICATION_ID = 1;
    private RadioService mService;
    private boolean mBound = false;
    private Button binderButton;
    private Button stopBackgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binderButton = findViewById(R.id.binder_button);
        stopBackgroundThread = findViewById(R.id.stop_background_thread_button);

        binderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = mService.getCounter();
                Toast.makeText(getApplicationContext(), "number: " + String.valueOf(num).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        stopBackgroundThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.stopBackgroundCounter();
            }
        });
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            RadioService.LocalBinder binder = (RadioService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RadioService.class);
        intent.putExtra("data", "Hello!");

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }
}