package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.ISAMRSproject.model.FishingInstructor;

public interface InstructorRepository extends JpaRepository<FishingInstructor,Integer>  {

}
