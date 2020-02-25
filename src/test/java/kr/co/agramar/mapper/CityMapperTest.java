package kr.co.agramar.mapper;

import com.google.gson.Gson;
import kr.co.agramar.model.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
public class CityMapperTest {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private Gson gson;

    @Test
    @Transactional
    public void test_master() {
        City mujin = City.builder()
                .countryCode("KOR")
                .name("Mujin")
                .district("Chollabuk")
                .population(95472L)
                .build();

        cityMapper.save(mujin);

        Assert.assertNotNull(mujin.getId());

        log.info("mujin : {}", mujin.toJsonString());
    }

    @Test
    @Transactional(readOnly = true)
    public void test_slave() {
        List<City> cityList = cityMapper.findAll();
        log.info("cityList : {}", gson.toJson(cityList));
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

        cityMapper.save(mujin);
    }
}