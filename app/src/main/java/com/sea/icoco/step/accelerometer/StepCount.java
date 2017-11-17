package com.sea.icoco.step.accelerometer;

import android.util.Log;

import com.sea.icoco.Control.DataControler;
import com.sea.icoco.MainActivity;

/**
 * Created by dylan on 16/9/27.
 */

/*
* 根据StepDetector传入的步点"数"步子
* */
public class StepCount implements StepCountListener {

    String tag = "StepCount";
    private int count = 0;
    private int mCount = 0;
    private StepValuePassListener mStepValuePassListener;
    private long timeOfLastPeak = 0;
    private long timeOfThisPeak = 0;
    private StepDetector stepDetector;
    DataControler dataControler = MainActivity.dataControler;
    public StepCount() {
        stepDetector = new StepDetector();
        stepDetector.initListener(this);
    }
    public StepDetector getStepDetector(){
        return stepDetector;
    }

    /*
        * 连续走十步才会开始计步
        * 连续走了9步以下,停留超过3秒,则计数清空
        * */
    @Override
    public void countStep() {
        Log.d(tag,"count="+String.valueOf(this.count));
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        if (this.timeOfThisPeak - this.timeOfLastPeak <= 3000L && (dataControler.gpsData.isKeepGoing() && dataControler.gpsData.getSpeed() <= 20.0)|| !dataControler.gpsData.isSpeedLimit()) {
            if (this.count < 0) {
                this.count++;
            } else if (this.count == 0) {
                this.count++;
                this.mCount += this.count;
                notifyListener();
            } else {
                this.mCount++;
                notifyListener();
            }
        } else {//超时
            this.count = 1;//为1,不是0
        }

    }

    public void initListener(StepValuePassListener listener) {
        this.mStepValuePassListener = listener;
    }

    public void notifyListener() {
        if (this.mStepValuePassListener != null)
            this.mStepValuePassListener.stepChanged(this.mCount);
    }


    public void setSteps(int initValue) {
        this.mCount = initValue;
        this.count = 0;
        timeOfLastPeak = 0;
        timeOfThisPeak = 0;
        notifyListener();
    }
}
