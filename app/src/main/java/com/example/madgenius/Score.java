package com.example.madgenius;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "score_table")
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;
    @NonNull
    @ColumnInfo(name = "points")
    private int points;
    /**
     * gameType false -> Agility game
     * gameType true -> Memory game
     */
    @NonNull
    @ColumnInfo(name="gameType")
    private boolean gameType;

    public Score(@NonNull String username, @NonNull int points, @NonNull boolean gameType){
        this.username = username;
        this.points = points;
        this.gameType = gameType;
    }

    @Ignore
    public Score(int id, @NonNull String username, @NonNull int points, @NonNull boolean gameType){
        this.id = id;
        this.username = username;
        this.points = points;
        this.gameType = gameType;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getUsername(){
        return this.username;
    }

    public int getPoints() {
        return this.points;
    }

    public boolean getGameType() { return this.gameType; }
}

