package com.service.impl;

import com.dao.JobLogDao;
import com.model.JobLog;
import com.service.JobLogService;
import com.util.CommonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobLogServiceImpl implements JobLogService {
    @Resource
    private JobLogDao jobLogDao;

    @Override
    public int insertSelective(JobLog record) {
        record.setId(CommonUtils.getUUID());
        return jobLogDao.insertSelective(record);
    }

    @Override
    public List<JobLog> selectByJobId(String jobId) {
        return jobLogDao.selectByJobId(jobId);
    }
}