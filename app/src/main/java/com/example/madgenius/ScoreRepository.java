package com.example.madgenius;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ScoreRepository {
    private ScoreDao scoreDao;

    ScoreRepository(Application application) {
        ScoreRoomDatabase db = ScoreRoomDatabase.getDatabase(application);
        scoreDao = db.scoreDao();
    }

    LiveData<List<Score>> getAllScores() {
        return scoreDao.getScores();
    }

    LiveData<List<Score>> getAllScores(String username){
        return scoreDao.getScores(username);
    }

    void insert(Score score) {
        new insertAsyncTask(scoreDao).execute(score);
    }

    void deleteAll() {
        new deleteAllAsyncTask(scoreDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private ScoreDao mAsyncTaskDao;
        deleteAllAsyncTask(ScoreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids){
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Score, Void, Void> {
        private ScoreDao mAsyncTaskDao;
        insertAsyncTask(ScoreDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Score... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    Score getHighestScore() {
        return scoreDao.getHighestScore();
    }

    Score getHighestScore(String username){
        return scoreDao.getHighestScore(username);
    }
}