package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.RegistrationRequest;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

}
