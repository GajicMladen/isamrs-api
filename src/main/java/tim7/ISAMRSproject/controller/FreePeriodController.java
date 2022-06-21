package tim7.ISAMRSproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tim7.ISAMRSproject.dto.FreePeriodDTO;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.service.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/freePeriods")
public class FreePeriodController {

    @Autowired
    private FreePeriodService freePeriodService;

    @Autowired
    private UserService userService;
    @Autowired
    private CottageService cottageService;
    @Autowired
    private BoatService boatService;

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private ReservationServiceOwner reservationServiceOwner;

    @GetMapping(value = "/getFreePeriods/{id}")
    public List<FreePeriodDTO> getFreePeriods(@PathVariable int id){

        List<FreePeriod> fr = freePeriodService.getFreePeriodByOfferId(id);
        List<FreePeriodDTO> ret = new ArrayList<>();
        for (FreePeriod f: fr) {
            ret.add(new FreePeriodDTO(f));
        }
        return ret;
    }
    
    @GetMapping(value = "/getFreePeriodsFromIds/{ids}")
	public List<FreePeriodDTO> getFreePeriodsFromIds(@PathVariable String ids) {
		List<FreePeriodDTO> freePeriods = new ArrayList<FreePeriodDTO>();
		String[] offerIds = ids.split(",");
		for (String i : offerIds) {
			int id = Integer.parseInt(i);
			List<FreePeriod> fr = freePeriodService.getFreePeriodByOfferId(id);
			for (FreePeriod f: fr) {
				freePeriods.add(new FreePeriodDTO(f));
			}
		}
		
		return freePeriods;
	}


    @PostMapping(value = "/addFreePeriod",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
                "|| hasRole('ROLE_BOAT_OWNER')"+
                "|| hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<?> addFreePeriod(@RequestBody FreePeriodDTO freePeriodDTO, Principal user){
        User u = userService.findByEmail(user.getName());

        LocalDateTime startDate = freePeriodDTO.getStartDate();
        LocalDateTime endDate = freePeriodDTO.getEndDate();

        Optional<Cottage> cottage = cottageService.getCottageById(freePeriodDTO.getOfferId());
        Optional<Boat> boat = boatService.getBoat(freePeriodDTO.getOfferId());

        if(cottage.isPresent()){
            if(cottage.get().getCottageOwnerId() != u.getId())
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            freePeriodService.addFreePeriod(startDate,endDate,cottage.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(boat.isPresent()){
            if(boat.get().getBoatOwner().getId() != u.getId())
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            freePeriodService.addFreePeriod(startDate,endDate,boat.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @PostMapping(value = "/addPeriodAdventure",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<?> addFreePeriodAdventure(@RequestBody FreePeriodDTO freePeriodDTO){

        int instructorId = adventureService.getFishingInstructorByOfferId(freePeriodDTO.getOfferId());
        for (Offer offer: adventureService.getAdventuresByInstructorId(instructorId) ) {
            if(reservationServiceOwner.isPeriodReserved(offer,freePeriodDTO.getStartDate(),freePeriodDTO.getEndDate()))
                return new ResponseEntity<>("Period je vec rezervisan.",HttpStatus.FORBIDDEN);
        }

        boolean correct = freePeriodService.addFreePeriodAdventure(freePeriodDTO);
        if (correct) {
        	return ResponseEntity.status(HttpStatus.ACCEPTED).body(correct);
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(correct);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
            "|| hasRole('ROLE_BOAT_OWNER')"+
            "|| hasRole('ROLE_INSTRUCTOR')")
    public void deleteFreePeriod(@PathVariable int id){
        freePeriodService.deleteFreePeriod(id);
    }

}
