package lv.visma.consulting.usercountriesapi.db.repositories;

import lv.visma.consulting.usercountriesapi.db.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM countries c LEFT JOIN favorite_country fc ON c.id = fc.country_id WHERE fc.user_id = :userId")
    Set<CountryEntity> findAllByUserId(Long userId);
}
