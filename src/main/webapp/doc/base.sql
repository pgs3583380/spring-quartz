CREATE DATABASE quartz;
CREATE TABLE TIMERJOB(
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  job_name VARCHAR(120) NOT NULL ,
  job_group VARCHAR(20) NOT NULL ,
  cron_expression VARCHAR(20) NOT NULL ,
  job_desc VARCHAR(100) NOT NULL ,
  job_status VARCHAR(10) NOT NULL,
  class_name VARCHAR(100) NOT NULL,
  method_name VARCHAR(20) NOT NULL
);
CREATE TABLE job_log(
  id varchar(40) PRIMARY KEY NOT NULL COMMENT '主键',
  job_id VARCHAR(40) NOT NULL COMMENT '任务主键',
  execution_start_time VARCHAR(40) NOT NULL COMMENT '开始执行时间',
  execution_end_time VARCHAR(40) NOT NULL COMMENT '结束执行时间',
  execution_state VARCHAR(10) NOT NULL COMMENT '执行状态,（成功，失败）'
);