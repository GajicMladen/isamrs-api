package tim7.ISAMRSproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tim7.ISAMRSproject.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    Optional<Complaint> findByReservation_IdEqualsAndFormOwnerIsTrue(Integer id);

    @Query("select c from Complaint c where c.status = 0")
	List<Complaint> findOnWait();


}
