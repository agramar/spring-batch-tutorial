package kr.co.agramar.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution // 스케쥴러 클러스터링 시에 다중실행 방지
public class SampleCronJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("===============> SampleCronJob Start");
        log.info("===============> SampleCronJob End");
    }
}
