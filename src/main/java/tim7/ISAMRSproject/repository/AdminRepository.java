package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
	
	@Query(value = "select a.is_first_login from Admin a where a.id = ?1", nativeQuery = true)
	boolean getFirstLoginById(int id);

	@Transactional
	@Modifying
	@Query(value = "update Admin a set is_first_login = false where a.id = ?1", nativeQuery = true)
	void changeFirstLoginById(int id);

}
