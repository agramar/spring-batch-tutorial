package kr.co.agramar.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@JobScope
@Configuration
@RequiredArgsConstructor
public class SampleJob {

    private final JobBuilderFactory jobs;

    /**
     * Spring also automatically run batch jobs configured.
     * To disable auto-run of jobs, you need to use spring.batch.job.enabled property in application.properties file.
     */
    @Bean
    public Job sampleBatchJob(@Qualifier("sampleBatchStep1") Step sampleBatchStep1, @Qualifier("sampleBatchStep2") Step sampleBatchStep2) {
        return jobs.get("sampleBatchJob")
                .start(sampleBatchStep1)
                .next(sampleBatchStep2)
                .build();
    }

}
