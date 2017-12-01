package com.service.impl;

import com.dao.TimerJobDao;
import com.util.CommonUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DisallowConcurrentExecution
public class TestJobService implements Job {
    private static final Logger logger = LoggerFactory.getLogger(TestJobService.class);
    @Autowired
    private TimerJobDao timerJobDao;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        logger.info("测试用例执行成功，时间:{}", CommonUtils.getCurrentTime());
        timerJobDao.selectByPrimaryKey("777");
    }
}