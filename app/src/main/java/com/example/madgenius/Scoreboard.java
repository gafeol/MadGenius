package com.example.madgenius;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.TextView;


public class Scoreboard extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.madgenius.username";

    RadioButton agilityButton, memoryButton;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        agilityButton = findViewById(R.id.agility);
        memoryButton = findViewById(R.id.memory);

        setContentView(R.layout.activity_scoreboard);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScoresOrdered(true).observe(Scoreboard.this, scores -> {
            adapter.setScores(scores);
        });
    }

    public void agilityClick(View view){
        agilityButton = findViewById(R.id.agility);
        memoryButton = findViewById(R.id.memory);
        agilityButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        memoryButton.setTextColor(getResources().getColor(R.color.colorAccent));
        final TextView text = findViewById(R.id.bestof);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScoresOrdered(false).observe(Scoreboard.this, scores -> {
            adapter.setScores(scores);
        });
        text.setText(R.string.best_agility);
    }

    public void memoryClick(View view){
        agilityButton = findViewById(R.id.agility);
        memoryButton = findViewById(R.id.memory);
        agilityButton.setTextColor(getResources().getColor(R.color.colorAccent));
        memoryButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        final TextView text = findViewById(R.id.bestof);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScoresOrdered(true).observe(Scoreboard.this, scores -> {
            adapter.setScores(scores);
        });
        text.setText(R.string.best_memory);
    }

    public void scoreChartRequest(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View messageView = getLayoutInflater().inflate(R.layout.dialog_chart_request, null);
            EditText usernameEditText = messageView.findViewById(R.id.usernameditText);
            usernameEditText.setText(SavedInfo.getUsername(getApplicationContext()));
            CheckBox saveUsernameCheckBox = messageView.findViewById(R.id.saveUsernameCheckBox);
            Button findChartButton = messageView.findViewById(R.id.ButtonFind);
            Button cancelButton = messageView.findViewById(R.id.ButtonCancel);

            mBuilder.setView(messageView);
            AlertDialog dialog = mBuilder.create();

            findChartButton.setOnClickListener(v -> {
                String username = usernameEditText.getText().toString();
                if(username.isEmpty())
                    usernameEditText.setError("Please fill your username");
                else {
                    if(saveUsernameCheckBox.isChecked())
                        SavedInfo.saveUsername(getApplicationContext(), username);
                    Intent intent = new Intent(getApplicationContext(), ScoreChart.class);
                    intent.putExtra(EXTRA_MESSAGE, username);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
    }



