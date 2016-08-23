package arjunissar.com.alarmclock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arjun on 22-08-2016.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {

    List<Alarm> mAlarms;
    Context context;

    public AlarmAdapter(List<Alarm> alarms, Context c) {
        this.mAlarms = alarms;
        this.context = c;
    }

    @Override
    public AlarmAdapter.AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_layout, parent, false);
        AlarmHolder alarmHolder = new AlarmHolder(view, context);
        return alarmHolder;
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.AlarmHolder holder, int position) {
        holder.root.setTag(mAlarms.get(position));
        String alarm = mAlarms.get(position).getHour() + ":" + mAlarms.get(position).getMin();
        holder.mAlarm.setText(alarm);
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class AlarmHolder extends RecyclerView.ViewHolder {
        View root;
        TextView mAlarm;

        public AlarmHolder(final View itemView, final Context context) {
            super(itemView);
            root = itemView;
//            root.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
            mAlarm = (TextView) root.findViewById(R.id.alarm_text);
        }
    }
}
