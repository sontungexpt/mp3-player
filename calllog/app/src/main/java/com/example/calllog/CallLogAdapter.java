package com.example.calllog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {
  private List<CallLogEntity> callLogs;

  public CallLogAdapter(List<CallLogEntity> callLogs) {
    this.callLogs = callLogs;
  }

  @NonNull
  @Override
  public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false);
    return new CallLogViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
    CallLogEntity callLog = callLogs.get(position);
    holder.numberTextView.setText("Số: " + callLog.getNumber());
    holder.typeTextView.setText("Loại: " + callLog.getType());
    holder.dateTextView.setText("Thời gian: " + callLog.getDate());
    holder.durationTextView.setText("Thời lượng: " + callLog.getDuration() + " giây");
  }

  @Override
  public int getItemCount() {
    return callLogs.size();
  }

  public static class CallLogViewHolder extends RecyclerView.ViewHolder {
    TextView numberTextView, typeTextView, dateTextView, durationTextView;

    public CallLogViewHolder(@NonNull View itemView) {
      super(itemView);
      numberTextView = itemView.findViewById(R.id.text_number);
      typeTextView = itemView.findViewById(R.id.text_type);
      dateTextView = itemView.findViewById(R.id.text_date);
      durationTextView = itemView.findViewById(R.id.text_duration);
    }
  }
}
