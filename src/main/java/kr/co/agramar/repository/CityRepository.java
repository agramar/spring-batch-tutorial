package kr.co.agramar.repository;

import kr.co.agramar.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}