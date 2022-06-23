package tim7.ISAMRSproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.dto.AdventureDTO;
import tim7.ISAMRSproject.dto.ChangePasswordDTO;
import tim7.ISAMRSproject.dto.DeletionRequestDTO;
import tim7.ISAMRSproject.dto.UserDTO;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.model.DeletionRequest.DeletionRequestStatus;
import tim7.ISAMRSproject.service.*;
import tim7.ISAMRSproject.utils.EmailServiceImpl;

@RestController
@RequestMapping(value = "adventure")
@CrossOrigin(origins = "https://go-fishing-ui.web.app")
public class AdventureController {

	@Autowired
	private AdventureService adventureService;
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationServiceOwner reservationServiceOwner;
	
	@GetMapping(value = "/get/{id}")
	public ResponseEntity<AdventureDTO> getAdventureById(@PathVariable Integer id){
		Optional<Adventure> adventure = adventureService.findById(id);
		if (adventure.isPresent()) {
			return new ResponseEntity<AdventureDTO>(new AdventureDTO(adventure.get()), HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/instructor/adventures/{id}")
	public ResponseEntity<List<AdventureDTO>> getInstructorAdventures(@PathVariable int id){
		
		List<Adventure> adventures = adventureService.getAdventuresByInstructorId(id);
		List<AdventureDTO> adventuresDTO = new ArrayList<AdventureDTO>();
		
	
		for (Adventure a : adventures) {
			if (!a.isDeleted()) {
				adventuresDTO.add(new AdventureDTO(a));
			}
		}
		
		return new ResponseEntity<>(adventuresDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<AdventureDTO> deleteAdventure(@PathVariable Integer id) {
		Optional<Adventure> adventure = adventureService.findById(id);
		
		if (adventure.isPresent()) {
			//if (!reservationService.AdventureHasReservations(id)) {
			adventureService.remove(id);
			return new ResponseEntity<AdventureDTO>(new AdventureDTO(adventure.get()), HttpStatus.OK);
			//}
			//return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		else {
			return new ResponseEntity<AdventureDTO>(new AdventureDTO(adventure.get()), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping()
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<Void> addAdventure(@RequestBody AdventureDTO a) {
		this.adventureService.addAdventure(a);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PutMapping(value = "/instructor")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<Void> updateInstructorData(@RequestBody UserDTO i) {
		this.adventureService.updateInstructorData(i);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PutMapping(value = "/edit")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<Void> editAdventure(@RequestBody AdventureDTO a) {
		try {
			this.adventureService.editAdventure(a);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/instructor/passwordChange/{id}")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<?> changeInstructorPassword(@PathVariable Integer id, @RequestBody ChangePasswordDTO changePasswordDTO) {
		if(!changePasswordDTO.validate())
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Pogrešni podaci!");
		
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			User changedUser = user.get();
			userService.changeUserPassword(changedUser, changePasswordDTO);		
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(changedUser);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nepostojeći korisnik!");
		
	}
	
	@PostMapping(value = "/instructor/delete/{id}")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<?> saveDeletionRequest(@PathVariable Integer id, @RequestBody DeletionRequestDTO deletionRequestDTO) {
		if (deletionRequestDTO.getDeletionReason().length() < 10) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Pogrešni podaci!");
		}
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			User deletionUser = user.get();
			DeletionRequest deletionRequest = new DeletionRequest();
			deletionRequest.setDeletionReason(deletionRequestDTO.getDeletionReason());
			deletionRequest.setRequestStatus(DeletionRequestStatus.PENDING);
			deletionRequest.setUser(deletionUser);
			deletionUser.setDeletionRequest(deletionRequest);
			userService.save(deletionUser);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletionUser);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nepostojeći korisnik!");
		}
	}
	
	@GetMapping(value = "/instructor/adventuresId/{id}")
	public ResponseEntity<List<Integer>> getInstructorAdventuresId(@PathVariable int id){
		
		List<Adventure> adventures = adventureService.getAdventuresByInstructorId(id);
		List<Integer> adventuresId = new ArrayList<Integer>();
		
	
		for (Adventure a : adventures) {
			if (!a.isDeleted()) {
				adventuresId.add(a.getId());
			}
		}
		
		return new ResponseEntity<>(adventuresId, HttpStatus.OK);
	}
	
	@PostMapping(value = "/addAction")
	@PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<?> addAction(@RequestBody ActionDTO actionDTO) {


		int instructorId = adventureService.getFishingInstructorByOfferId(actionDTO.getOfferId());
		for (Offer offer: adventureService.getAdventuresByInstructorId(instructorId) ) {
			if(reservationServiceOwner.isPeriodReserved(offer,actionDTO.getStartDate(),actionDTO.getEndDate()))
				return new ResponseEntity<>("Period je vec rezervisan.",HttpStatus.FORBIDDEN);
		}

		Reservation action = this.actionService.addAction(actionDTO);
		if (action != null) {
			List<Client> subscribers = clientService.getSubscribersForOffer(actionDTO.getOfferId());
            for(Client client: subscribers ) {
                try {
                    emailService.sendActionEmail(client, action);
                }
                catch (Exception e){
                    System.out.println("Exceprion on sending email to : "+client.getEmail());
                }
            }
			return new ResponseEntity<>(HttpStatus.OK);
		} 
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/getActionsFromIds/{ids}")
	public List<ActionDTO> getFromIds(@PathVariable String ids) {
		List<ActionDTO> actions = new ArrayList<ActionDTO>();
		String[] offerIds = ids.split(",");
		for (String i : offerIds) {
			int id = Integer.parseInt(i);
			List<Reservation> ac = this.actionService.getActionByOfferId(id);
			for (Reservation a: ac) {
				ActionDTO actionDTO = new ActionDTO();
				actionDTO.setEndDate(a.getEndDateTime());
				actionDTO.setStartDate(a.getStartDateTime());
				actionDTO.setTotalPrice(a.getTotalPrice());
				actionDTO.setOfferId(a.getOffer().getId());
				actionDTO.setId(a.getId());
				actions.add(actionDTO);
			}
		}
		return actions;
	}
	
	@GetMapping(value = "/getActionsFromId/{id}") 
	public List<ActionDTO> getActionsFromId(@PathVariable int id) {
		List<Reservation> actions = this.actionService.getActionByOfferId(id);
		List<ActionDTO> dtos = new ArrayList<ActionDTO>();
		for (Reservation a: actions) {
			System.out.println(a.getId());
			ActionDTO actionDTO = new ActionDTO();
			actionDTO.setEndDate(a.getEndDateTime());
			actionDTO.setStartDate(a.getStartDateTime());
			actionDTO.setTotalPrice(a.getTotalPrice());
			actionDTO.setOfferId(a.getOffer().getId());
			actionDTO.setId(a.getId());
			dtos.add(actionDTO);
			
		}
		
		return dtos;
	}
	
	@GetMapping(value = "/getAdventuresPreview")
    public ResponseEntity<?> getAdventuresPreview(){
    	List<AdventureDTO> dtos = new ArrayList<AdventureDTO>();
    	List<Adventure> adventures = adventureService.getAdventuresPreview();
		for(Adventure a: adventures) {
			AdventureDTO dto = new AdventureDTO();
			dto.setRating(a.getRating());
			dto.setPrice(a.getPrice());
			dto.setCapacity(a.getCapacity());
			dto.setName(a.getName());
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    	
    }
    
    @GetMapping(value = "/getAdventuresPreviewParam/{param}")
    public ResponseEntity<?> getBoatsPreviewParam(@PathVariable String param){
    	List<AdventureDTO> dtos = new ArrayList<AdventureDTO>();
    	List<Adventure> adventures = adventureService.getAdventuresPreviewParam(param);
		for(Adventure a: adventures) {
			AdventureDTO dto = new AdventureDTO();
			dto.setRating(a.getRating());
			dto.setPrice(a.getPrice());
			dto.setCapacity(a.getCapacity());
			dto.setName(a.getName());
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    
    }
}
