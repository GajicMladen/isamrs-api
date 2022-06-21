package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.model.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByOffer_IdEquals(Integer id);

    @Query("select r from Reservation r where r.id = ?1 and r.status = ?2")
    Optional<Reservation> getReservationByIdAndStatus(Integer id, ReservationStatus status);


    
    @Query("select r from Reservation r where r.startDateTime >= ?1 and r.startDateTime <= ?2")
	List<Reservation> findAllByDateRange(LocalDateTime start, LocalDateTime end);

    List<Reservation> findByClient_IdEquals(Integer id);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation WHERE id = ?1")
    void cancelReservationById(Integer id);


}
