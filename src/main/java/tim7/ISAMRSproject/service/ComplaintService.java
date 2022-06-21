package tim7.ISAMRSproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.model.Complaint;
import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.repository.ComplaintRepository;

import java.util.List;

@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ReservationService resService;
    
    public void addNewComplaint(Complaint c){
    	
        complaintRepository.save(c);
    }
    
    public void addNewComplaint(Complaint c, int reservationId){
    	Optional<Reservation> res = resService.getReservationById(reservationId);
    	c.setReservation(res.get());
    	c.setOffenderId(res.get().getOffer().getId());
        complaintRepository.save(c);
    }

    public Optional<Complaint> findByReservationAndFromOwner(int id){
        return complaintRepository.findByReservation_IdEqualsAndFormOwnerIsTrue(id);
    }

	public List<Complaint> findComplaintsOnWait() {
		return this.complaintRepository.findOnWait();
	}
	
	@Transactional
	public Optional<Complaint> getComplaintById(int id) {
		return this.complaintRepository.findById(id);
	}

	@Transactional
	public void save(Complaint complaint) {
		this.complaintRepository.save(complaint);
		
	}
}

