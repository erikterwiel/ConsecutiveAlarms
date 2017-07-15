package erikterwiel.consecutivealarmsv20;

/**
 * Created by Erik on 6/22/2017.
 */

// Acts as a package to return two strings to display time
public class Time {
    private String mTime;
    private String mMorning;

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getMorning() {
        return mMorning;
    }

    public void setMorning(String morning) {
        mMorning = morning;
    }
}
