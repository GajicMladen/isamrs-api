package tim7.ISAMRSproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.dto.BoatDTO;
import tim7.ISAMRSproject.dto.OfferShortDTO;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.BoatOwner;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.repository.BoatRepository;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    public Optional<Boat> getBoat(int id){

        return boatRepository.findById(id);
    }

    public List<Boat> getBoatsByOwnerId(Integer ownerId){

        return boatRepository.findByOwnerId(ownerId);
    }

    public List<OfferShortDTO> getBoatsPage(int page, int perPage, String sort){
	    ArrayList<OfferShortDTO> boatsDTO = new ArrayList<OfferShortDTO>();
		Page<Boat> boats;
		switch(sort) {
			case "name-asc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				boats = boatRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				boats = boatRepository.findAll(PageRequest.of(page, perPage));
		}
		for(Boat b: boats)
			boatsDTO.add(new OfferShortDTO(b));
		return boatsDTO;
    }
    
	public int getTotalBoats() {
		return boatRepository.getTotalBoats();
	}
	
	public List<Boat> getBoatsPageSearch(int page, int perPage, String sort, String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice){
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		List<Boat> boats;
		switch(sort) {
			case "name-asc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				boats = boatRepository.getBoatsPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage));
		}
		
		return boats;
	}
	
	
	public Integer getBoatsPageSearchCount(String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice) {	
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		return boatRepository.getBoatsSearchCount(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj);	
	}
	
	private LocalDateTime convertDateString(String s) {
		String[] tokens = s.split("-");
		LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
		return retVal;
	}

  public List<Boat> getAllBoats() {
    return boatRepository.findAll();
  }

  public Boat addNewBoat(BoatDTO boatDTO, User user) {

      Boat boat = new Boat(boatDTO);
      boat.setBoatOwner((BoatOwner) user);
      return saveBoat(boat);

  }

  public Boat saveBoat(Boat boat){
		return boatRepository.save(boat);
  }

  public boolean deleteBoat(Integer id){
      try{
          boatRepository.deleteById(id);
          return true;
      }
      catch (Exception e){
          System.out.println(e.getMessage());
          return false;
      }
  }


	public void editBoat(Boat boat,Integer id,String name,String description,float price,int capacity){

		boat.setChanging(!boat.isChanging());
		boatRepository.save(boat);

		boatRepository.updateBoat(id, name,
				description,price,capacity);
	}

  
  public List<Boat> getBoatsPreview(){
	  return boatRepository.findAll(PageRequest.of(0, 5)).getContent();
  }
  
  public List<Boat> getBoatsPreviewParam(String param){
	  String paramSql = "%" + param.toUpperCase() + "%";
	  return boatRepository.getBoatsPreviewParam(paramSql, PageRequest.of(0, 5));
  }
}
