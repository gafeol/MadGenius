package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        scoreViewModel.getAllScores().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.setScores(words);
        });

<<<<<<< HEAD

=======
>>>>>>> 9e10435b5ff0a3e8736273f806ac8374b51c661f
    }
}
