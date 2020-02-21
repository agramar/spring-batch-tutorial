package kr.co.agramar.sample;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=local"})
public class SampleTest {
    @Test
    public void test_test() {
    }
}
