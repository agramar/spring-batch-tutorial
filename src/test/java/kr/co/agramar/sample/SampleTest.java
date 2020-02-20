package kr.co.agramar.sample;

import kr.co.agramar.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest
public class SampleTest {

    @Autowired
    Configuration configuration;

    @Test
    public void test_test() {
        System.out.println(configuration.toString());
    }
}
