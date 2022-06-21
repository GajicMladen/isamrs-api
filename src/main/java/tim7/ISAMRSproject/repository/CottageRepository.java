package tim7.ISAMRSproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.model.Cottage;


public interface CottageRepository extends JpaRepository<Cottage,Integer>{
	
	@Query("select v from Cottage v where v.cottageOwner.id = ?1")
	public List<Cottage> findByOwnerId(Integer ownerId);

	@Transactional
	@Modifying
	@Query(value = "update Cottage c set c.name = ?2 , c.promoDescription = ?3 , c.price = ?4, c.capacity = ?5 where c.id = ?1")
	public void updateCottage(Integer cottageId,String name,String promoDescription,float price, int capacity);
	
	@Query("select COUNT(*) from Cottage")
	public Integer getTotalCottages();

	@Query("select c from Cottage c inner join c.freePeriods freePeriods " +
            "where upper(c.name) like ?1 and c.price <= ?2 and c.price >= ?3 and c.rating >= ?4 and upper(c.address.city) like ?5 and c.capacity >= ?6 and freePeriods.startDateTime < ?7 and freePeriods.endDateTime > ?8")
    public List<Cottage> getCottagesPageSearch(String name, float price, float price1, float rating, String city, int capacity, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

	@Query("select COUNT(c) from Cottage c inner join c.freePeriods freePeriods " +
            "where upper(c.name) like ?1 and c.price <= ?2 and c.price >= ?3 and c.rating >= ?4 and upper(c.address.city) like ?5 and c.capacity >= ?6 and freePeriods.startDateTime < ?7 and freePeriods.endDateTime > ?8")
    public int getCottagesSearchCount(String name, float price, float price1, float rating, String city, int capacity, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("select a.cottageOwner.id from Cottage a where a.id = ?1")
    public Integer getCottageOwnerByOfferId(Integer id);
	
	List<Cottage> findBySubscribers_IdEquals(Integer id);
	
    @Query("select c from Cottage c where upper(c.name) like ?1 or str(c.roomCount) like ?1 or str(c.rating) like ?1 or str(c.price) like ?1 or str(c.capacity) like ?1")
    public List<Cottage> getCottagesPreviewParam(String param, Pageable pageable);
}
