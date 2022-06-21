package tim7.ISAMRSproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.model.ReservationStatus;
import tim7.ISAMRSproject.repository.AdventureRepository;
import tim7.ISAMRSproject.repository.ReservationRepository;

@Service
public class ActionService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private AdventureRepository adventureRepository;

	public Reservation addAction(ActionDTO actionDTO) {
		if (checkDate(actionDTO)) {
			Reservation res = new Reservation();
			res.setStartDateTime(actionDTO.getStartDate());
			res.setEndDateTime(actionDTO.getEndDate());
			res.setTotalPrice(actionDTO.getTotalPrice());
			res.setStatus(ReservationStatus.FOR_ACTION);
			Optional<Adventure> adventure = adventureRepository.findById(actionDTO.getOfferId());
	        if (adventure.isPresent()) {
	        	res.setOffer(adventure.get());
	        	return reservationRepository.save(res);	        	
	        }
	        return null;
		}
		else {
			return null;
		}
	}

	private boolean checkDate(ActionDTO actionDTO) {
		return true;
	}

	public List<Reservation> getActionByOfferId(int id) { 
		List<Reservation> ls = this.reservationRepository.findByOffer_IdEquals(id);
		List<Reservation> retVal = new ArrayList<Reservation>();
		for(Reservation r: ls) {
			if (r.getStatus() == ReservationStatus.FOR_ACTION)
				retVal.add(r);
		}
		return retVal;
	}
}
