package kr.co.agramar.dao;

import kr.co.agramar.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    List<City> findAll() {
        String sql = "SELECT * FROM world.city";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> City.builder()
                        .id(rs.getLong("ID"))
                        .name(rs.getString("Name"))
                        .countryCode(rs.getString("CountryCode"))
                        .district(rs.getString("District"))
                        .population(rs.getLong("Population"))
                        .build()
        );
    }

    void save(City city) {
        String sql = "        INSERT INTO\n" +
                "            world.city (\n" +
                "                Name,\n" +
                "                CountryCode,\n" +
                "                District,\n" +
                "                Population\n" +
                "            )\n" +
                "        VALUES (\n" +
                "                ?,\n" +
                "                ?,\n" +
                "                ?,\n" +
                "                ?\n" +
                "            )";

        jdbcTemplate.update(sql, city.getName(), city.getCountryCode(), city.getDistrict(), city.getPopulation());
    }
}
