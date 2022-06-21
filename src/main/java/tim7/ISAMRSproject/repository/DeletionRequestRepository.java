package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.DeletionRequest;

public interface DeletionRequestRepository extends JpaRepository<DeletionRequest, Integer>  {

}
