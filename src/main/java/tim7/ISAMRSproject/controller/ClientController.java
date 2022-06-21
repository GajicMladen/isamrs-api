package tim7.ISAMRSproject.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.SubscriptionDTO;
import tim7.ISAMRSproject.dto.UnsubscribeDTO;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.Client;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.model.Offer;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.ClientService;
import tim7.ISAMRSproject.service.SubscribeService;
import tim7.ISAMRSproject.service.UserService;

@RestController
@RequestMapping(value = "api/clients")
@Transactional
public class ClientController {

    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private SubscribeService subscribeService;

    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER') or hasRole('ROLE_BOAT_OWNER') or hasRole('ROLE_INSTRUCTOR')")
    @PostMapping(value = "/addPenalToClient/{id}")
    public ResponseEntity<?> addPenalToClient(@PathVariable int id){
    	if (clientService.clientExists(id)) {
    		clientService.addPenatlToClient(id);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/addSubscription/{offerId}")
    public void addSubscription(Principal user,@PathVariable int offerId){

        User reqUser = this.userService.findByEmail(user.getName());
        subscribeService.addSubsription(reqUser.getId(),offerId);

    }
    @PostMapping(value = "/removeSubscription/{offerId}")
    public void removeSubscription(Principal user,@PathVariable int offerId){

        User reqUser = this.userService.findByEmail(user.getName());
        subscribeService.removeSubsription(reqUser.getId(),offerId);

    }
    @GetMapping(value = "/isSubscribedToOffer/{offerId}")
    public boolean isSubscribedToOffer(Principal user,@PathVariable int offerId){

        User reqUser = this.userService.findByEmail(user.getName());
        return clientService.isClinetSubscribedToOffer(reqUser.getId(),offerId);

    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value="/subscriptions")
	public ResponseEntity<?> getClientSubscriptions(Principal user){
		User u = this.userService.findByEmail(user.getName());
		if (u == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		List<Offer> subs = userService.getAllSubscriptions(u);
		List<SubscriptionDTO> dtos = new ArrayList<SubscriptionDTO>();
		for(Offer a: subs) {
			SubscriptionDTO subDTO = new SubscriptionDTO();
			subDTO.setId(a.getId());
			subDTO.setOfferName(a.getName());
			subDTO.setOfferAddress(a.getAddress().toString());
			subDTO.setSubscribed(true);
			subDTO.setPrice(a.getPrice());

			if (a instanceof Cottage) {
				Cottage ac = (Cottage) a;
				subDTO.setOwnerName(ac.getCottageOwner().getName()+ " " + ac.getCottageOwner().getLastName());
				subDTO.setInstructor(false);
				dtos.add(subDTO);
			} else if (a instanceof Boat) {
				Boat ab = (Boat) a;
				subDTO.setOwnerName(ab.getBoatOwner().getName()+ " " + ab.getBoatOwner().getLastName());
				subDTO.setInstructor(false);
				dtos.add(subDTO);
			} else {
				Adventure aa = (Adventure) a;
				subDTO.setOwnerName(aa.getFishingInstructor().getName()+ " " + aa.getFishingInstructor().getLastName());
				subDTO.setInstructor(false);
				dtos.add(subDTO);
			}
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value="/unsubscribe")
    public ResponseEntity<?> unsubscribeOffer(@RequestBody UnsubscribeDTO unsubDTO, Principal user){
    	User u = this.userService.findByEmail(user.getName());
    	if (u == null)
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	subscribeService.removeSubsription(u.getId(), unsubDTO.getId());
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value="/resubscribe")
    public ResponseEntity<?> resubscribeOffer(@RequestBody UnsubscribeDTO unsubDTO, Principal user){
    	User u = this.userService.findByEmail(user.getName());
    	if (u == null)
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	subscribeService.addSubsription(u.getId(), unsubDTO.getId());
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value="/penalties")
    public ResponseEntity<?> getPenaltiesForClient(Principal user){
    	Client c = this.clientService.findByEmail(user.getName());
    	if (c == null)
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	else
    		return new ResponseEntity<>(c.getPenalCount(), HttpStatus.OK);
    }

}
