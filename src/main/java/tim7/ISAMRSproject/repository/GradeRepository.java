package tim7.ISAMRSproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tim7.ISAMRSproject.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Integer>{
	
	@Query("select r from Grade r where r.status = 0")
	public List<Grade> getOnWaitReviews();
}
