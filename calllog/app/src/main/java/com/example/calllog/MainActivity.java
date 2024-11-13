package com.example.calllog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends ComponentActivity {
  private final int REQUEST_CODE_READ_CALL_LOG = 101;
  private RecyclerView recyclerView;
  private CallLogAdapter callLogAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    requestCallLogPermission();

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    loadCallLogs();
  }

  private void loadCallLogs() {
    new LoadCallLogsTask(this).execute();
  }

  private class LoadCallLogsTask extends AsyncTask<Void, Void, List<CallLogEntity>> {
    private Context context;

    public LoadCallLogsTask(Context context) {
      this.context = context;
    }

    @Override
    protected List<CallLogEntity> doInBackground(Void... voids) {
      AppDatabase db = DatabaseClient.getInstance(context);
      return db.callLogDao().getAllCallLogs();
    }

    @Override
    protected void onPostExecute(List<CallLogEntity> callLogs) {
      super.onPostExecute(callLogs);
      // Set up the adapter with the retrieved call logs
      callLogAdapter = new CallLogAdapter(callLogs);
      recyclerView.setAdapter(callLogAdapter);
    }
  }

  private void requestCallLogPermission() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, 1);
    }
  }
}

