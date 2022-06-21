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

import tim7.ISAMRSproject.dto.GradeDTO;
import tim7.ISAMRSproject.model.ApprovalStatus;
import tim7.ISAMRSproject.model.Grade;
import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.GradeService;
import tim7.ISAMRSproject.service.ReservationService;
import tim7.ISAMRSproject.service.UserService;
import tim7.ISAMRSproject.utils.EmailServiceImpl;

@RestController
@RequestMapping(value = "api/grade")
public class GradeController {
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/getReviews")
	public ResponseEntity<?> getOnWaitReviews() {
		List<Grade> reviews = this.gradeService.getOnWaitReviews();
		List<GradeDTO> dtos = new ArrayList<GradeDTO>();
		for (Grade g : reviews) {
			GradeDTO dto = new GradeDTO();
			dto.setId(g.getId());
			dto.setReviewText(g.getRevision());
			dto.setGrade(g.getGrade());
			dtos.add(dto);
		}
		
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
	
	@PostMapping(value = "/refuse")
	@PreAuthorize("hasRole('ROLE_ADMIN')"+
			"|| hasRole('ROLE_SYSADMIN')")
	public ResponseEntity<Void> refuseReview(@RequestBody int id) {
		Optional<Grade> grade = this.gradeService.getGradeById(id);
		if (grade.isPresent()) {
			Grade g = grade.get();
			g.setStatus(ApprovalStatus.DENIED);
			this.gradeService.save(g);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/accept")
	@PreAuthorize("hasRole('ROLE_ADMIN')"+
			"|| hasRole('ROLE_SYSADMIN')")
	public ResponseEntity<Void> acceptReview(@RequestBody int id) {
		Optional<Grade> grade = this.gradeService.getGradeById(id);
		if (grade.isPresent()) {
			Grade g = grade.get();
			g.setStatus(ApprovalStatus.ACCEPT);
			this.gradeService.save(g);
			User serviceProvider = this.userService.findServiceProviderByOfferId(g.getReservation().getOffer().getId());			
			this.emailService.sendReviewAcceptedMail(serviceProvider, g.getRevision(), g.getGrade());
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	//Returns only accepted reviews
	@GetMapping(value = "/getReviewsByOfferId/{id}")
	public ResponseEntity<?> getReviewsByOfferId (@PathVariable int id) {
		List<Reservation> reservations = this.reservationService.getReservationsForOffer(id);
		List<GradeDTO> dtos = new ArrayList<GradeDTO>();
		for (Reservation r : reservations) {
			if (r.getGrade() != null) {
				if (r.getGrade().getStatus() == ApprovalStatus.ACCEPT)
				{
					GradeDTO dto = new GradeDTO();
					dto.setId(r.getGrade().getId());
					dto.setGrade(r.getGrade().getGrade());
					dto.setReviewText(r.getGrade().getRevision());
					dtos.add(dto);
				}
			}
		}
		
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}
