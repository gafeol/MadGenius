package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setHelloMessage();
    }

    private void setHelloMessage(){
        helloTextView = findViewById(R.id.helloTextView);
        String username = SavedInfo.getUsername(getApplicationContext());
        if(!username.isEmpty())
            helloTextView.setText("Hello "+username+"!");
    }

    @Override
    public void onResume() {
        super.onResume();
        setHelloMessage();
    }

    public void memory_onClick(View view){
        Intent intent = new Intent(Menu.this, GameplayMemory.class);
        startActivity(intent);
    }

    public void agility_onClick(View view) {
        Intent intent = new Intent(Menu.this, GameplayAgility.class);
        startActivity(intent);
    }

    public void scoreboard_onClick(View view) {
        Intent intent = new Intent(Menu.this, Scoreboard.class);
        startActivity(intent);
    }
}
