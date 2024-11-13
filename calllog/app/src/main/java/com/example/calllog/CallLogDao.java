package com.example.calllog;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CallLogDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(CallLogEntity callLog);

  @Query("SELECT * FROM call_log ORDER BY date DESC")
  List<CallLogEntity> getAllCallLogs();
}
