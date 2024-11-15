package com.example.w10_lab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBarRabbit, seekBarTurtle;
    private Button btnStart;
    private boolean isRacing = false;

    // Handler for updating UI from background threads
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // Rabbit progress update
                    seekBarRabbit.setProgress(msg.arg1);
                    if (msg.arg1 >= 100 && isRacing) {
                        isRacing = false;
                        Toast.makeText(MainActivity.this, "Rabbit Wins!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2: // Turtle progress update
                    seekBarTurtle.setProgress(msg.arg1);
                    if (msg.arg1 >= 100 && isRacing) {
                        isRacing = false;
                        Toast.makeText(MainActivity.this, "Turtle Wins!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBarRabbit = findViewById(R.id.seekBarRabbit);
        seekBarTurtle = findViewById(R.id.seekBarTurtle);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            if (!isRacing) {
                startRace();
            }
        });
    }

    private void startRace() {
        // Reset progress bars
        seekBarRabbit.setProgress(0);
        seekBarTurtle.setProgress(0);
        isRacing = true;

        // Start both threads
        runRabbit();
        runTurtle();
    }

    private void runRabbit() {
        new Thread(() -> {
            int progress = 0;
            while (progress < 100 && isRacing) {
                try {
                    // Rabbit runs faster but takes breaks
                    if (Math.random() < 0.3) { // 30% chance to sleep (taking a break)
                        Thread.sleep(300);
                    } else {
                        progress += 2; // Moves 2% each time
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void runTurtle() {
        new Thread(() -> {
            int progress = 0;
            while (progress < 100 && isRacing) {
                try {
                    // Turtle moves slowly but steadily
                    progress += 1; // Moves 1% each time
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    msg.arg1 = progress;
                    handler.sendMessage(msg);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}