package com.util;

import com.dao.TimerJobDao;
import com.model.TimerJob;
import com.util.CommonUtils;
import com.util.SchedulerUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务初始化
 */
@Component
public class ScheduleJobInit {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobInit.class);

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

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        try {
            // 查询所有正常状态的定时任务，并在容器启动后，启动任务
            List<TimerJob> jobs = timerJobDao.selectUsed();
            for (TimerJob record : jobs) {
                String name = record.getJobName();
                String group = record.getJobGroup();
                // 加载job类
                Class<? extends Job> clazz = null;
                try {
                    String className = record.getClassName();
                    if (CommonUtils.isEmpty(className)) {
                        logger.info("id:{}无对应类", record.getId());
                        break;
                    }
                    Class aClass = Class.forName(record.getClassName());
                    if (Job.class.isAssignableFrom(aClass)) {
                        clazz = (Class<? extends Job>) Class.forName(className);
                    } else {
                        logger.info("类{}未继承Job接口", record.getClassName());
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    logger.info("类{}未找到", record.getClassName());
                }
                CronTrigger cronTrigger = SchedulerUtils.getCronTrigger(scheduler, name, group);
                //不存在，创建一个
                if (cronTrigger == null) {
                    //生成jobDetail
                    JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(name, group).build();
                    //表达式调度构建器
                    CronScheduleBuilder cornSB = CronScheduleBuilder.cronSchedule(record.getCronExpression());
                    //生成触发器
                    CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cornSB).build();
                    //添加job
                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    //已存在，那么更新相应的定时设置
                    SchedulerUtils.updateScheduleJob(scheduler, record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}