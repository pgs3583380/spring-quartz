package com.vo;

import com.model.TimerJob;

public class TimerJobVo extends TimerJob {
    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
