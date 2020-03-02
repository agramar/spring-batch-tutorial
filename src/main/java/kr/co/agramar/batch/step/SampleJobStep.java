package kr.co.agramar.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@StepScope
@Configuration
@RequiredArgsConstructor
public class SampleJobStep {

    private final StepBuilderFactory steps;

    @Bean
    protected Step sampleBatchStep1() {
        return steps.get("sampleBatchStep1")
                .tasklet((contribution, chunkContext) -> null)
                .build();
    }

    @Bean
    protected Step sampleBatchStep2() {
        return steps.get("sampleBatchStep2")
                .tasklet((contribution, chunkContext) -> null)
                .build();
    }
}
