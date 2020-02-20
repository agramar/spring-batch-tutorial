package kr.co.agramar;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
class SpringBatchApplicationTests {

	@Test
	void contextLoads() {
	}

}
