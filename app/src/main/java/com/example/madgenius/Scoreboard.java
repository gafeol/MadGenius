package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Scoreboard extends AppCompatActivity {
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
}
