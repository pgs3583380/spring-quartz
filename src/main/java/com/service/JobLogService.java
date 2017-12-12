package com.service;

import com.model.JobLog;

import java.util.List;

public interface JobLogService {
    int insertSelective(JobLog record);

    List<JobLog> selectByJobId(String jobId);
}
