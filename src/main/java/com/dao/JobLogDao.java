package com.dao;

import com.model.JobLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobLogDao {
    int insertSelective(JobLog record);

    List<JobLog> selectByJobId(String jobId);
}