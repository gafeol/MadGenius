package com.example.madgenius;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    private TextView helloTextView, loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setHelloMessage();
    }

    /**
     * Sets the message to greet user at the Menu
     */
    private void setHelloMessage(){
        helloTextView = findViewById(R.id.helloTextView);
        loginTextView = findViewById(R.id.loginTextView);
        String username = SavedInfo.getUsername(getApplicationContext());
        if(!username.isEmpty()) {
            helloTextView.setText("Hello " + username + "!");
            loginTextView.setText("Not you?");
        }
        else{
            loginTextView.setText("Click here to login");
        }
    }


    /**
     * Dialog allowing user to login in the application.
     * Works as an onClick function for loginTextView
     */
    public void loginMessage(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View messageView = getLayoutInflater().inflate(R.layout.dialog_login, null);

        EditText usernameEditText = messageView.findViewById(R.id.usernameEditText);
        String savedUsername = SavedInfo.getUsername(getApplicationContext());
        usernameEditText.setText(savedUsername);

        Button saveButton = messageView.findViewById(R.id.saveButton);
        Button cancelButton = messageView.findViewById(R.id.cancelButton);
        mBuilder.setView(messageView);
        AlertDialog dialog = mBuilder.create();

        saveButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if(username.isEmpty())
                usernameEditText.setError("Please fill your username");
            else {
                // Saves username for future use in the application
                SavedInfo.saveUsername(getApplicationContext(), username);
                setHelloMessage();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    /**
     * On resume it is necessary to reset the Hello Message, since the username saved may have changed.
     */
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

    public void tutorial_onClick(View view) {
        Intent intent = new Intent(Menu.this, Tutorial.class);
        startActivity(intent);
    }
}
