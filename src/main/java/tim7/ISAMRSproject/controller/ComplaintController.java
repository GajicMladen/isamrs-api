package tim7.ISAMRSproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.ComplaintDTO;
import tim7.ISAMRSproject.model.ApprovalStatus;
import tim7.ISAMRSproject.model.Complaint;
import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.ClientService;
import tim7.ISAMRSproject.service.ComplaintService;
import tim7.ISAMRSproject.service.ReservationService;
import tim7.ISAMRSproject.service.UserService;
import tim7.ISAMRSproject.utils.EmailServiceImpl;

@RestController
@RequestMapping(value = "api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private EmailServiceImpl emailService;
    
    @Autowired
    private UserService userService;

    @PostMapping(value = "/addNew")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')" +
            "|| hasRole('ROLE_BOAT_OWNER')" +
            "|| hasRole('ROLE_INSTRUCTOR')" +
            "|| hasRole('ROLE_USER')")
    public void addNewComplaint(@RequestBody ComplaintDTO complaintDTO){

        Complaint newOne = new Complaint(complaintDTO);
        Optional<Reservation> reservation = reservationService.getReservationById(complaintDTO.getReservationId());
        newOne.setReservation(reservation.get());

        complaintService.addNewComplaint(newOne);

    }

    @GetMapping(value = "/getComplaintFromOwner/{id}")
    public ComplaintDTO getComplaintFromOwner(@PathVariable int id){
        Optional<Complaint> ret = complaintService.findByReservationAndFromOwner(id);
        if(ret.isPresent())
            return  new ComplaintDTO(ret.get());
        return  null;
    }
    
    //Return only complaints with ApprovalStatus of ON_WAIT
    @GetMapping(value = "/getComplaints")
    public ResponseEntity<List<ComplaintDTO>> getComplaints() {
    	List<Complaint> complaints = this.complaintService.findComplaintsOnWait();
    	List<ComplaintDTO> dtos = new ArrayList<ComplaintDTO>();
    	for (Complaint c : complaints) {
    		ComplaintDTO dto = new ComplaintDTO(c);
    		dtos.add(dto);
    	}
    	
    	return new ResponseEntity<List<ComplaintDTO>>(dtos, HttpStatus.OK);
    }
    
    @PostMapping(value = "/refuseComplaint")
    @PreAuthorize("hasRole('ROLE_ADMIN')"+
            "|| hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> refuseComplaint(@RequestBody int id) {
    	Optional<Complaint> c = this.complaintService.getComplaintById(id);
    	if (c.isPresent()) {
    		Complaint complaint = c.get();
    		complaint.setStatus(ApprovalStatus.DENIED);
    		this.complaintService.save(complaint);
    	}
    	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping(value = "/sendResponse/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')"+
            "|| hasRole('ROLE_SYSADMIN')")
    public ResponseEntity<Void> sendResponse(@PathVariable int id, @RequestBody String response) {
    	Optional<Complaint> c = this.complaintService.getComplaintById(id);
    	if (c.isPresent()) {
    		Complaint complaint = c.get();
    		complaint.setStatus(ApprovalStatus.ACCEPT);
    		this.complaintService.save(complaint);
    		if (complaint.isPunishOffender()) {
    			this.clientService.addPenatlToClient(complaint.getOffenderId());
    			Optional<Reservation> r = this.reservationService.getReservationById(complaint.getReservation().getId());
    			if (r.isPresent()) {
    				User accuser =  this.userService.findServiceProviderByOfferId(r.get().getOffer().getId());
    				this.emailService.sendComplaintResponse(accuser, this.userService.findById(complaint.getOffenderId()).get(), response);
    			}
    		}
    		else {
    			this.emailService.sendComplaintResponse(complaint.getReservation().getClient(), this.userService.findById(complaint.getOffenderId()).get(), response);
    		}
    	}
    	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    
}
