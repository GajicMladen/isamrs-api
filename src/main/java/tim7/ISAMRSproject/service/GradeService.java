package tim7.ISAMRSproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.model.Grade;
import tim7.ISAMRSproject.repository.GradeRepository;

@Service
@Transactional
public class GradeService {
	
	@Autowired
	private GradeRepository gradeRepository;
	
	public List<Grade> getOnWaitReviews() {
		return this.gradeRepository.getOnWaitReviews();
	}
	
	@Transactional
	public Optional<Grade> getGradeById(int id) {
		return this.gradeRepository.findById(id);
	}
	
	@Transactional
	public void save(Grade grade) {
		this.gradeRepository.save(grade);
	}
	
}
