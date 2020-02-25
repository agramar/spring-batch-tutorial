package kr.co.agramar.mapper;

import kr.co.agramar.model.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {
    List<City> findAll();
    void save(City city);
}