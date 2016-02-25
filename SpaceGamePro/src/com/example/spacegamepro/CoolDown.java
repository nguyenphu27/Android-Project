package com.example.spacegamepro;

import java.util.Timer;
import java.util.TimerTask;

public class CoolDown {
	private boolean valid;
    private Timer timer;
    private long delay = 300;
    private static CoolDown instance = null;
 
    public static CoolDown sharedCoolDown() {
        if (instance == null) {
            instance = new CoolDown();
        }
        return instance;
    }
 
    private CoolDown() {
        timer = new Timer();
        valid = true;
    }
 
    public boolean checkValidity() {
        if (valid) {
            valid = false;
            timer.schedule(new Task(), delay);
            return true;
        }
        return false;
    }
 
    class Task extends TimerTask {
        public void run() {
            valid = true;
        }
    }
}
