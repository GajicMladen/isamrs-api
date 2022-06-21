package tim7.ISAMRSproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tim7.ISAMRSproject.model.Action;

public interface ActionRepository extends JpaRepository<Action, Integer> {

	@Query("select a from Action a where a.offer.id = ?1")
	List<Action> findAllByOfferId(int id);

}
