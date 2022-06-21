package tim7.ISAMRSproject.service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.dto.ReservationDTO;

import tim7.ISAMRSproject.dto.ReservationListItemDTO;
import tim7.ISAMRSproject.repository.ReservationRepository;
import tim7.ISAMRSproject.utils.EmailServiceImpl;

import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.*;


@Service
@Transactional
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private CottageRepository cottageRepository;
	@Autowired
	private BoatRepository boatRepository;
	@Autowired
	private AdventureRepository adventureRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private FreePeriodRepository fpRepository;

	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private EmailServiceImpl emailService;



	public boolean AdventureHasReservations(Integer id) {
		for (Reservation r : reservationRepository.findAll()) {
			if (r.getOffer().getId() == id) {
				return true;
			}
		}
		return false;
		
	}


	public Optional<Reservation> getReservationById(int id){
		return reservationRepository.findById(id);
	}

	public List<Reservation> getReservationsForOffer(int offerId){

		List<Reservation> ret = new ArrayList<>();

		List<Reservation> allReservations =  reservationRepository.findByOffer_IdEquals(offerId);
		for (Reservation reservation:allReservations) {
			if(! reservation.getStatus().equals(ReservationStatus.FOR_ACTION))
				ret.add(reservation);
		}

		return ret;
	}


	public List<Reservation> getActionsForOffer(int offerId){
		List<Reservation> ret = new ArrayList<>();
		List<Reservation> allReservations =  reservationRepository.findByOffer_IdEquals(offerId);
		for (Reservation reservation:allReservations) {
			if(reservation.getStatus().equals(ReservationStatus.FOR_ACTION))
				ret.add(reservation);
		}

		return ret;
	}

	public void deleteAction(int id){
		this.reservationRepository.deleteById(id);
	}



	public String addNewReservation(ReservationDTO reservationDTO){
		Reservation newReservation = new Reservation();
		newReservation.setEndDateTime(reservationDTO.getEndDate());
		newReservation.setStartDateTime(reservationDTO.getStartDate());
		newReservation.setStatus(reservationDTO.getReservationStatus());

		int daysNum = Period.between(reservationDTO.getStartDate().toLocalDate(),reservationDTO.getEndDate().toLocalDate()).getDays();

		Optional<Client> client = clientRepository.findById(reservationDTO.getClientId());

		if(client.isPresent()){
			newReservation.setClient(client.get());
		}
		else{
			return "invalid Client ID";
		}

		Optional<Cottage> cottage = cottageRepository.findById(reservationDTO.getOfferId());
		Optional<Boat> boat = boatRepository.findById(reservationDTO.getOfferId());
		Optional<Adventure> adventure = adventureRepository.findById(reservationDTO.getOfferId());

		if(cottage.isPresent()) {
			newReservation.setOffer(cottage.get());
			newReservation.setTotalPrice( daysNum * cottage.get().getPrice() );
			reservationRepository.save(newReservation);
			return "OK";
		}
		if(boat.isPresent()){
			newReservation.setOffer(boat.get());
			newReservation.setTotalPrice( daysNum * boat.get().getPrice() );
			reservationRepository.save(newReservation);
			return "OK";
		}
		if(adventure.isPresent()){
			newReservation.setOffer(adventure.get());
			newReservation.setTotalPrice( daysNum * adventure.get().getPrice() );
			reservationRepository.save(newReservation);
			return "OK";
		}

		return "Invalid Offer ID";
	}

	public Reservation createReservationFromData(String startDateString, String endDateString, Offer offer, float totalPrice, String offerType, User user) {
		LocalDateTime startDate = convertDateString(startDateString);
		LocalDateTime endDate = convertDateString(endDateString);
		Reservation res = new Reservation();
		res.setStartDateTime(startDate);
		res.setEndDateTime(endDate);
		res.setTotalPrice(totalPrice);

		offer.setChanging(true);
		if (offerType.equals("cottage")){
			cottageRepository.save((Cottage) offer);
			res.setOffer(offer);
		}
		else if (offerType.equals("boat")) {
			boatRepository.save((Boat) offer);
			res.setOffer(offer);
		}else {
			adventureRepository.save((Adventure) offer);
			res.setOffer(offer);
		}
		res.setStatus(ReservationStatus.ACTIVE);
		res.setClient(clientRepository.getById(user.getId()));
		return res;
	}
	
	public void reserveFreePeriods(Reservation res) {
	
		List<FreePeriod> fps = fpRepository.findByOffer_Id(res.getOffer().getId());
		
		for (FreePeriod fp: fps) {
			if(fp.getStartDateTime().isBefore(res.getStartDateTime()) && fp.getEndDateTime().isAfter(res.getEndDateTime())) {
				FreePeriod before = new FreePeriod();
				FreePeriod after = new FreePeriod();
				
				before.setOffer(fp.getOffer());
				after.setOffer(fp.getOffer());
				before.setStartDateTime(fp.getStartDateTime());
				before.setEndDateTime(res.getStartDateTime().minusDays(1));
				after.setStartDateTime(res.getEndDateTime());
				after.setEndDateTime(fp.getEndDateTime());
				
				fpRepository.deleteById(fp.getId());
				fpRepository.save(before);
				fpRepository.save(after);
			} else if (fp.getStartDateTime().equals(res.getStartDateTime()) && fp.getEndDateTime().equals(res.getEndDateTime())) {
				fpRepository.deleteById(fp.getId());
			}
		}

		
	}
	
	public boolean saveReservation(Reservation res, User user, int points) {

		reservationRepository.save(res);

		user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
		userRepository.save(user);
		emailService.sendReservationConfirmationMail(user, res, res.getOffer().getName());
		return true;
	}
	
	public List<ReservationListItemDTO> getActiveReservations(User u) {
		List<ReservationListItemDTO> retVal = new ArrayList<ReservationListItemDTO>();
		for(Reservation r: reservationRepository.findByClient_IdEquals(u.getId())) {
			if (r.getStartDateTime().isAfter(LocalDateTime.now())) {
				ReservationListItemDTO rli = new ReservationListItemDTO();
				rli.setId(r.getId());
				rli.setStartDate(r.getStartDateTime());
				rli.setEndDate(r.getEndDateTime());
				rli.setClientId(u.getId());
				rli.setOfferId(r.getOffer().getId());
				rli.setOfferName(r.getOffer().getName());
				rli.setTotalPrice(r.getTotalPrice());
				rli.setOfferAddress(r.getOffer().getAddress().toString());
				if(LocalDateTime.now().plusDays(3).isAfter(r.getStartDateTime()))
					rli.setCanCancel(false);
				else
					rli.setCanCancel(true);
				retVal.add(rli);
			}
		}
		return retVal;
	}
	
	public List<ReservationListItemDTO> getPastReservations(User u) {
		List<ReservationListItemDTO> retVal = new ArrayList<ReservationListItemDTO>();
		for(Reservation r: reservationRepository.findByClient_IdEquals(u.getId())) {
			if (r.getStartDateTime().isBefore(LocalDateTime.now())) {
				ReservationListItemDTO rli = new ReservationListItemDTO();
				rli.setId(r.getId());
				rli.setStartDate(r.getStartDateTime());
				rli.setEndDate(r.getEndDateTime());
				rli.setClientId(u.getId());
				rli.setOfferId(r.getOffer().getId());
				rli.setOfferName(r.getOffer().getName());
				rli.setTotalPrice(r.getTotalPrice());
				rli.setOfferAddress(r.getOffer().getAddress().toString());
				if(r.getGrade() == null)
					rli.setCanCancel(true);
				else
					rli.setCanCancel(false);
				boolean canComplain = true;
				for (Complaint c: r.getComplaints()) {
					if (r.getId() == c.getReservation().getId() && !c.isFormOwner())
						canComplain = false;
				}
				rli.setCanComplain(canComplain);
				retVal.add(rli);
			}
		}
		return retVal;
	}
	
	public boolean cancelReservation(int id) {
		Reservation res = reservationRepository.getById(id);
		if (res == null) return false;
		else {
			FreePeriod fp = new FreePeriod();
			fp.setStartDateTime(res.getStartDateTime());
			fp.setEndDateTime(res.getEndDateTime());
			fp.setOffer(res.getOffer());
			reservationRepository.cancelReservationById(id);
			fpRepository.save(fp);
			return true;
		}
	}
	
	public boolean addReview(Grade grade, int reservationId) {
		Reservation res = reservationRepository.getById(reservationId);
		if (res != null) {
			res.setGrade(grade);
			grade.setReservation(res);
			gradeRepository.save(grade);
			return true;
		} else return false;

	}
	
	private LocalDateTime convertDateString(String s) {
		String[] tokens = s.split("-");
		LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
		return retVal;
	}
	
	
	public List<Reservation> getAllReservations() {
		return this.reservationRepository.findAll();
	}

	public List<Reservation> getReservationsByDataRange(LocalDateTime start, LocalDateTime end) {
		return this.reservationRepository.findAllByDateRange(start, end);
	}


	@Transactional
	public Reservation findById(int id){
		return reservationRepository.findById(id).get();
	}
	@Transactional(readOnly = false)
	public Reservation saveReservation(Reservation res){
		return reservationRepository.save(res);
	}


	@Transactional(readOnly = false)
	public boolean buyAction(Client client,Reservation res){
		if( res.getStatus() == ReservationStatus.FOR_ACTION){
			res.setClient(client);
			res.setStatus(ReservationStatus.ACTIVE);
			return true;
		}
		return false;
	}
}
