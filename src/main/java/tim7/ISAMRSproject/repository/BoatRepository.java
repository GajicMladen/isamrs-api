package tim7.ISAMRSproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.model.Boat;

public interface BoatRepository extends JpaRepository<Boat,Integer>{

  @Query("select COUNT(*) from Boat")
  public Integer getTotalBoats();
  
  @Query("select v from Boat v where v.boatOwner.id = ?1")
  public List<Boat> findByOwnerId(Integer ownerId);
  
  @Query("select c from Boat c inner join c.freePeriods freePeriods " +
  "where upper(c.name) like ?1 and c.price <= ?2 and c.price >= ?3 and c.rating >= ?4 and upper(c.address.city) like ?5 and c.capacity >= ?6 and freePeriods.startDateTime < ?7 and freePeriods.endDateTime > ?8")
  public List<Boat> getBoatsPageSearch(String name, float price, float price1, float rating, String city, int capacity, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

  @Query("select COUNT(c) from Boat c inner join c.freePeriods freePeriods " +
  "where upper(c.name) like ?1 and c.price <= ?2 and c.price >= ?3 and c.rating >= ?4 and upper(c.address.city) like ?5 and c.capacity >= ?6 and freePeriods.startDateTime < ?7 and freePeriods.endDateTime > ?8")
  public int getBoatsSearchCount(String name, float price, float price1, float rating, String city, int capacity, LocalDateTime startDateTime, LocalDateTime endDateTime);

  @Transactional
  @Modifying
  @Query(value = "update Boat b set b.name = ?2 , b.promoDescription = ?3 , b.price = ?4, b.capacity = ?5 where b.id = ?1")
  public void updateBoat(Integer boatId,String name,String promoDescription,float price, int capacity);

  @Query("select a.boatOwner.id from Boat a where a.id = ?1")
  public Integer getBoatOwnerByOfferId(Integer id);
  

  List<Boat> findBySubscribers_IdEquals(Integer id);
  
  @Query("select b from Boat b where upper(b.name) like ?1 or str(b.maxSpeed) like ?1 or str(b.rating) like ?1 or str(b.price) like ?1 or str(b.capacity) like ?1")
  public List<Boat> getBoatsPreviewParam(String param, Pageable pageable);
}

