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

    @Query("SELECT * FROM score_table ORDER BY points DESC")
    LiveData<List<Score>> getScores();

    @Query("SELECT * FROM score_table ORDER BY points DESC LIMIT 1")
    Score getHighestScore();

    @Query("SELECT * FROM score_table WHERE username = :username ORDER BY points DESC LIMIT 1")
    Score getHighestScore(String username);

    /** Used to check if there is any entry on the db
     * If length = 0, the db is empty, otherwise, it has entries
     * @return Score[]
     */
    @Query("SELECT * FROM score_table LIMIT 1")
    Score[] getAnyScore();
}
