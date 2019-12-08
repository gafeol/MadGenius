package com.example.madgenius;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {
    private ScoreRepository repository;

    public ScoreViewModel (Application application) {
        super(application);
        repository = new ScoreRepository(application);
    }

    public void insert(Score score) { repository.insert(score); }
    public void deleteAll() { repository.deleteAll(); }

    public LiveData<List<Score>> getAllScores() { return repository.getAllScores(); }
    public LiveData<List<Score>> getAllScoresOrdered() { return repository.getAllScoresOrdered(); }
    public LiveData<List<Score>> getAllScores(String username) { return repository.getAllScores(username); }
    public LiveData<List<Score>> getAllScoresOrdered(String username) { return repository.getAllScoresOrdered(username); }
    public LiveData<List<Score>> getAllScores(boolean gameType) { return repository.getAllScores(gameType); }
    public LiveData<List<Score>> getAllScoresOrdered(boolean gameType) { return repository.getAllScoresOrdered(gameType); }
    public LiveData<List<Double>> getAllScores(String username, boolean gameType) { return repository.getAllScores(username, gameType); }
    public LiveData<List<Double>> getAllScoresOrdered(String username, boolean gameType) { return repository.getAllScoresOrdered(username, gameType); }

    public Score getHighestScore() { return repository.getHighestScore(); }
    public Score getHighestScore(String username) { return repository.getHighestScore(username); }
    public Double getHighestScore(String username, boolean gameType) { return repository.getHighestScore(username, gameType); }
}
