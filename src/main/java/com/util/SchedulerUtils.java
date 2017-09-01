package com.util;

import com.model.TimerJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerUtils {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerUtils.class);

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 恢复任务执行
     *
     * @param scheduler 调度器
     * @param jobName   任务名
     * @param groupName 组织名
     */
    public static void resumeJob(Scheduler scheduler, String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.info("定时任务恢复失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler the scheduler
     * @param jobName   the job name
     * @param jobGroup  the job group
     * @return cron trigger
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            logger.error("获取定时任务CronTrigger出现异常", e);
        }
        return null;
    }

    /**
     * 暂停任务执行
     *
     * @param scheduler 调度器
     * @param jobName   任务名
     * @param groupName 组织名
     */
    public static void pauseJob(Scheduler scheduler, String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);

        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            logger.info("定时任务暂停失败");
            e.printStackTrace();
        }
    }

    /**
     * 运行一次
     *
     * @param scheduler 调度器
     * @param jobName   任务名
     * @param groupName 组织名
     */
    public static void runOne(Scheduler scheduler, String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.info("运行一次失败");
        }
    }

    /**
     * 删除任务
     *
     * @param scheduler 调度器
     * @param jobName   任务名
     * @param groupName 组织名
     */
    public static void delete(Scheduler scheduler, String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.info("删除任务");
        }
    }

    /**
     * 更新定时任务
     *
     * @param scheduler the scheduler
     * @param timerJob  the schedule job
     */
    public static void updateScheduleJob(Scheduler scheduler, TimerJob timerJob) {
        updateScheduleJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup(),
                timerJob.getCronExpression());
    }

    /**
     * 更新定时任务
     *
     * @param scheduler      the scheduler
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     */
    private static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression) {
        try {
            TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败", e);
        }
    }
}