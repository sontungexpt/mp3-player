package com.example.calllog;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CallLogEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CallLogDao callLogDao();
}
