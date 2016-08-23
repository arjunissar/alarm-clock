package arjunissar.com.alarmclock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun on 22-08-2016.
 */
public class AlarmSingleton {

    private static AlarmSingleton singleton = new AlarmSingleton();
    private List<Alarm> mAlarms = new ArrayList<>();

    private AlarmSingleton() { }

    public static AlarmSingleton getInstance() {
        return singleton;
    }

    public List<Alarm> getAlarms() {
        return mAlarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.mAlarms = alarms;
    }
}