package tim7.ISAMRSproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.model.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    @Transactional
    @Modifying
    @Query("update Client c set c.penalCount = c.penalCount + 1 where c.id = ?1")
    void addPenalToClient(Integer id);

    @Query("select c from Client c inner join c.subscribedOffers subscribedOffers where subscribedOffers.id = ?1")
    List<Client> getSubscribersForOffer(Integer id);

    Client findByEmail(String email);


}
