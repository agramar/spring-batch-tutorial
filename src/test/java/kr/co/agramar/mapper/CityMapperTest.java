package kr.co.agramar.mapper;

import kr.co.agramar.model.City;
import kr.co.agramar.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=local", "jasypt.encryptor.password=song"})
@Transactional
public class CityMapperTest {

    @Autowired
    private CityMapper cityMapper;

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
