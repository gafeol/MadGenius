package com.example.madgenius;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Score score);

    @Query("DELETE FROM score_table")
    void deleteAll();

    @Query("SELECT * FROM score_table")
    LiveData<List<Score>> getScores();

    @Query("SELECT * FROM score_table ORDER BY points DESC")
    LiveData<List<Score>> getOrderedScores();

    @Query("SELECT * FROM score_table WHERE username = :username")
    LiveData<List<Score>> getScores(String username);

    @Query("SELECT * FROM score_table WHERE username = :username ORDER BY points DESC")
    LiveData<List<Score>> getOrderedScores(String username);

    @Query("SELECT points FROM score_table WHERE username = :username AND gameType = :gameType")
    LiveData<List<Double>> getScores(String username, boolean gameType);

    @Query("SELECT points FROM score_table WHERE username = :username AND gameType = :gameType ORDER BY points DESC")
    LiveData<List<Double>> getOrderedScores(String username, boolean gameType);

    @Query("SELECT * FROM score_table ORDER BY points DESC LIMIT 1")
    Score getHighestScore();

    @Query("SELECT * FROM score_table WHERE username = :username ORDER BY points DESC LIMIT 1")
    Score getHighestScore(String username);

    @Query("SELECT points FROM score_table WHERE username = :username AND gameType = :gameType ORDER BY points DESC LIMIT 1")
    Double getHighestScore(String username, boolean gameType);

    /** Used to check if there is any entry on the db
     * If length = 0, the db is empty, otherwise, it has entries
     * @return Score[]
     */
    @Query("SELECT * FROM score_table LIMIT 1")
    Score[] getAnyScore();
}
