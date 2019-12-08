package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, scores -> {
            adapter.setScores(scores);
        });

        List<Double> listScores = new ArrayList<Double>();
        scoreViewModel.getAllScores("cobra", true).observe(this, scores -> {
            listScores.addAll(scores);
            for(int i=0;i<listScores.size();i++){
                Log.d("SCORES", "Score "+listScores.get(i));
            }
        });

    }
}
