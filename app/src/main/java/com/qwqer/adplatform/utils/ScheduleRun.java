package com.qwqer.adplatform.utils;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 计时器。
 * @author E
 */
public class ScheduleRun {

	private Timer timer = null;
	private int delayTime = 0;
	private boolean onPause = false;
	private int maxCount = Integer.MAX_VALUE;
	private int count = 0;
	
    public ScheduleRun(int delayTime) {
        this.delayTime = delayTime;
    }

    public void start(){
        if (null == timer){
            timer = new Timer();
            timer.schedule(new ScheduleRunTask(), 1000, delayTime * 1000);
        }
    }
	
    public void stop() {
        if (null != timer){
            timer.cancel();
            timer = null;
        }

        //重置
        count = 0;
        maxCount = Integer.MAX_VALUE;
        onPause = false;
    }
	
    private class ScheduleRunTask extends TimerTask {
        public void run() {
            if (onPause){
                return;
            }
            count ++;
        	onTimeRun();
        }
    }
    
    private void onTimeRun(){
        int leftCount = maxCount - count;
    	if (null != scheduleRunListener) {
    		scheduleRunListener.onTimeFlip(count , leftCount);
		}
		if (leftCount <= 0){
    	    stop();
        }
    }
    
    public void setScheduleRunListener(ScheduleRunListener scheduleRunListener) {
		this.scheduleRunListener = scheduleRunListener;
	}

	private ScheduleRunListener scheduleRunListener = null;
    
    public interface ScheduleRunListener {
        void onTimeFlip(int count, int leftCount);
    }

    public void pause() {
        this.onPause = true;
    }

    public void resume(){
        this.onPause = false;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
