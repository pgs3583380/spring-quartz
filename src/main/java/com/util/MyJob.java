package com.util;

import com.model.JobLog;
import com.model.TimerJob;
import com.service.JobLogService;
import com.service.ScheduleJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 扩展Job
 */
public abstract class MyJob implements Job {
    @Resource
    private ScheduleJobService scheduleJobService;
    @Resource
    private JobLogService jobLogService;
    private static final Logger logger = LoggerFactory.getLogger(MyJob.class);

    public abstract void before(JobExecutionContext context);

    public abstract void doWork(JobExecutionContext context);

    public abstract void after(JobExecutionContext context);

    @Override
    public void execute(JobExecutionContext context) {
        //开始时间
        String state = Constants.EXECUTE_STATE_SUCCESS;
        String startTime = CommonUtils.timeFormat(new Date(), Constants.timestampPattern);
        TimerJob timerJob = scheduleJobService.selectByJobKey(context.getTrigger().getJobKey());
        String name = context.getTrigger().getJobKey().getName();
        before(context);
        try {
            // 执行成功
            doWork(context);
        } catch (Exception e) {
            if (null == timerJob) {
                logger.info("未查询到任务,任务名:{}，时间:{}", name, CommonUtils.getCurrentTime());
            } else {
                scheduleJobService.pauseJobByJobKey(timerJob);
                state = Constants.EXECUTE_STATE_FAIL;
            }
        } finally {
            if (null == timerJob) {
                logger.info("未查询到任务,任务名:{}，时间:{}", name, CommonUtils.getCurrentTime());
            } else {
                //插入到历史表中
                String endTime = CommonUtils.timeFormat(new Date(), Constants.timestampPattern);
                JobLog jobLog = new JobLog();
                jobLog.setExecutionStartTime(startTime);
                jobLog.setExecutionEndTime(endTime);
                jobLog.setJobId(timerJob.getId());
                jobLog.setExecutionState(state);
                jobLogService.insertSelective(jobLog);
            }
        }
        after(context);
    }
}