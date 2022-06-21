package tim7.ISAMRSproject.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import tim7.ISAMRSproject.dto.ChangePasswordDTO;
import tim7.ISAMRSproject.dto.DeletionRequestDTO;
import tim7.ISAMRSproject.dto.UserDTO;
import tim7.ISAMRSproject.dto.UserRegisterDTO;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.model.DeletionRequest;
import tim7.ISAMRSproject.model.DeletionRequest.DeletionRequestStatus;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.AdventureService;
import tim7.ISAMRSproject.service.BoatService;
import tim7.ISAMRSproject.service.CottageService;
import tim7.ISAMRSproject.service.LoyaltyService;
import tim7.ISAMRSproject.service.UserService;


@RestController
@RequestMapping(value = "/api/users", produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CottageService cottageService;
	@Autowired
	private BoatService boatService;

	@Autowired
	private AdventureService adventureService;

	@Autowired
	private LoyaltyService loyaltyService;
	
	@GetMapping(value = "/all")
	public ResponseEntity<List<UserDTO>> getAllKorisnici() {

		List<User> users = userService.findAll();

		List<UserDTO> userDTOS = new ArrayList<>();
		for (User u : users) {
			userDTOS.add(new UserDTO(u));
		}

		return new ResponseEntity<>(userDTOS, HttpStatus.OK);
	}

	@GetMapping(value = "/getUserRole")
	public ResponseEntity<?> getUserRole(Principal user){
		User u = this.userService.findByEmail(user.getName());
		if (u == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		String ret = "{\"role\":\"" + u.getRoleString() +"\", \"id\": \""+ u.getId() + "\"}";
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getUser/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id){

		Optional<User> user = userService.findById(id);

		if(user != null)
			return new ResponseEntity<>(new UserDTO(user.get()),HttpStatus.OK);

		return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping(value = "/getUserData")
	@PreAuthorize("hasRole('ROLE_USER')" +
			"|| hasRole('ROLE_COTTAGE_OWNER')" +
			"|| hasRole('ROLE_BOAT_OWNER')" +
			"|| hasRole('ROLE_INSTRUCTOR')")
	public UserRegisterDTO user(Principal user){
		
		return UserRegisterDTO.getUserDTOFromUser(userService.findByEmail(user.getName()));
	}
	
	@PostMapping(value = "/updateProfile")
	@PreAuthorize("hasRole('ROLE_USER')" +
			"|| hasRole('ROLE_COTTAGE_OWNER')" +
			"|| hasRole('ROLE_BOAT_OWNER')" +
			"|| hasRole('ROLE_INSTRUCTOR')")
	public ResponseEntity<?> updateProfile(@RequestBody UserRegisterDTO userRegisterDTO, UriComponentsBuilder ucBuilder){
		// Converts appropriate fields to title case.
		userRegisterDTO.casify();
		
		if(!userRegisterDTO.validate())
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid data!");
		
		User existingUser = userService.findByEmail(userRegisterDTO.getEmail());
		userService.updateUser(existingUser, userRegisterDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(existingUser);
	}
	
	@PostMapping(value = "/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, UriComponentsBuilder ucBuilder, Principal user){
		
		if(!changePasswordDTO.validate())
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid data!");
		
		User changedUser = userService.findByEmail(user.getName());
		userService.changeUserPassword(changedUser, changePasswordDTO);		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(changedUser);
	}
	
	@GetMapping(value = "/loyaltyPoints")
	public String getLoyaltyPoints(Principal user){
		User reqUser = this.userService.findByEmail(user.getName());
		return "{ \"loyaltyPoints\": " + reqUser.getLoyaltyPoints() + ", \"currentRank\": \""+ loyaltyService.getRankForUser(reqUser) +"\"}";
	}
	
	@PostMapping(value="/deletionRequest")
	public ResponseEntity<?> saveDeletionRequest(@RequestBody DeletionRequestDTO deletionRequestDTO, UriComponentsBuilder ucBuilder, Principal user){
		if (deletionRequestDTO.getDeletionReason().length() < 10) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid data!");
		User requestUser = userService.findByEmail(user.getName());
		
		DeletionRequest deletionRequest = new DeletionRequest();
		deletionRequest.setDeletionReason(deletionRequestDTO.getDeletionReason());
		deletionRequest.setRequestStatus(DeletionRequestStatus.PENDING);
		deletionRequest.setUser(requestUser);
		requestUser.setDeletionRequest(deletionRequest);
		userService.save(requestUser);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestUser);
	}

	@GetMapping(value = "/isThisLoggedUser/{id}")
	public boolean checkLoggedUser(Principal user,@PathVariable int id){
		User requestUser = userService.findByEmail(user.getName());
		return  requestUser.getId() == id;
	}
	@GetMapping(value = "/isLoggedUserOfferOwner/{offerId}")
	public boolean checkLoggedUserOfferOwner(Principal user,@PathVariable int offerId){
		User requestUser = userService.findByEmail(user.getName());
		Optional<Cottage> cottage = cottageService.getCottageById(offerId);
		Optional<Boat> boat = boatService.getBoat(offerId);
		Optional<Adventure> adventure =  adventureService.findById(offerId);

		if(cottage.isPresent()){
			return cottage.get().getCottageOwnerId() == requestUser.getId();
		}
		if(boat.isPresent()){
			return boat.get().getBoatOwner().getId() == requestUser.getId();
		}
		if(adventure.isPresent()){
			return adventure.get().getInstructorId() == requestUser.getId();
		}
		return false;
	}

	@GetMapping(value = "/isLoggedUserOnlyClient")
	public boolean isLoggedUserHasRole(Principal user){

		User requestUser = userService.findByEmail(user.getName());
		return userService.isUserOnlyClient(requestUser);
	}
}