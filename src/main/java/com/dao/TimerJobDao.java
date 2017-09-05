package com.dao;

import com.model.TimerJob;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerJobDao {
    void insertSelective(TimerJob record);

    TimerJob selectByPrimaryKey(String id);

    void updateByPrimaryKeySelective(TimerJob record);

    /**
     * 获取启用的任务
     *
     * @return
     */
    List<TimerJob> selectUsed();

    /**
     * 根据条件获取任务信息
     */
    List<TimerJob> selectByCondition();

    /**
     * 删除任务
     *
     * @param id schedule job id
     */
    void deleteByPrimaryKey(String id);

}