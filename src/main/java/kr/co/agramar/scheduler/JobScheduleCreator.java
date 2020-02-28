package kr.co.agramar.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component
public class JobScheduleCreator {

    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, boolean isDurable, ApplicationContext applicationContext, String jobName, String jobGroup, String description) {

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();

        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(isDurable);
        factoryBean.setApplicationContext(applicationContext);
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);
        factoryBean.setDescription(description);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobGroup + jobName, jobClass.getName());
        factoryBean.setJobDataAsMap(jobDataMap);

        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    public CronTrigger createCronTrigger(String triggerName, Date startTime, Long startDelay, String cronExpression, int misFireInstruction) {

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();

        factoryBean.setBeanName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setStartDelay(startDelay != null ? startDelay : 0L);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(misFireInstruction);

        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        return factoryBean.getObject();
    }

    public SimpleTrigger createSimpleTrigger(String triggerName, Date startTime, Long startDelay, Long repeatTime, int misFireInstruction) {

        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();

        factoryBean.setName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setStartDelay(startDelay != null ? startDelay : 0L);
        factoryBean.setRepeatInterval(repeatTime);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(misFireInstruction);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }
}
