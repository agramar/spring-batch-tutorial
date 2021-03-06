package kr.co.agramar;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Slf4j
@EnableBatchProcessing
@EnableEncryptableProperties
@EnableConfigurationProperties
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("application running in UTC timezone : {}", ZonedDateTime.now());
    }

    @PreDestroy
    public void preDestroy() {
        log.info("application shutting down in UTC timezone : {}", ZonedDateTime.now());
    }
}