package com.example.calllog;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import java.util.ArrayList;
import java.util.List;

public class CallLogHelper {
  public static List<CallLogEntity> getCallLogs(Context context) {
    List<CallLogEntity> callLogs = new ArrayList<>();
    Cursor cursor =
        context
            .getContentResolver()
            .query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

    if (cursor != null) {
      int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
      int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
      int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
      int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

      while (cursor.moveToNext()) {
        CallLogEntity callLog = new CallLogEntity();
        callLog.setNumber(cursor.getString(numberIndex));
        callLog.setType(cursor.getString(typeIndex));
        callLog.setDate(cursor.getLong(dateIndex));
        callLog.setDuration(cursor.getString(durationIndex));
        callLogs.add(callLog);
      }
      cursor.close();
    }
    return callLogs;
  }
}
