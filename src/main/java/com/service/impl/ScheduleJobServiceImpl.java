package com.service.impl;

import com.dao.TimerJobDao;
import com.model.TimerJob;
import com.service.ScheduleJobService;
import com.util.CommonUtils;
import com.util.Constants;
import com.util.SchedulerUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private TimerJobDao timerJobDao;


    private Scheduler scheduler;

    @Autowired
    public void setTimerJobDao(TimerJobDao timerJobDao) {
        this.timerJobDao = timerJobDao;
    }

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void insert(TimerJob timerJob) {
        timerJob.setId(CommonUtils.getUUID());
        timerJob.setJobStatus(Constants.RUN_STATUS_PAUSE);
        timerJob.setMethodName("execute");
        timerJobDao.insertSelective(timerJob);
    }

    @Override
    public void update(TimerJob timerJob) {
        timerJobDao.updateByPrimaryKeySelective(timerJob);
        SchedulerUtils.updateScheduleJob(scheduler, timerJob);
    }

    @Override
    public void runOnce(String scheduleJobId) {
        TimerJob timerJob = timerJobDao.selectByPrimaryKey(scheduleJobId);
        SchedulerUtils.runOne(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
    }

    @Override
    public void pauseJob(String scheduleJobId) {
        TimerJob timerJob = timerJobDao.selectByPrimaryKey(scheduleJobId);
        timerJob.setJobStatus(Constants.RUN_STATUS_PAUSE);
        timerJobDao.updateByPrimaryKeySelective(timerJob);
        SchedulerUtils.pauseJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
    }

    @Override
    public void resumeJob(String scheduleJobId) {
        TimerJob timerJob = timerJobDao.selectByPrimaryKey(scheduleJobId);
        timerJob.setJobStatus(Constants.RUN_STATUS_STARTED);
        timerJobDao.updateByPrimaryKeySelective(timerJob);
        SchedulerUtils.resumeJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
    }

    @Override
    public List<TimerJob> selectByCondition() {
        return timerJobDao.selectByCondition();
    }

    @Override
    public TimerJob selectJob(String id) {
        return timerJobDao.selectByPrimaryKey(id);
    }
}