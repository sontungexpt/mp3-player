package com.example.calllog;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class SaveCallLogsTask extends AsyncTask<Void, Void, Void> {
    private Context context;

    public SaveCallLogsTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<CallLogEntity> callLogs = CallLogHelper.getCallLogs(context);
        AppDatabase db = DatabaseClient.getInstance(context);
        for (CallLogEntity callLog : callLogs) {
            db.callLogDao().insert(callLog);
        }
        return null;
    }
}
