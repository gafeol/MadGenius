package com.example.madgenius;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Scoreboard extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.madgenius.username";



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscoreboard);
        final TextView text = (TextView)findViewById(R.id.bestof);
        final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            scoreViewModel.getAllScoresOrdered(isChecked).observe(Scoreboard.this, scores -> {
                adapter.setScores(scores);
            });
            if (isChecked) {
                text.setText(R.string.best_memory);
            } else {
                text.setText(R.string.best_agility);
            }
        });

        scoreViewModel.getAllScoresOrdered(true).observe(Scoreboard.this, scores -> {
            adapter.setScores(scores);
        });
    }




    public void scoreChartRequest(View view) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View messageView = getLayoutInflater().inflate(R.layout.dialog_chart_request, null);
            EditText usernameEditText = messageView.findViewById(R.id.usernameditText);

            Button findChartButton = messageView.findViewById(R.id.ButtonFind);
            Button cancelButton = messageView.findViewById(R.id.ButtonCancel);

            mBuilder.setView(messageView);
            AlertDialog dialog = mBuilder.create();

            findChartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameEditText.getText().toString();
                    if(username.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), ScoreChart.class);
                        intent.putExtra(EXTRA_MESSAGE, username);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }


            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }



