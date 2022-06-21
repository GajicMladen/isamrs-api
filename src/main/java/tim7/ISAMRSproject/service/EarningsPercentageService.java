package tim7.ISAMRSproject.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.model.EarningsPercentage;
import tim7.ISAMRSproject.repository.EarningsPercentageRepository;

@Service
public class EarningsPercentageService {
	
	@Autowired
	private EarningsPercentageRepository eariningsPercentageRepository;
	
	public EarningsPercentage getLastEntry() {
		return this.eariningsPercentageRepository.getLastEntry().get(0);
	}

	public void save(int percentage) {
		EarningsPercentage ep = new EarningsPercentage();
		ep.setPercentage(percentage);
		ep.setDateOfSetting(LocalDateTime.now());
		this.eariningsPercentageRepository.save(ep);
		
	}
}
