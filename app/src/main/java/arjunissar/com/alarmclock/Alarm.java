package arjunissar.com.alarmclock;

/**
 * Created by Arjun on 22-08-2016.
 */
public class Alarm {
    private String mId;
    private String mHour;
    private String mMin;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String mHour) {
        this.mHour = mHour;
    }

    public String getMin() {
        return mMin;
    }

    public void setMin(String mMin) {
        this.mMin = mMin;
    }
}
