package tim7.ISAMRSproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findById(Long id);
}
