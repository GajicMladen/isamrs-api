package tim7.ISAMRSproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.AdventureRepository;
import tim7.ISAMRSproject.repository.BoatRepository;
import tim7.ISAMRSproject.repository.CottageRepository;
import tim7.ISAMRSproject.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceOwner {


        @Autowired
        private ReservationRepository reservationRepository;

        @Autowired
        private CottageRepository cottageRepository;
        @Autowired
        private BoatRepository boatRepository;
        @Autowired
        private AdventureRepository adventureRepository;

        public Reservation reserveCottage(Cottage cottage, Client client, LocalDateTime startDate,LocalDateTime endDate){
            int daysNum = Period.between(startDate.toLocalDate(),endDate.toLocalDate()).getDays();

            Reservation newRes = new Reservation();
            newRes.setClient(client);
            newRes.setStatus(ReservationStatus.ACTIVE);
            newRes.setTotalPrice(daysNum*cottage.getPrice());
            newRes.setOffer(cottage);
            newRes.setStartDateTime(startDate);
            newRes.setEndDateTime(endDate);

            cottage.setChanging(!cottage.isChanging());
            cottageRepository.save(cottage);

            return newRes;
        }
    public Reservation reserveBoat(Boat boat, Client client, LocalDateTime startDate,LocalDateTime endDate){
        int daysNum = Period.between(startDate.toLocalDate(),endDate.toLocalDate()).getDays();

        Reservation newRes = new Reservation();
        newRes.setClient(client);
        newRes.setStatus(ReservationStatus.ACTIVE);
        newRes.setTotalPrice(daysNum*boat.getPrice());
        newRes.setOffer(boat);
        newRes.setStartDateTime(startDate);
        newRes.setEndDateTime(endDate);

        boat.setChanging(!boat.isChanging());
        boatRepository.save(boat);

        return newRes;
    }
    public Reservation reserveAdventure(Adventure adventure, Client client, LocalDateTime startDate,LocalDateTime endDate){
        int daysNum = Period.between(startDate.toLocalDate(),endDate.toLocalDate()).getDays();

        Reservation newRes = new Reservation();
        newRes.setClient(client);
        newRes.setStatus(ReservationStatus.ACTIVE);
        newRes.setTotalPrice(daysNum*adventure.getPrice());
        newRes.setOffer(adventure);
        newRes.setStartDateTime(startDate);
        newRes.setEndDateTime(endDate);

        adventure.setChanging(!adventure.isChanging());
        adventureRepository.save(adventure);

        return newRes;
    }

        public void saveReservation(Reservation res){
            reservationRepository.save(res);
        }

        public boolean isPeriodReserved(Offer offer,LocalDateTime startDate,LocalDateTime endDate){

            for (Reservation reservation:reservationRepository.findByOffer_IdEquals(offer.getId())) {
                if( !(startDate.isAfter(reservation.getEndDateTime())
                    || endDate.isBefore(reservation.getStartDateTime()))){
                    return true;
                }
            }

            return false;
        }

    public Reservation addNewAction(ActionDTO actionDTO,User user){

        Optional<Cottage> cottage = cottageRepository.findById(actionDTO.getOfferId());
        Optional<Boat> boat = boatRepository.findById(actionDTO.getOfferId());
        Optional<Adventure> adventure = adventureRepository.findById(actionDTO.getOfferId());

        if(cottage.isPresent()) {
            if( cottage.get().getCottageOwnerId() != user.getId())
                return null;
            if(! isPeriodReserved(cottage.get(),actionDTO.getStartDate(),actionDTO.getEndDate()))
                return addNewActionCottage(actionDTO.getStartDate(),actionDTO.getEndDate(),actionDTO.getTotalPrice(),cottage.get());
        }
        if(boat.isPresent()){
            if( boat.get().getBoatOwner().getId() != user.getId())
                return null;
            if(! isPeriodReserved(boat.get(),actionDTO.getStartDate(),actionDTO.getEndDate()))
                return addNewActionBoat(actionDTO.getStartDate(),actionDTO.getEndDate(),actionDTO.getTotalPrice(),boat.get());
        }
        if(adventure.isPresent()){
            if( adventure.get().getInstructorId() != user.getId())
                return null;
            if(! isPeriodReserved(adventure.get(),actionDTO.getStartDate(),actionDTO.getEndDate()))
                return addNewActionCottage(actionDTO.getStartDate(),actionDTO.getEndDate(),actionDTO.getTotalPrice(),cottage.get());
        }

        return null;

    }

    public Reservation addNewActionCottage(LocalDateTime startDate,LocalDateTime endDate,float totalPrice,Cottage cottage){

        Reservation newAction = new Reservation();
        newAction.setEndDateTime(endDate);
        newAction.setStartDateTime(startDate);
        newAction.setTotalPrice(totalPrice);
        newAction.setStatus(ReservationStatus.FOR_ACTION);
        newAction.setOffer(cottage);

        cottage.setChanging(!cottage.isChanging());
        cottageRepository.save(cottage);

        return reservationRepository.save(newAction);
    }
    public Reservation addNewActionBoat(LocalDateTime startDate,LocalDateTime endDate,float totalPrice,Boat boat){

        Reservation newAction = new Reservation();
        newAction.setEndDateTime(endDate);
        newAction.setStartDateTime(startDate);
        newAction.setTotalPrice(totalPrice);
        newAction.setStatus(ReservationStatus.FOR_ACTION);
        newAction.setOffer(boat);

        boat.setChanging(!boat.isChanging());
        boatRepository.save(boat);

        return reservationRepository.save(newAction);
    }

}
