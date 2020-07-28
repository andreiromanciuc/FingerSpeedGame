package com.andrei.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView aThousandTextView;
    private Button tapTapButton;

    private CountDownTimer countDownTimer;

    private long initialCountDownInMillis = 60000;
    private int timerInterval = 1000;
    private int remainingTime = 60;
    private int aThousand = 10;

    private final String REMAINING_TIME_KEY = "remaining time key";
    private final String A_THOUSAND_KEY = "a thousand key";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(REMAINING_TIME_KEY, remainingTime);
        outState.putInt(A_THOUSAND_KEY, aThousand);
        countDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.txtTimer);
        aThousandTextView = findViewById(R.id.txtAThousand);
        tapTapButton = findViewById(R.id.btnTap);

        aThousandTextView.setText(aThousand+"");

        if (savedInstanceState != null) {

            remainingTime = savedInstanceState.getInt(REMAINING_TIME_KEY);
            aThousand = savedInstanceState.getInt(A_THOUSAND_KEY);

            restoreTheGame();

        }

        tapTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aThousand--;
                aThousandTextView.setText(aThousand+"");

                if (remainingTime > 0 && aThousand <= 0) {
                    showAlert("Congratulations", "Would you like to restart the game?");
                    countDownTimer.cancel();
                }

            }
        });

        if (savedInstanceState == null) {

            countDownTimer = new CountDownTimer(initialCountDownInMillis, timerInterval) {
                @Override
                public void onTick(long millisUntilFinished) {

                    remainingTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(remainingTime + "");

                }

                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                    showAlert("Time up!", "Would you like to reset the game?");
                }
            };
            countDownTimer.start();

        }

    }

    private void restoreTheGame() {

        int restoredRemainingTime = remainingTime;
        int restoredAThousand = aThousand;

        timerTextView.setText(restoredRemainingTime+"");
        aThousandTextView.setText(restoredAThousand+"");

        countDownTimer = new CountDownTimer((long) remainingTime * 1000, timerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000;
                timerTextView.setText(remainingTime+"");

            }

            @Override
            public void onFinish() {
                showAlert("Congratulations", "Would you like to restart the game?");
            }
        };
    }

    private void resetTheGame() {

        aThousand = 1000;

        aThousandTextView.setText(aThousand+"");

        timerTextView.setText(remainingTime + "");

        countDownTimer = new CountDownTimer(initialCountDownInMillis, timerInterval) {
            @Override
            public void onTick(long millisToFinish) {

                remainingTime =(int) millisToFinish / 1000;
                timerTextView.setText(remainingTime + "");

            }

            @Override
            public void onFinish() {
                showAlert("Congratulations", "Would you like to restart the game?");
            }
        };
        countDownTimer.start();
    }

    private void showAlert(String title, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetTheGame();
                    }
                }).show();
        alertDialog.setCancelable(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.info_item) {
            showAlert("Alert", BuildConfig.VERSION_NAME);

        }

        return true;

    }
}