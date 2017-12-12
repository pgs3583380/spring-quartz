package com.controller;

import com.model.JobLog;
import com.model.TimerJob;
import com.service.JobLogService;
import com.service.ScheduleJobService;
import com.util.CommonUtils;
import com.util.Constants;
import com.vo.TimerJobVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/schedule")
public class SchedulerController {
    private static Logger log = Logger.getLogger(SchedulerController.class);
    @Resource
    private ScheduleJobService scheduleJobService;
    @Resource
    private JobLogService jobLogService;

    @RequestMapping(value = "/selectByCondition", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectByCondition(TimerJob timerJob) {
        List<TimerJobVo> timerJobs = scheduleJobService.selectByCondition();
        Map<String, Object> map = new HashMap<>();
        map.put("aaData", timerJobs);
        return map;
    }

    /**
     * 查询一个任务
     *
     * @param id 任务id
     */
    @RequestMapping(value = "selectJob", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectJob(String id) {
        Map<String, Object> map = new HashMap<>();
        Integer flag = Constants.SEARCH_FAIL;
        if (CommonUtils.isNotEmpty(id)) {
            TimerJob job = scheduleJobService.selectJob(id);
            if (job != null) {
                map.put("job", job);
                flag = Constants.SEARCH_SUCCESS;
            }
        }
        map.put("flag", flag);
        return map;
    }

    /**
     * 暂停任务
     *
     * @param id 任务id
     */
    @RequestMapping(value = "pauseJob", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> pauseJob(String id) {
        Map<String, Object> map = new HashMap<>();
        scheduleJobService.pauseJob(id);
        map.put("flag", 1);
        return map;
    }

    /**
     * 启用任务
     *
     * @param id 任务id
     */
    @RequestMapping(value = "resumeJob", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resumeJob(String id) {
        Map<String, Object> map = new HashMap<>();
        scheduleJobService.resumeJob(id);
        map.put("flag", 1);
        return map;
    }

    /**
     * 启用一次
     *
     * @param id 任务id
     */
    @RequestMapping(value = "runOnce", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> runOnce(String id) {
        Map<String, Object> map = new HashMap<>();
        scheduleJobService.runOnce(id);
        map.put("flag", 1);
        return map;
    }

    @RequestMapping(value = "saveOrupdateJob", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveOrupdateJob(TimerJob timerJob) {
        Map<String, Object> map = new HashMap<>();
        if (timerJob != null) {
            if (CommonUtils.isNotEmpty(timerJob.getId())) {//更新Job
                scheduleJobService.update(timerJob);
            } else {
                scheduleJobService.insert(timerJob);//新增Job
            }
        }
        map.put("flag", 1);
        return map;
    }

    @RequestMapping(value = "deleteJob", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteJob(String id) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.isNotEmpty(id)) {
            scheduleJobService.deleteJob(id);
        }
        map.put("flag", 1);
        return map;
    }

    @RequestMapping(value = "/showLog", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> showLog(String id) {
        List<JobLog> jobLogs = jobLogService.selectByJobId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("aaData", jobLogs);
        return map;
    }
}