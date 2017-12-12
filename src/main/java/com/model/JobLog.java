package com.model;

import java.io.Serializable;

public class JobLog implements Serializable{
    private String id;

    private String jobId;

    private String executionStartTime;

    private String executionEndTime;

    private String executionState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(String executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public String getExecutionEndTime() {
        return executionEndTime;
    }

    public void setExecutionEndTime(String executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    public String getExecutionState() {
        return executionState;
    }

    public void setExecutionState(String executionState) {
        this.executionState = executionState;
    }
}