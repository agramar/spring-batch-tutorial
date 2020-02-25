package kr.co.agramar.dao;

import com.google.gson.Gson;
import kr.co.agramar.model.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=local", "jasypt.encryptor.password=song"})
@Transactional
public class CityDAOTest {

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private Gson gson;

    @Test
    @Transactional(readOnly = true)
    public void test_slave() {
        List<City> cityList = cityDAO.findAll();
        log.info("cityList : {}", cityList);
    }

    @Test
    @Transactional
    public void test_master() {
        City mujin = City.builder()
                .countryCode("KOR")
                .name("Mujin")
                .district("Chollabuk")
                .population(95472L)
                .build();

        cityDAO.save(mujin);
    }

    @Test(expected = TransientDataAccessResourceException.class)
    @Transactional(readOnly = true)
    public void test_master_exception() {
        City mujin = City.builder()
                .countryCode("KOR")
                .name("Mujin")
                .district("Chollabuk")
                .population(95472L)
                .build();

        cityDAO.save(mujin);
    }
}
