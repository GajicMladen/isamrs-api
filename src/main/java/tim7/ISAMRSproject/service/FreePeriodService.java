package tim7.ISAMRSproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.dto.FreePeriodDTO;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FreePeriodService {

    @Autowired
    private FreePeriodRepository freePeriodRepository;

    @Autowired
    private CottageRepository cottageRepository;
    @Autowired
    private BoatRepository boatRepository;
    
    @Autowired
    private AdventureRepository adventureRepository;


    public List<FreePeriod> getFreePeriodByOfferId(int id){
        return freePeriodRepository.findByOffer_Id(id);
    }

    public void addFreePeriod(LocalDateTime startDate,LocalDateTime endDate,Offer offer){


        freePeriodRepository.save(new FreePeriod(startDate,endDate,offer));


    }

    public void deleteFreePeriod(int id){

        freePeriodRepository.deleteById(id);
    }

	public boolean addFreePeriodAdventure(FreePeriodDTO freePeriodDTO) {
		
		LocalDateTime startDate = freePeriodDTO.getStartDate();
        LocalDateTime endDate = freePeriodDTO.getEndDate();
        Optional<Adventure> adventure = adventureRepository.findById(freePeriodDTO.getOfferId());
        if (adventure.isPresent()) {

        	freePeriodRepository.save(new FreePeriod(startDate, endDate, adventure.get()));
        	return true;
        }
        return false;
		
	}

    public boolean inFreePeriodsOfOffer(Offer offer,String startDateString,String endDateString){
        LocalDateTime startDate = convertDateString(startDateString);
        LocalDateTime endDate = convertDateString(endDateString);

        for (FreePeriod fp: freePeriodRepository.findByOffer_Id(offer.getId())) {
            if(startDate.isAfter(fp.getStartDateTime()) && endDate.isBefore(fp.getEndDateTime()))
                return true;
        }

        return false;
    }


    private LocalDateTime convertDateString(String s) {
        String[] tokens = s.split("-");
        LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
        return retVal;
    }

}
