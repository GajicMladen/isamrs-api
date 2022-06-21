package tim7.ISAMRSproject.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.dto.ClientComplaintDTO;
import tim7.ISAMRSproject.dto.DataForChartDTO;
import tim7.ISAMRSproject.dto.DateRangeDTO;
import tim7.ISAMRSproject.dto.DateRangeStringDTO;
import tim7.ISAMRSproject.dto.GradeDTO;
import tim7.ISAMRSproject.dto.ReservationDTO;
import tim7.ISAMRSproject.dto.ReservationListItemDTO;

import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.service.*;
import tim7.ISAMRSproject.service.AdventureService;
import tim7.ISAMRSproject.service.BoatService;
import tim7.ISAMRSproject.service.ClientService;
import tim7.ISAMRSproject.service.ComplaintService;
import tim7.ISAMRSproject.service.CottageService;
import tim7.ISAMRSproject.service.FreePeriodService;
import tim7.ISAMRSproject.service.LoyaltyService;
import tim7.ISAMRSproject.service.ReservationService;
import tim7.ISAMRSproject.service.UserService;

import tim7.ISAMRSproject.utils.EmailServiceImpl;

@RestController
@RequestMapping(value = "api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private BoatService boatService;
    @Autowired
    private CottageService cottageService;
    @Autowired
    private AdventureService adventureService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private FreePeriodService freePeriodService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;
    

    @Autowired
    private ReservationServiceOwner reservationServiceOwner;
    
    @Autowired
    private LoyaltyService loyaltyService;
    
    @PostMapping(value = "/addNewAction",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
            "|| hasRole('ROLE_BOAT_OWNER')"+
            "|| hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<?> addNewAction(@RequestBody ActionDTO actionDTO,Principal user){

        try{
            User u = userService.findByEmail(user.getName());
            Reservation newAction = reservationServiceOwner.addNewAction(actionDTO,u);
            if(newAction != null){
                List<Client> subscribers = clientService.getSubscribersForOffer(newAction.getOffer().getId());
                for(Client client: subscribers ) {
                    try {
                        emailService.sendActionEmail(client,newAction);
                    }
                    catch (Exception e){
                        System.out.println("Exception on sending email to : "+client.getEmail());
                    }
                }
            }
            else {
             return new ResponseEntity<>("Niste u mogucnosti napraviti akciju!\n" +
                     "Period je rezervisan ili već postoji akcija!\n" +
                     "(ili niste vlasnik ovog entiteta)",HttpStatus.FORBIDDEN);
            }
        }catch (OptimisticEntityLockException e){
            return  new ResponseEntity<>("Izgleda da je neko pokusao da rezervise...Probajte ponovo.",HttpStatus.FORBIDDEN);

        }
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getActions/{id}")
    public List<ActionDTO> getActionsForOffer(@PathVariable int id){

        List<Reservation> actionsForOffer =  reservationService.getActionsForOffer(id);
        List<ActionDTO> retVal = new ArrayList<>();
        for (Reservation reservation:actionsForOffer) {
            retVal.add(new ActionDTO(reservation));
        }

        return retVal;
    }

    @GetMapping(value = "/getReservationsForOffer/{id}")
    public List<ReservationDTO> getReservationsForOffer(@PathVariable int id){

        List<Reservation> reservationsForOffer =  reservationService.getReservationsForOffer(id);
        List<ReservationDTO> retVal = new ArrayList<>();
        for (Reservation reservation:reservationsForOffer) {
                retVal.add(new ReservationDTO(reservation));
        }

        return retVal;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
            "|| hasRole('ROLE_BOAT_OWNER')"+
            "|| hasRole('ROLE_INSTRUCTOR')")
    public void deleteFreePeriod(@PathVariable int id){
        reservationService.deleteAction(id);
    }

    @GetMapping(value = "/getReservationsForBoatOwner/{id}")
    public List<ReservationDTO> getReservationsForBoatOwner(@PathVariable int id){
        List<ReservationDTO> retVal = new ArrayList<>();

        List<Boat> offers = boatService.getBoatsByOwnerId(id);
        for (Boat boat:offers) {
            List<Reservation> actionsForOffer =  reservationService.getReservationsForOffer(boat.getId());
            for (Reservation reservation:
                    actionsForOffer) {
                if( ! reservation.getStatus().equals(ReservationStatus.FOR_ACTION))
                    retVal.add(new ReservationDTO(reservation));
            }
        }

        return retVal;
    }

    @GetMapping(value = "/getReservationsForCottageOwner/{id}")
    public List<ReservationDTO> getReservationsForCottageOwner(@PathVariable int id){
        List<ReservationDTO> retVal = new ArrayList<>();

        List<Cottage> offers = cottageService.getCottagesByOwnerId(id);
        for (Cottage cottage:offers) {
            List<Reservation> actionsForOffer =  reservationService.getReservationsForOffer(cottage.getId());
            for (Reservation reservation:
                    actionsForOffer) {
                if( ! reservation.getStatus().equals(ReservationStatus.FOR_ACTION))
                    retVal.add(new ReservationDTO(reservation));
            }
        }

        return retVal;
    }
    
    @GetMapping(value ="/getReservationsForInstructor/{id}")
    public List<ReservationDTO> getReservationsForInstructor(@PathVariable int id) {
    	List<ReservationDTO> reservationsDTO = new ArrayList<ReservationDTO>();
    	List<Adventure> adventures = this.adventureService.getAdventuresByInstructorId(id);
    	for (Adventure a : adventures) {
    		List<Reservation> reservations = this.reservationService.getReservationsForOffer(a.getId());
    		for (Reservation r : reservations) {
    			reservationsDTO.add(new ReservationDTO(r));    			
    		}
    	}
    	return reservationsDTO;
    }

    @GetMapping(value = "/getProfitChartDataForCottageOwner/{id}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public List<DataForChartDTO> getDataForChartCottageOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Cottage> offers = cottageService.getCottagesByOwnerId(id);
        for (Cottage cottage:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(cottage.getName());
            float value = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(cottage.getId());
            for (Reservation reservation: reservations) {
                value += reservation.getTotalPrice();
            }
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }

    @GetMapping(value = "/getVisitChartDataForCottageOwner/{id}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public List<DataForChartDTO> getVisitDataForChartCottageOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Cottage> offers = cottageService.getCottagesByOwnerId(id);
        for (Cottage cottage:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(cottage.getName());
            float value = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(cottage.getId());
            value += reservations.size();
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }

    @GetMapping(value = "/getGradeChartDataForCottageOwner/{id}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
    public List<DataForChartDTO> getGradeDataForChartCottageOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Cottage> offers = cottageService.getCottagesByOwnerId(id);
        for (Cottage cottage:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(cottage.getName());
            float value = 0;
            int gradesCount = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(cottage.getId());
            for (Reservation reservation: reservations) {
                if (reservation.getGrade() != null) {
                    value += reservation.getGrade().getGrade();
                    gradesCount++;
                }
            }
            if(gradesCount > 0 )
                value = value/gradesCount;
            else
                value = 0;
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }


    @GetMapping(value = "/getProfitChartDataForBoatOwner/{id}")
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public List<DataForChartDTO> getDataForChartBoatOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Boat> offers = boatService.getBoatsByOwnerId(id);
        for (Boat boat:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(boat.getName());
            float value = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(boat.getId());
            for (Reservation reservation: reservations) {
                value += reservation.getTotalPrice();
            }
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }

    @GetMapping(value = "/getVisitChartDataForBoatOwner/{id}")
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public List<DataForChartDTO> getVisitDataForChartBoatOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Boat> offers = boatService.getBoatsByOwnerId(id);
        for (Boat boat:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(boat.getName());
            float value = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(boat.getId());
            value += reservations.size();
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }

    @GetMapping(value = "/getGradeChartDataForBoatOwner/{id}")
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public List<DataForChartDTO> getGradeDataForChartBoatOwner(@PathVariable int id){
        List<DataForChartDTO> retVal = new ArrayList<>();

        List<Boat> offers = boatService.getBoatsByOwnerId(id);
        for (Boat boat:offers) {
            DataForChartDTO newData = new DataForChartDTO();
            newData.setName(boat.getName());
            float value = 0;
            int gradesCount = 0;
            List<Reservation> reservations =  reservationService.getReservationsForOffer(boat.getId());
            for (Reservation reservation: reservations) {
                if(reservation.getGrade() != null) {
                    value += reservation.getGrade().getGrade();
                    gradesCount ++;
                }
            }
            if(gradesCount> 0 )
                value = value/gradesCount;
            else
                value = 0;
            newData.setValue(value);
            retVal.add(newData);
        }

        return retVal;

    }


    @GetMapping(value = "/getFreePeriods/{cottageId}")
    public ResponseEntity<List<String>> getCottageFreePeriods(@PathVariable int cottageId){
    	List<String> retVal = new ArrayList<String>();
    	List<FreePeriod> freePeriods = freePeriodService.getFreePeriodByOfferId(cottageId);
    	for (FreePeriod fp: freePeriods) {
    		retVal.add(fp.getStartDateTime().getDayOfMonth() + "-" + fp.getStartDateTime().getMonthValue() + "-" + fp.getStartDateTime().getYear() + " " + fp.getEndDateTime().getDayOfMonth() + "-" + fp.getEndDateTime().getMonthValue() + "-" + fp.getEndDateTime().getYear());
    	}
    	return new ResponseEntity<>(retVal, HttpStatus.OK);
    }  
    
    @PostMapping(value = "/newReservation")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
            "|| hasRole('ROLE_BOAT_OWNER')"+
            "|| hasRole('ROLE_INSTRUCTOR')" +
            "|| hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewReservation(@RequestBody DateRangeStringDTO dateRangeDTO, Principal user){
    	User u = userService.findByEmail(user.getName());
    	int points = loyaltyService.getPointsForUser(u);

        Offer offer;
        switch (dateRangeDTO.getOfferType()){
            case "cottage":
                offer = cottageService.getCottageById(dateRangeDTO.getOfferId()).get();
                break;
            case "boat":
                offer = boatService.getBoat(dateRangeDTO.getOfferId()).get();
                break;
            case "adventure":
                offer = adventureService.findById(dateRangeDTO.getOfferId()).get();
                break;
            default:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    	Reservation res = reservationService.createReservationFromData(dateRangeDTO.getStartDateString(), dateRangeDTO.getEndDateString(),offer, dateRangeDTO.getTotalPrice(), dateRangeDTO.getOfferType(), u);
    	reservationService.reserveFreePeriods(res);
    	try {
    		boolean status = reservationService.saveReservation(res, u, points);
    		if (status)
        		return ResponseEntity.status(HttpStatus.CREATED).body("{\"status\":\"Reservation created successfully!\"}");

    	} catch (OptimisticEntityLockException e) {
    		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    	}
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	
    }

    
    @PostMapping(value = "/getReservationsByDateRange")
    public ResponseEntity<?> getReservationsByDateRange(@RequestBody DateRangeDTO dateRange) {
    	List<Reservation> reservations = this.reservationService.getReservationsByDataRange( dateRange.getStart(), dateRange.getEnd());
    	List<ReservationDTO> dtos = new ArrayList<ReservationDTO>();
    	for (Reservation r : reservations) {
    		ReservationDTO dto = new ReservationDTO(r);
    		dtos.add(dto);
    	}
    	return new ResponseEntity<List<ReservationDTO>>(dtos, HttpStatus.OK);
    }
    
    @GetMapping(value = "/AllReservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
    	List<Reservation> reservations = this.reservationService.getAllReservations();
    	List<ReservationDTO> dtos = new ArrayList<ReservationDTO>();
    	for (Reservation r : reservations) {
    		ReservationDTO dto = new ReservationDTO(r);
    		dtos.add(dto);
    	}
    	return new ResponseEntity<List<ReservationDTO>>(dtos, HttpStatus.OK);
    }
    
    @GetMapping(value = "/getReservation/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable int id) {
    	Optional<Reservation> reservation = this.reservationService.getReservationById(id);
    	if (reservation.isPresent()) {
    		return new ResponseEntity<>(new ReservationDTO(reservation.get()), HttpStatus.OK);
    	}
    	else {
    		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    	}
    }
  
    @GetMapping(value = "/activeReservations")
    public ResponseEntity<?> getActiveReservations(Principal user){
    	User u = userService.findByEmail(user.getName());
    	List<ReservationListItemDTO> activeReservations = reservationService.getActiveReservations(u);
    	return new ResponseEntity<>(activeReservations, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/cancelReservation/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable int id){
    	boolean status = reservationService.cancelReservation(id);
    	if (status)
    		return new ResponseEntity<>(HttpStatus.OK);
    	else
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping(value = "/pastReservations")
    public ResponseEntity<?> getPastReservations(Principal user){
    	User u = userService.findByEmail(user.getName());
    	List<ReservationListItemDTO> pastReservations = reservationService.getPastReservations(u);
    	return new ResponseEntity<>(pastReservations, HttpStatus.OK);
    }
    
    @PostMapping(value = "/addReview")
    public ResponseEntity<?> addReview(@RequestBody GradeDTO gradeDTO){
    	Grade grade = new Grade();
    	grade.setGrade(gradeDTO.getGrade());
    	grade.setRevision(gradeDTO.getReviewText());
    	grade.setStatus(ApprovalStatus.ON_WAIT);
    	
    	boolean status = reservationService.addReview(grade, gradeDTO.getId());
    	if (status)
    		return new ResponseEntity<>(HttpStatus.OK);
    	else
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping(value = "/addComplaint")
    public ResponseEntity<?> addReview(@RequestBody ClientComplaintDTO complaintDTO){
    	Complaint complaint = new Complaint();
    	
    	complaint.setFormOwner(false);
    	complaint.setForOffer(true);
    	complaint.setPunishOffender(false);
    	complaint.setText(complaintDTO.getComplaintText());
    	complaint.setStatus(ApprovalStatus.ON_WAIT);
    	
    	complaintService.addNewComplaint(complaint, complaintDTO.getId());
		return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/buyAction/{idAction}")
    public ResponseEntity<?> buyAction(Principal user,@PathVariable int idAction){
        try {
            Client client = clientService.findByEmail(user.getName());
            Reservation reservation = reservationService.findById(idAction);

            if(reservationService.buyAction(client,reservation)) {
                reservationService.saveReservation(reservation);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>("Nismo uspeli da rezervišemo akciju,pokusajte ponovo.",HttpStatus.FORBIDDEN);
        }
        catch (OptimisticEntityLockException e){
            return new ResponseEntity<>("Izgleda da je akcija već rezervisana",HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "/newReservation/{clientId}")
    @PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')"+
            "|| hasRole('ROLE_BOAT_OWNER')"+
            "|| hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<?> addTestReservation( @RequestBody DateRangeStringDTO dateRangeStringDTO,
                                                 @PathVariable int clientId,
                                                 Principal user) {

        try {
            User owner = userService.findByEmail(user.getName());

            String type;
            Offer offer;

            Optional<Cottage> cottage = cottageService.getCottageById(dateRangeStringDTO.getOfferId());
            Optional<Boat> boat = boatService.getBoat(dateRangeStringDTO.getOfferId());
            Optional<Adventure> adventure = adventureService.findById(dateRangeStringDTO.getOfferId());

            if(cottage.isPresent()){ type = "cottage"; offer = cottage.get();
                if(cottage.get().getCottageOwnerId() != owner.getId())
                    return new ResponseEntity<>("Niste vlasnik entiteta",HttpStatus.FORBIDDEN);
            }
            else if(boat.isPresent()) {type = "boat"; offer = boat.get();
                if(boat.get().getBoatOwner().getId() != owner.getId())
                    return new ResponseEntity<>("Niste vlasnik entiteta",HttpStatus.FORBIDDEN);
            }
            else if(adventure.isPresent()) {type = "adventure"; offer = adventure.get();
                if(adventure.get().getInstructorId() != owner.getId())
                    return new ResponseEntity<>("Niste vlasnik entiteta",HttpStatus.FORBIDDEN);
            }
            else return new ResponseEntity<>("Nepostojeci entitet!",HttpStatus.FORBIDDEN);

            Client client = clientService.findClientById(clientId);
            LocalDateTime startDate =convertDateString(dateRangeStringDTO.getStartDateString());
            LocalDateTime endDate =convertDateString(dateRangeStringDTO.getEndDateString());

            if (! reservationServiceOwner.isPeriodReserved(offer,startDate,endDate)) {
                Reservation newRes;
                switch (type){
                    case "cottage":
                        newRes = reservationServiceOwner.reserveCottage(cottage.get(), client, startDate, endDate);
                        break;
                    case "boat":
                        newRes = reservationServiceOwner.reserveBoat(boat.get(),client,startDate,endDate);
                        break;
                    case "adventure":
                        newRes = reservationServiceOwner.reserveAdventure(adventure.get(),client,startDate,endDate);
                        break;
                    default:
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                reservationServiceOwner.saveReservation(newRes);
                emailService.sendReservationConfirmationMail(client,newRes, offer.getName());

            } else
                return new ResponseEntity<>("Period je rezervisan",HttpStatus.FORBIDDEN);
        }
        catch ( OptimisticEntityLockException e){
            return  new ResponseEntity<>("Izgleda da je neko vec rezervisao... :(",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private LocalDateTime convertDateString(String s) {
        String[] tokens = s.split("-");
        LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
        return retVal;
    }
}
