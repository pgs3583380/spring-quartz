package com.service;

import com.model.TimerJob;

import java.util.List;

/**
 * @author pgs
 * @Date 2017-09-01
 */
public interface ScheduleJobService {
    /**
     * 初始化
     */
    void init();

    /**
     * 新增
     */
    void insert(TimerJob timerJob);

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     */
    void update(TimerJob timerJob);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
     */
    void runOnce(String scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
     */
    void pauseJob(String scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
     */
    void resumeJob(String scheduleJobId);

    List<TimerJob> selectByCondition();

    /**
     * 获取一个任务的信息
     */
    TimerJob selectJob(String id);

    /**
     * 删除一个任务
     *
     * @param id the schedule job id
     */
    void deleteJob(String id);
}