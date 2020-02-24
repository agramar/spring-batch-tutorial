package kr.co.agramar.repository;

import kr.co.agramar.model.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=local", "jasypt.encryptor.password=song"})
@Transactional
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void test_slave() {
        List<City> cityList = cityRepository.findAll();
        log.info("cityList : {}", cityList);
    }

    @Test
    public void test_master() {
        cityRepository.save(City.builder()
                .countryCode("KOR")
                .name("Mujin")
                .district("Chollabuk")
                .population(95472L)
                .build());
    }

    @Test
    public void test_repository() {
        City city = City.builder()
                .countryCode("KOR")
                .name("Mujin")
                .district("Chollabuk")
                .population(95472L)
                .build();
        cityRepository.save(city);

        Optional<City> mujin = cityRepository.findById(city.getId());
        mujin.ifPresent(mj -> {
            log.info("mujin : {}", mujin);
            cityRepository.delete(mj);
        });
    }

}
