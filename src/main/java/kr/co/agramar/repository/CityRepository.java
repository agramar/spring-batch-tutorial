package kr.co.agramar.repository;

import kr.co.agramar.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}