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
    private double points;

    public Score(@NonNull String username, @NonNull double points){
        this.username = username;
        this.points = points;
    }

    @Ignore
    public Score(int id, @NonNull String username, @NonNull double points){
        this.id = id;
        this.username = username;
        this.points = points;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getUsername(){
        return this.username;
    }

    public double getPoints() {
        return this.points;
    }
}

