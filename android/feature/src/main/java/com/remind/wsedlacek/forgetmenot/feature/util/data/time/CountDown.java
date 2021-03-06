package com.remind.wsedlacek.forgetmenot.feature.util.data.time;

import com.remind.wsedlacek.forgetmenot.feature.backend.BackgroundManager;
import com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import java.util.Calendar;

import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.convertStringToDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctToCurrentDate;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.correctUTC;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectDateFormat;
import static com.remind.wsedlacek.forgetmenot.feature.util.TimeCorrection.getCorrectTimeFormat;

public class CountDown extends MonitoredVariable<Calendar> {
    private String TAG = "CountDown";
    private String mCountDownText;
    private MonitoredVariable<Boolean> mPastTimmer;
    private MonitoredVariable<String> mTimeData;
    private MonitoredVariable<String> mDateData;
    private MonitoredVariable<String> mFreqData;
    private MonitoredVariable<Integer>[] mBackground;

    public CountDown(MonitoredVariable<String> tTimeData, MonitoredVariable<String> tDateData, MonitoredVariable<String> tFreqData) throws Exception {
        this(tTimeData, tDateData, tFreqData, null,null);
    }
    public CountDown(MonitoredVariable<String> tTimeData, MonitoredVariable<String> tDateData, MonitoredVariable<String> tFreqData, MonitoredVariable<Integer>[] tBackground) throws Exception {
        this(tTimeData, tDateData, tFreqData, tBackground, null);
    }
    public CountDown(MonitoredVariable<String> tTimeData, MonitoredVariable<String> tDateData, MonitoredVariable<String> tFreqData, MonitoredVariable<Integer>[] tBackground, ChangeListener tListener) throws Exception {
        super(tTimeData.get() == null ? Calendar.getInstance() : convertStringToDate(tTimeData.get()), tListener);
        mTimeData = tTimeData;
        mDateData = tDateData;
        mFreqData = tFreqData;
        mBackground = tBackground;
        mPastTimmer = new MonitoredVariable<>(false);
        mTimeData.setListener(new ChangeListener() {
            @Override
            public void onChange() {
                notifyChange();
            }
        });
    }

    public void updateCountDownText(Calendar tCurrentTime) {
        Debug.Log(TAG, "Updating Countdown Text...");
        Calendar tDate = calcCountDown(tCurrentTime);

        int tHour = tDate.get(Calendar.HOUR_OF_DAY);
        int tMin = tDate.get(Calendar.MINUTE);

        String tText = mPastTimmer.get() ? "LATE: " : "in ";
        tText += tHour != 0 ? tHour + "h " : "";
        tText += tHour != 0 || tMin != 0 ? tMin + "m" : "";
        tText += tHour == 0 && tMin == 0 ? "NOW!!" : "";
        mCountDownText = tText;
    }
    private Calendar calcCountDown(Calendar tCurrentTime) {
        Debug.Log(TAG, "Calculating Countdown...");

        Debug.Log(TAG, "Comparing Current Time and Countdown Timer");
        long diff =  mData.getTimeInMillis() - tCurrentTime.getTimeInMillis();
        Debug.Log(TAG, "Time Difference: " + diff + "ms");
        if (Math.abs(diff) != diff) {
            diff *= -1;
            mPastTimmer.set(true);
        }
        Debug.Log(TAG, "Time in past: " + mPastTimmer);

        Calendar tReturn = Calendar.getInstance();
        tReturn.setTimeInMillis(diff);

        Debug.Log(TAG, "Correcting Timezone and setting to current date...");
        correctUTC(tReturn);
        correctToCurrentDate(tReturn);

        float tPercent = (float)diff / (float)TimeCorrection.stringToFreq("Hourly");
        if (tPercent > 1f) tPercent = 1f;
        if (tPercent < 0f) tPercent = 0f;
        if (mBackground != null) BackgroundManager.updateBackground(tPercent, mPastTimmer.get(), mBackground, true);

        return tReturn;
    }

    public void nextFreqency() {
        if (mPastTimmer.get()) {
            while (Calendar.getInstance().getTimeInMillis() > mData.getTimeInMillis()) {
                long lFreq = TimeCorrection.stringToFreq(mFreqData.get());
                Debug.Log(TAG, "FREQENCY: "+ mFreqData.get());
                Debug.Log(TAG, "CURRENT MS: "+ Calendar.getInstance().getTimeInMillis());
                Debug.Log(TAG, "MS OF DEADLINE: "+ mData.getTimeInMillis());
                Debug.Log(TAG, "MS OF FREQ: "+ lFreq);
                mData.setTimeInMillis(mData.getTimeInMillis() + lFreq);
                Debug.Log(TAG, "NEW MS: "+ mData.getTimeInMillis());
            }

            Debug.Log(TAG, "OLD TIME: "+ mTimeData.get());
            String tTime = getCorrectTimeFormat(mData.get(Calendar.HOUR_OF_DAY), mData.get(Calendar.MINUTE));
            String tDate = getCorrectDateFormat(mData.get(Calendar.YEAR), mData.get(Calendar.MONTH), mData.get(Calendar.DAY_OF_MONTH));
            Debug.Log(TAG, "NEW TIME: "+ tTime);
            mPastTimmer.set(false);
            mDateData.set(tDate);
            mTimeData.set(tTime);
        }
    }

    public boolean past() {
        return mPastTimmer.get();
    }
    public int get(int tCalendarEnum) {
        return mData.get(tCalendarEnum);
    }
    public String getText() {
        return mCountDownText;
    }
    public long getTimeInMillis() {
        return mData.getTimeInMillis();
    }
    public void setTimeInMillis(long tTimeInMillis) {
        mData.setTimeInMillis(tTimeInMillis);
    }
    public void notifyChange() {
        updateCountDownText(Calendar.getInstance());
        super.notifyChange();
    }
}
