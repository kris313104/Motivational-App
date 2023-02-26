package com.example.demotivationalquotes;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Button newQuoteButton = findViewById(R.id.newQuoteButton);

        TextView quoteText = findViewById(R.id.quoteText);

        TextView authorText = findViewById(R.id.authorText);

        QuoteFinder quoteFinder = new QuoteFinder();
        List<String> quotes = null;
        try {
            quotes = quoteFinder.searchQuote();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (quotes == null) {
            quoteText.setText("No internet connection");
        } else {
            quoteText.setText(quotes.get(0));
            authorText.setText(quotes.get(1));

            String quote = quotes.get(0);
        }
        newQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> quotes = null;
                try {
                    quotes = quoteFinder.searchQuote();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (quotes == null) {
                    quoteText.setText("No internet connection");
                    authorText.setText("");
                } else {
                    quoteText.setText(quotes.get(0));
                    authorText.setText(quotes.get(1));
                }
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }


}


