package com.service.impl;

import com.dao.TimerJobDao;
import com.model.TimerJob;
import com.service.ScheduleJobService;
import com.util.CommonUtils;
import com.util.Constants;
import com.util.SchedulerUtils;
import com.vo.TimerJobVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
    @Autowired
    private TimerJobDao timerJobDao;
    @Autowired
    private Scheduler scheduler;

    @Override
    @PostConstruct
    /**
     * 查询所有正常状态的定时任务，并在容器启动后，启动任务
     */
    public void initScheduleJob() {
        List<TimerJob> jobs = timerJobDao.selectUsed();
        for (TimerJob record : jobs) {
            String name = record.getJobName();
            String group = record.getJobGroup();
            CronTrigger cronTrigger = SchedulerUtils.getCronTrigger(scheduler, name, group);
            if (cronTrigger == null) {
                SchedulerUtils.createScheduleJob(record, scheduler);
            } else {
                SchedulerUtils.updateScheduleJob(scheduler, record);
            }
        }
    }

    @Override
    public void insert(TimerJob timerJob) {
        timerJob.setId(CommonUtils.getUUID());
        timerJob.setJobStatus(Constants.RUN_STATUS_PAUSE);
        timerJob.setMethodName(Constants.DEFAULT_METHOD_NAME);
        timerJob.setJobGroup(Constants.DEFAULT_GROUP_NAME);
        dealWithJobName(timerJob);
        timerJobDao.insertSelective(timerJob);
        SchedulerUtils.createScheduleJob(timerJob, scheduler);
        SchedulerUtils.pauseJob(scheduler, timerJob.getJobName(), timerJob.getJobGroup());
    }

    /**
     * //任务名生成规则--防止重名
     *
     * @param timerJob
     */
    private void dealWithJobName(TimerJob timerJob) {
        if (CommonUtils.isNotEmpty(timerJob.getClassName())) {
            String[] clazz = timerJob.getClassName().split("\\.");
            timerJob.setJobName(clazz[clazz.length - 1] + "Job" + CommonUtils.timeFormat(new Date(), Constants.simplifyTimestampPattern));
        }
    }

    @Override
    public void update(TimerJob timerJob) {
        timerJobDao.updateByPrimaryKeySelective(timerJob);
        TimerJob newJob = timerJobDao.selectByPrimaryKey(timerJob.getId());
        SchedulerUtils.updateScheduleJob(scheduler, newJob);
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
    public List<TimerJobVo> selectByCondition() {
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