package com.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 扩展Job
 */
public abstract class MyJob implements Job {
    public abstract void before(JobExecutionContext context);

    public abstract void doWork(JobExecutionContext context);

    public abstract void after(JobExecutionContext context);

    @Override
    public void execute(JobExecutionContext context) {
        before(context);
        doWork(context);
        after(context);
    }
}