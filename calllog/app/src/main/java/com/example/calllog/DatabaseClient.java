package com.example.calllog;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
  private static AppDatabase appDatabase;

  public static AppDatabase getInstance(Context context) {
    if (appDatabase == null) {
      appDatabase =
          Room.databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, "call_log_database")
              .build();
    }
    return appDatabase;
  }
}
