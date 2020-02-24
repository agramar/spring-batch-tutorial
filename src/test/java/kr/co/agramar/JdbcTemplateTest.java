package kr.co.agramar;

import kr.co.agramar.mapper.CityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=local", "jasypt.encryptor.password=song"})
@Transactional
public class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    @Test
    public void test_slave() {
    }

    @Test
    public void test_master() {
    }

    @Test(expected = JpaSystemException.class)
    @Transactional(readOnly = true)
    public void test_master_exception() {
    }
}
