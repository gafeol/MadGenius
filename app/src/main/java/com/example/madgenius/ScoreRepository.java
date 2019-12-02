package com.example.madgenius;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {
    private ScoreDao scoreDao;
    private LiveData<List<Score>> allScores;

    ScoreRepository(Application application) {
        ScoreRoomDatabase db = ScoreRoomDatabase.getDatabase(application);
        scoreDao = db.scoreDao();
        allScores = scoreDao.getScores();
    }

    LiveData<List<Score>> getAllScores() {
        return allScores;
    }

    void insert(Score score) {
        ScoreRoomDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.insert(score);
        });
    }
}