package tim7.ISAMRSproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.model.Client;
import tim7.ISAMRSproject.model.Offer;
import tim7.ISAMRSproject.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public boolean clientExists(Integer id) {
    	return clientRepository.existsById(id);
    }
    
    public void addPenatlToClient(Integer id){
        clientRepository.addPenalToClient(id);
    }

    public Optional<Client> getClientById(Integer id){
        return clientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Client findClientById(Integer id){
        return clientRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Client findByEmail(String email) throws AccessDeniedException {
        return clientRepository.findByEmail(email);
    }


    public List<Client> getSubscribersForOffer(Integer offerId){
        return clientRepository.getSubscribersForOffer(offerId);
    }

    public boolean isClinetSubscribedToOffer(Integer clientId,Integer offerId){
        Optional<Client> client = clientRepository.findById(clientId);
        if(client.isPresent()){
            for (Offer offer:client.get().getSubscribedOffers() ) {
                if(offer.getId() == offerId)
                    return true;
            }
        }
        return false;
    }
    
    public List<Client> findAll(){
    	return this.clientRepository.findAll();
    }
    
    public Client save(Client c) {
    	return this.clientRepository.save(c);
    }
}
