package com.dao;

import com.model.TimerJob;
import com.vo.TimerJobVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerJobDao {
    void deleteByPrimaryKey(String id);

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
    List<TimerJobVo> selectByCondition();

}