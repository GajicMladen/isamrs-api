package tim7.ISAMRSproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tim7.ISAMRSproject.model.EarningsPercentage;

public interface EarningsPercentageRepository  extends JpaRepository<EarningsPercentage, Integer> {
	@Query(value = "select * from earnings_percentage ep order by ep.id desc", nativeQuery = true)
	List<EarningsPercentage> getLastEntry();

}
