/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.bpmn.safejs.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicBoolean;

//@SuppressWarnings("all")
public class MonitorThread extends Thread {
    private final long maxCPUTime;

    private final AtomicBoolean stop;

    private final AtomicBoolean operationInterrupted;

    private Thread threadToMonitor;

    private Runnable onInvalid;

    private final AtomicBoolean cpuLimitExceeded;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void run() {
        try {
            final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long _id = this.threadToMonitor.getId();
            final long startCPUTime = bean.getThreadCpuTime(_id);
            while ((!this.stop.get())) {
                {
                    long _id_1 = this.threadToMonitor.getId();
                    final long threadCPUTime = bean.getThreadCpuTime(_id_1);
                    final long runtime = (threadCPUTime - startCPUTime);
                    if ((runtime > this.maxCPUTime)) {
                        logger.warn("Thread {} stopping with execution time {}", threadToMonitor.getId(), runtime);
                        this.cpuLimitExceeded.set(true);
                        this.stop.set(true);
                        this.onInvalid.run();
                        Thread.sleep(50);
                        boolean _get = this.operationInterrupted.get();
                        boolean _equals = (_get == false);
                        if (_equals) {
                            String _plus = (this + ": Thread hard shutdown!");
                            logger.warn(_plus);
                            this.threadToMonitor.stop();
                        }
                        return;
                    }
                    Thread.sleep(5);
                }
            }
        } catch (Throwable _e) {
            throw new RuntimeException(_e);
        }
    }

    public void stopMonitor() {
        this.stop.set(true);
    }

    public void setThreadToMonitor(final Thread t) {
        this.threadToMonitor = t;
    }

    public void setOnInvalidHandler(final Runnable r) {
        this.onInvalid = r;
    }

    public void notifyOperationInterrupted() {
        this.operationInterrupted.set(true);
    }

    public boolean isCPULimitExceeded() {
        return this.cpuLimitExceeded.get();
    }

    public boolean gracefullyInterrputed() {
        return this.operationInterrupted.get();
    }

    public MonitorThread(final long maxCPUTimne) {
        this.maxCPUTime = maxCPUTimne;
        AtomicBoolean _atomicBoolean = new AtomicBoolean(false);
        this.stop = _atomicBoolean;
        AtomicBoolean _atomicBoolean_1 = new AtomicBoolean(false);
        this.operationInterrupted = _atomicBoolean_1;
        AtomicBoolean _atomicBoolean_2 = new AtomicBoolean(false);
        this.cpuLimitExceeded = _atomicBoolean_2;
    }
}
