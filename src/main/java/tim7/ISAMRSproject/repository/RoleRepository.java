package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
