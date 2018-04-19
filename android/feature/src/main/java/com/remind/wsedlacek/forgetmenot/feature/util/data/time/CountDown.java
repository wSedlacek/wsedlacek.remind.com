package com.remind.wsedlacek.forgetmenot.feature.util.data.time;

import com.remind.wsedlacek.forgetmenot.feature.backend.DataManager;
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
    private boolean mPastTimmer;
    private MonitoredVariable<String> mTimeData;
    private MonitoredVariable<String> mDateData;
    private MonitoredVariable<String> mFreqData;

    public CountDown(MonitoredVariable<String> tTimeData, MonitoredVariable<String> tDateData, MonitoredVariable<String> tFreqData) throws Exception {
        this(tTimeData, tDateData, tFreqData, null);
    }
    public CountDown(MonitoredVariable<String> tTimeData, MonitoredVariable<String> tDateData, MonitoredVariable<String> tFreqData, ChangeListener tListener) throws Exception {
        super(convertStringToDate(tTimeData.get()), tListener);
        mTimeData = tTimeData;
        mDateData = tDateData;
        mFreqData = tFreqData;
    }

    public void updateCountDownText(Calendar tCurrentTime) {
        Debug.Log(TAG, "Updating Countdown Text...");
        Calendar tDate = calcCountDown(tCurrentTime);

        int tHour = tDate.get(Calendar.HOUR_OF_DAY);
        int tMin = tDate.get(Calendar.MINUTE);

        String tText = mPastTimmer ? "LATE: " : "in ";
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
            mPastTimmer = true;
        }
        Debug.Log(TAG, "Time in past: " + mPastTimmer);

        Calendar tReturn = Calendar.getInstance();
        tReturn.setTimeInMillis(diff);

        Debug.Log(TAG, "Correcting Timezone and setting to current date...");
        correctUTC(tReturn);
        correctToCurrentDate(tReturn);
        return tReturn;
    }

    public void nextFreqency() {
        if (mPastTimmer) {
            String tFreq = mFreqData.get();
            while (Calendar.getInstance().getTimeInMillis() > mData.getTimeInMillis()) {
                long lFreq = 0;
                if (tFreq == null) tFreq = "Daily";
                switch (tFreq) {
                    case "Hourly":
                        lFreq = 3600000;
                        break;
                    case "Daily":
                        lFreq = 86400000;
                        break;
                    case "weekly":
                        lFreq = 604800000;
                        break;
                }

                Debug.Log(TAG, "FREQENCY: "+ tFreq);
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
            mTimeData.set(tTime);
            mDateData.set(tDate);
            mPastTimmer = false;
            notifyChange();
        }
    }

    public boolean past() {
        return mPastTimmer;
    }
    public void set (Calendar tData) {
        mData = tData;
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
