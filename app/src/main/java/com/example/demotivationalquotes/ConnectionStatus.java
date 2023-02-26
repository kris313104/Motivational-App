package com.example.demotivationalquotes;

import android.net.ConnectivityManager;

public class ConnectionStatus {
    public boolean isConnectedToInternet() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
