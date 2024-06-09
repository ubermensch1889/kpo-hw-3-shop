package hse.kpo.hw.kpohw3shop.repository;

import hse.kpo.hw.kpohw3shop.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    Optional<Station> findById(int id);
}
