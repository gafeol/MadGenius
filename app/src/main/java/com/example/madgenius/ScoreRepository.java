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

    void insert(Score score) { new insertAsyncTask(scoreDao).execute(score); }
    void deleteAll() { new deleteAllAsyncTask(scoreDao).execute(); }

    LiveData<List<Score>> getAllScores() { return scoreDao.getScores(); }
    LiveData<List<Score>> getAllScoresOrdered() { return scoreDao.getOrderedScores(); }
    LiveData<List<Score>> getAllScores(String username){ return scoreDao.getScores(username); }
    LiveData<List<Score>> getAllScoresOrdered(String username){ return scoreDao.getOrderedScores(username); }
    LiveData<List<Score>> getAllScores(boolean gameType) { return scoreDao.getScores(gameType);}
    LiveData<List<Score>> getAllScoresOrdered(boolean gameType) { return scoreDao.getOrderedScores(gameType);}
    LiveData<List<Double>> getAllScores(String username, boolean gameType) { return scoreDao.getScores(username, gameType);}
    LiveData<List<Double>> getAllScoresOrdered(String username, boolean gameType) { return scoreDao.getOrderedScores(username, gameType);}


    Score getHighestScore() { return scoreDao.getHighestScore(); }
    Score getHighestScore(String username){ return scoreDao.getHighestScore(username); }
    Double getHighestScore(String username, boolean gameType) { return scoreDao.getHighestScore(username, gameType); }

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
}