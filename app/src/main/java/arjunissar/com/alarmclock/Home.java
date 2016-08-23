package arjunissar.com.alarmclock;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Home extends Activity {

    private int mHour, mMinute;
    private SharedPreferences mSharedPreferences;
    private AlarmsDataSource mDatasource;
    private AlarmAdapter mAlarmAdapter;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    private static Home mInstance;
    private static final int PERMISSION_REQUEST_CODE = 10;

    public static Home instance() {
        return mInstance;
    }

    @Override
    public void onStart() {
        super.onStart();
        mInstance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//        mDatasource = new AlarmsDataSource(this);
//        mDatasource.open();

//        List<Alarm> values = mDatasource.getAllAlarms();

//        mSharedPreferences = getSharedPreferences("alarmSharedPreferences",MODE_PRIVATE);
//        values = (List) mSharedPreferences.getStringSet("alarms", new ArraySet<String>());

        List values = new ArrayList();
        for(int i=0; i<10; i++) {
            Alarm alarm = new Alarm();
            alarm.setHour(String.valueOf(i));
            alarm.setMin(String.valueOf(i));
            values.add(alarm);
        }

        if (values != null) {
            mAlarmAdapter = new AlarmAdapter(values, this);
        }
        recyclerView.setAdapter(mAlarmAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkPermissions() {
        if ((ContextCompat.checkSelfPermission(Home.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(Home.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            openTimePicker();
        } else {
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public void openTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(Home.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setAlarm(hourOfDay, minute);
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void setAlarm(int hour, int min) {

        String uniqueID = UUID.randomUUID().toString();
        Alarm alarm = new Alarm();
        alarm.setId(uniqueID);
        alarm.setHour(String.valueOf(hour));
        alarm.setMin(String.valueOf(min));

        List<Alarm> alarms = AlarmSingleton.getInstance().getAlarms();
        if (alarms!=null) {
            alarms.add(alarm);
        } else {
            alarms = new ArrayList<>();
            alarms.add(alarm);
        }

        Intent myIntent = new Intent(Home.this, AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(Home.this, 0, myIntent, 0);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND, 0);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), mPendingIntent);
//        Log.d("MyActivity", "Alarm On for " + hour + ":" + min);
    }

    private void cancelAlarm() {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPendingIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openTimePicker();
                } else {
                    Toast.makeText(this, "Can set alarm only if permission granted!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
