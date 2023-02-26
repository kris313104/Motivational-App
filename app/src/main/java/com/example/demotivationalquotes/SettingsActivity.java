package com.example.demotivationalquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    int hour = -1;
    int minute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        createNotificationChannel();

        Button backButton = findViewById(R.id.notifyButtonBack);

        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        Switch lightSwitch = findViewById(R.id.lightSwitch);
        lightSwitch.setEnabled(true);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    try {
                        cameraManager.setTorchMode("0",true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    lightSwitch.setText("FLASH ON");
                }
                else {
                    try {
                        cameraManager.setTorchMode("0",false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    lightSwitch.setText("FLASH ON");
                }
            }
        });

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        });


        Button selectTimeButton = findViewById(R.id.selectTimeButton);

        selectTimeButton.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    selectTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }
            };

            int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

            timePickerDialog.setTitle(("Select time for a daily reminder"));
            timePickerDialog.show();
        });

        Button notifyButton = findViewById(R.id.notifyButton);

        notifyButton.setOnClickListener(view -> {
            ConnectionStatus connectionStatus = new ConnectionStatus();
            if (connectionStatus.isConnectedToInternet()) {
                if (hour != -1 && connectionStatus.isConnectedToInternet()) {

                    Intent intent = new Intent(SettingsActivity.this, ReminderBroadcast.class);


                    PendingIntent pendingIntent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        pendingIntent = PendingIntent.getBroadcast(this,
                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

                    } else {
                        pendingIntent = PendingIntent.getBroadcast(this,
                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    }

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    Calendar setTimeCalendar = Calendar.getInstance();

                    int hourNow = setTimeCalendar.get(setTimeCalendar.HOUR_OF_DAY);
                    int minuteNow = setTimeCalendar.get(setTimeCalendar.MINUTE);
                    int secondNow = setTimeCalendar.get(setTimeCalendar.SECOND);

                    System.out.println("current hour: " + hourNow);
                    System.out.println("current minute: " + minuteNow);
                    System.out.println("current second: " + secondNow);
                    setTimeCalendar.set(Calendar.HOUR_OF_DAY, hour);
                    setTimeCalendar.set(Calendar.MINUTE, minute);
                    setTimeCalendar.set(Calendar.SECOND, 00);

                    if (setTimeCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                        Log.e("setAlarm","time is in past");
                        setTimeCalendar.add(Calendar.DAY_OF_YEAR, 1); // it will tell to run to next day
                    }

                    System.out.println("reminder hour: " + setTimeCalendar.get(setTimeCalendar.HOUR_OF_DAY));
                    System.out.println("reminder minute: " + setTimeCalendar.get(setTimeCalendar.MINUTE));
                    System.out.println("reminder second: " + setTimeCalendar.get(setTimeCalendar.SECOND));

                    System.out.println("reminder in: " + (setTimeCalendar.get(setTimeCalendar.HOUR_OF_DAY) - hourNow));

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setTimeCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, setTimeCalendar.getTimeInMillis(), pendingIntent);
                    Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Time is not set!", Toast.LENGTH_SHORT).show();
                }
            } else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannel";
            String description = "Channel for motivational notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notcha1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}