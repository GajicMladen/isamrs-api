package tim7.ISAMRSproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.AdventureRepository;
import tim7.ISAMRSproject.repository.BoatRepository;
import tim7.ISAMRSproject.repository.ClientRepository;
import tim7.ISAMRSproject.repository.CottageRepository;

import java.util.Optional;

@Service
@Transactional
public class SubscribeService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CottageRepository cottageRepository;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private AdventureRepository adventureRepository;


    public void addSubsription(int clinetnId,int offerId){

        Optional<Client> client = clientRepository.findById(clinetnId);

        if(client.isPresent()){

            Optional<Cottage> cottage = cottageRepository.findById(offerId);
            if(cottage.isPresent() && ! client.get().isSubscribedToOffer(cottage.get().getId())){
                client.get().addSubscribedOffer(cottage.get());
                clientRepository.save(client.get());
                return;
            }

            Optional<Boat> boat = boatRepository.findById(offerId);
            if(boat.isPresent() && ! client.get().isSubscribedToOffer(boat.get().getId())){
                client.get().addSubscribedOffer(boat.get());
                clientRepository.save(client.get());
                return;
            }

            Optional<Adventure> adventure = adventureRepository.findById(offerId);
            if(adventure.isPresent() && ! client.get().isSubscribedToOffer(adventure.get().getId())){
                client.get().addSubscribedOffer(adventure.get());
                clientRepository.save(client.get());
            }

        }
    }
    public void removeSubsription(int clinetnId,int offerId){

        Optional<Client> client = clientRepository.findById(clinetnId);

        if(client.isPresent()){

            Optional<Cottage> cottage = cottageRepository.findById(offerId);
            if(cottage.isPresent() &&  client.get().isSubscribedToOffer(cottage.get().getId())){
                client.get().removeSubscribedOffer(cottage.get());
                clientRepository.save(client.get());
                return;
            }

            Optional<Boat> boat = boatRepository.findById(offerId);
            if(boat.isPresent() &&  client.get().isSubscribedToOffer(boat.get().getId())){
                client.get().removeSubscribedOffer(boat.get());
                clientRepository.save(client.get());
                return;
            }

            Optional<Adventure> adventure = adventureRepository.findById(offerId);
            if(adventure.isPresent() &&  client.get().isSubscribedToOffer(adventure.get().getId())){
                client.get().removeSubscribedOffer(adventure.get());
                clientRepository.save(client.get());
                return;
            }
        }
    }
}
