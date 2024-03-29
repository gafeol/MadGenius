package com.example.madgenius;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// When you modify the database schema, you'll need to update the version number and define a migration strategy
@Database(entities = {Score.class}, version=3, exportSchema = false)
public abstract class ScoreRoomDatabase extends RoomDatabase {
    public abstract ScoreDao scoreDao();

    private static volatile ScoreRoomDatabase INSTANCE;

    static ScoreRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScoreRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ScoreRoomDatabase.class, "score_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ScoreDao mDao;

        String[] usernames = {"Gabriel", "Gabriel", "Gabriel", "Pedro", "Pedro", "Pedro", "Pedro", "Ivan", "Ivan", "Ivan"};
        int[] points = {1, 5, 21, 25, 10, 4, 5, 10, 8, 8};
        boolean[] gameTypes = {false, true, false, false, true, true, false, false, false, true};

        PopulateDbAsync(ScoreRoomDatabase db) {
            mDao = db.scoreDao();
        }

        /** If the db has entries, load them.
         * Otherwise insert the pre-defined user scores above.
         */
        @Override
        protected Void doInBackground(final Void... params) {
            if(mDao.getAnyScore().length == 0){
                for (int i = 0; i <= usernames.length - 1; i++) {
                    Score score = new Score(usernames[i], points[i], gameTypes[i]);
                    mDao.insert(score);
                }
            }
            return null;
        }
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
}
