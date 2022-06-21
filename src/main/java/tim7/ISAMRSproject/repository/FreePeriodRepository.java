package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tim7.ISAMRSproject.model.FreePeriod;

import java.util.List;

public interface FreePeriodRepository extends JpaRepository<FreePeriod, Integer> {
    @Query("select f from FreePeriod f where f.offer.id = ?1")
    List<FreePeriod> findByOffer_Id(Integer id);

}