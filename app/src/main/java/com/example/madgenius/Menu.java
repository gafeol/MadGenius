package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void agility_onClick(View view) {
        Intent intent = new Intent(Menu.this, GameplayAgility.class);
        startActivity(intent);
    }
}
