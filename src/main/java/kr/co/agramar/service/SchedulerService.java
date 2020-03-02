package kr.co.agramar.service;

import kr.co.agramar.dto.SchedulerJobInfo;
import kr.co.agramar.scheduler.JobScheduleCreator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final ApplicationContext context;
    private final JobScheduleCreator scheduleCreator;

    private final List<SchedulerJobInfo> JOB_INFO_LIST = Arrays.asList(
            SchedulerJobInfo.builder()
                    .jobName("SampleJobLauncherJob")
                    .jobGroup("jobGroup")
                    .jobClass("kr.co.agramar.scheduler.job.SampleJobLauncherJob")
                    .cronExpression("00 05 19 * * ?")
                    .cronJob(true)
                    .description("Spring Batch Job Launcher Job Sample")
                    .build(),
            SchedulerJobInfo.builder()
                    .jobName("SampleSimpleJob")
                    .jobGroup("SimpleJobGroup")
                    .jobClass("kr.co.agramar.scheduler.job.SampleSimpleJob")
                    .repeatInterval(1000000L)
                    .cronJob(false)
                    .description("Simple Job Sample")
                    .build(),
            SchedulerJobInfo.builder()
                    .jobName("SampleCronJob")
                    .jobGroup("CronJobGroup")
                    .jobClass("kr.co.agramar.scheduler.job.SampleCronJob")
                    .cronExpression("0/30 * * * * ?")
                    .cronJob(true)
                    .description("Cron Job Sample")
                    .build());

    public void clearAndStartAllSchedulers() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.clear();
            JOB_INFO_LIST.forEach(jobInfo -> this.scheduleJob(jobInfo, scheduler));
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unused")
    public void startAllSchedulers() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JOB_INFO_LIST.forEach(jobInfo -> this.scheduleJob(jobInfo, scheduler));
    }

    @SuppressWarnings("unused")
    private void scheduleJob(SchedulerJobInfo jobInfo) {
        this.scheduleJob(jobInfo, null);
    }

    @SuppressWarnings("unused")
    public void clear() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.clear();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public void scheduleJob(SchedulerJobInfo jobInfo, Scheduler scheduler) {
        try {
            if (scheduler == null)
                scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob((Class<? extends Job>) Class.forName(jobInfo.getJobClass()))
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
                    .build();

            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob(
                        (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
                        false,
                        context,
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        jobInfo.getDescription());

                Trigger trigger;

                if (jobInfo.isCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {

                    trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getStartDelay(),
                            jobInfo.getCronExpression(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {

                    trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getStartDelay(),
                            jobInfo.getRepeatInterval(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }

                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                this.updateScheduleJob(jobInfo);
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found : {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateScheduleJob(SchedulerJobInfo jobInfo) {

        Trigger newTrigger;

        if (jobInfo.isCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {

            newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(),
                    new Date(),
                    jobInfo.getStartDelay(),
                    jobInfo.getCronExpression(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        } else {

            newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(),
                    new Date(),
                    jobInfo.getStartDelay(),
                    jobInfo.getRepeatInterval(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        }

        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unused")
    public boolean unScheduleJob(String jobName) {
        try {
            return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
        } catch (SchedulerException e) {
            log.error("Failed to un-schedule job : {}", jobName, e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job : {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job : {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job : {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean triggerJob(SchedulerJobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to trigger job : {}", jobInfo.getJobName(), e);
            return false;
        }
    }
}
