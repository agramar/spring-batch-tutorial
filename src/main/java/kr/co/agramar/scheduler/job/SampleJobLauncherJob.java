package kr.co.agramar.scheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@DisallowConcurrentExecution
public class SampleJobLauncherJob extends QuartzJobBean {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("key", LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                .toJobParameters());
    }
}
