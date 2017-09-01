package com.model;

import java.io.Serializable;

public class TimerJob implements Serializable {
    private String id;
    /**
     * 任务名
     */
    private String jobName;
    /**
     * 机构
     */
    private String jobGroup;
    /**
     * 时间表达式
     */
    private String cronExpression;
    /**
     * 类名
     */
    private String className;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 任务状态，默认为PAUSED 启用RUNNING
     */
    private String jobStatus;
    /**
     * 调用的方法名
     */
    private String methodName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}