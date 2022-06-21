package tim7.ISAMRSproject.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import tim7.ISAMRSproject.dto.UserRegisterDTO;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.UserService;
import tim7.ISAMRSproject.utils.EmailServiceImpl;

/*
 * Ova klasa predstavlja kontroler za registraciju korisnika.
 * */

@RestController
@RequestMapping(value="/reg", produces=MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {
	
	private String SUCCESSFUL_ACTIVATION_REDIRECT = "http://localhost:4200/login/success";
	private String ALREADY_ACTIVE_REDIRECT = "http://localhost:4200/login/alreadyactive";
	private String INVALID_ACTIVATION_TRY = "http://localhost:4200/login/invalidactivation";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailServiceImpl mailService;
	
	/*
	 * Endpoint za registraciju novog korisnika
	 * */
	@PostMapping("/signup")
	public ResponseEntity<?> addUser(@RequestBody UserRegisterDTO userRegisterDTO, UriComponentsBuilder ucBuilder){
		// Converts appropriate fields to title case.
		userRegisterDTO.casify();
		
		if(!userRegisterDTO.validate())
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid data!");
		
		User existingUser = userService.findByEmail(userRegisterDTO.getEmail());
		
		if (existingUser != null) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Email is already taken!");
		}
		
		User newUser = userService.save(userRegisterDTO);
		if (userRegisterDTO.getRole().equals("ROLE_USER"))
			mailService.sendConfirmationMail(newUser);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@GetMapping("/activate/{id}")
	public ResponseEntity<?> activateUser(@PathVariable Integer id ){
		
		User user = userService.findById(id).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(INVALID_ACTIVATION_TRY)).build();
		}
		else {
			if (user.isActive())
				return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(ALREADY_ACTIVE_REDIRECT)).build();
			else {
				user.setActive(true);
				userService.save(user);
				return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(SUCCESSFUL_ACTIVATION_REDIRECT)).build();
			}
		}
	}
	
}
