package com.service.impl;

import com.dao.TimerJobDao;
import com.model.TimerJob;
import com.service.ScheduleJobService;
import com.util.CommonUtils;
import com.util.Constants;
import com.util.SchedulerUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
    @PostConstruct
    public void init() {
        // 查询所有正常状态的定时任务，并在容器启动后，启动任务
        List<TimerJob> jobs = timerJobDao.selectUsed();
        for (TimerJob record : jobs) {
            String name = record.getJobName();
            String group = record.getJobGroup();
            CronTrigger cronTrigger = SchedulerUtils.getCronTrigger(scheduler, name, group);
            //不存在，创建一个
            if (cronTrigger == null) {
                SchedulerUtils.createScheduleJob(record, scheduler);
            } else {
                //已存在，那么更新相应的定时设置
                SchedulerUtils.updateScheduleJob(scheduler, record);
            }
        }
    }

    @Override
    public void insert(TimerJob timerJob) {
        timerJob.setId(CommonUtils.getUUID());
        timerJob.setJobStatus(Constants.RUN_STATUS_PAUSE);
        timerJob.setMethodName("execute");
        timerJobDao.insertSelective(timerJob);
        SchedulerUtils.createScheduleJob(timerJob, scheduler);
        SchedulerUtils.pauseJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
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

    @Override
    public void deleteJob(String id) {
        TimerJob timerJob = timerJobDao.selectByPrimaryKey(id);
        if (timerJob != null) {
            SchedulerUtils.deleteJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
            timerJobDao.deleteByPrimaryKey(id);
        }
    }
}