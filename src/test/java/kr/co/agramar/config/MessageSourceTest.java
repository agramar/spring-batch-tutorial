package kr.co.agramar.config;

import kr.co.agramar.model.City;
import kr.co.agramar.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest("jasypt.encryptor.password=song")
public class MessageSourceTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void test_messageSource() {

        String helloMessageKr = messageSource.getMessage("hello", null, Locale.KOREAN);
        Assert.assertEquals("월드", helloMessageKr);
        log.info("helloMessageKr : {}", helloMessageKr);

        String helloMessageEn = messageSource.getMessage("hello", null, Locale.ENGLISH);
        Assert.assertEquals("world", helloMessageEn);
        log.info("helloMessageEn : {}", helloMessageEn);
    }

    @Test
    public void test_messageSource_agr() {

        String helloMessageKr = messageSource.getMessage("message", new Object[]{"1"}, Locale.KOREAN);
        log.info("helloMessageKr : {}", helloMessageKr);

        String helloMessageEn = messageSource.getMessage("message", new Object[]{"1"}, Locale.ENGLISH);
        log.info("helloMessageEn : {}", helloMessageEn);
    }
}
