package com.example.pbenavalli.campitempo.ui.base;

/**
 * Created by pbenavalli on 5/16/17.
 */

public class BaseTest {

    public void doWait() {
        this.doWait(2000L);
    }

    public void doWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
            throw new RuntimeException("Could not sleep.", var4);
        }
    }
}
