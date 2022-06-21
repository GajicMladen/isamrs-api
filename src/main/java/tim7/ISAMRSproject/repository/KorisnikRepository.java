package tim7.ISAMRSproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.User;


public interface KorisnikRepository extends JpaRepository<User, Long> {
	public Optional<User> findById(Long id);
	public Optional<User> findByEmail(String email);
}