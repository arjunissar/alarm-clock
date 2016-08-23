package arjunissar.com.alarmclock;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Arjun on 22-08-2016.
 */

public class AlarmsDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_HOUR, DatabaseHelper.COLUMN_MIN };

    public AlarmsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Alarm createAlarm(String hour,String min) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_HOUR, hour);
        values.put(DatabaseHelper.COLUMN_MIN, min);
        long insertId = database.insert(DatabaseHelper.TABLE_ALARMS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARMS,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Alarm newAlarm = cursorToAlarm(cursor);
        cursor.close();
        return newAlarm;
    }

    public void deleteAlarm(Alarm alarm) {
        String id = alarm.getId();
        System.out.println("Alarm deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_ALARMS, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<Alarm>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Alarm alarm = cursorToAlarm(cursor);
            alarms.add(alarm);
            cursor.moveToNext();
        }
        cursor.close();
        return alarms;
    }

    private Alarm cursorToAlarm(Cursor cursor) {
        Alarm alarm = new Alarm();
        alarm.setId(cursor.getString(0));
        alarm.setHour(cursor.getString(1));
        alarm.setMin(cursor.getString(2));
        return alarm;
    }
}
