package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.LoyaltyDefinition;

public interface LoyaltyRepository extends JpaRepository<LoyaltyDefinition,Integer> {

}
