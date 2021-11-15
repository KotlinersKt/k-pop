package com.kotlinerskt.exampleapp;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class WrongActivity extends ComponentActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
