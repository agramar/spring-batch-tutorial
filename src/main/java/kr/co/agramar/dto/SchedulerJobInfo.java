package kr.co.agramar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchedulerJobInfo {
    private Long id;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private Long startDelay;
    private String cronExpression;
    private Long repeatInterval;
    private boolean cronJob;
    private String description;
}