package tim7.ISAMRSproject.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.JwtAuthenticationRequest;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.utils.TokenUtils;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response){
		
		Authentication auth;
		try {
			auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));			
		} catch(AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
		}
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		User user = (User) auth.getPrincipal();
		
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();

		if(user.isDeleted())
			return new ResponseEntity<>("Account is deleted!",HttpStatus.FORBIDDEN);

		if(user.isActive())
			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user));
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is not verified!");
	}
	
}
