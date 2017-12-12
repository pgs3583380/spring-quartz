package com.service.impl;

import com.dao.TimerJobDao;
import com.service.TestJobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("TestJobService")
public class TestJobServiceImpl implements TestJobService {
    @Resource
    private TimerJobDao timerJobDao;

    @Override
    public void doWork() {
        timerJobDao.selectByPrimaryKey("22");
    }
}