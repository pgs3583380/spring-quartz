package com.service;

import com.model.TimerJob;
import com.vo.TimerJobVo;

import java.util.List;

/**
 * @author pgs 845486067@qq.com
 * @Date 2017-09-01
 */
public interface ScheduleJobService {
    /**
     * 初始化定时任务
     */
    void initScheduleJob();

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

    List<TimerJobVo> selectByCondition();

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