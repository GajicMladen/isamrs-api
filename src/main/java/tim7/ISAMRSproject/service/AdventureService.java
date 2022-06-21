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

import tim7.ISAMRSproject.dto.AdventureDTO;
import tim7.ISAMRSproject.dto.OfferShortDTO;
import tim7.ISAMRSproject.dto.UserDTO;
import tim7.ISAMRSproject.model.Address;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.FishingInstructor;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.repository.AdventureRepository;

@Service
public class AdventureService {
	
	@Autowired
	private AdventureRepository adventureRepository;
	
	@Autowired
	private UserService userService;
	
	public Optional<Adventure> findById(Integer id) {
		return this.adventureRepository.findById(id);
	}
	
	public void remove(Integer id) {
		try{
			Optional<Adventure> a =  this.adventureRepository.findById(id);
			if (a.isPresent()) {
				Adventure adventure = a.get();
				adventure.setDeleted(true);
				this.adventureRepository.save(adventure);
				
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void addAdventure(AdventureDTO a) {
		Adventure adventure = new Adventure();
		Address address = new Address();
		address.setStreet(a.getStreet());
		address.setCity(a.getCity());
		address.setCountry(a.getCountry());
		address.setLatitude(a.getLatitude());
		address.setLongitude(a.getLongitude());
		address.setOffer(adventure);		
		adventure.setAddress(address);
		adventure.setCapacity(a.getCapacity());
		adventure.setDeleted(false);
		adventure.setEquipment(a.getEquipment());
		adventure.setFishingInstructor((FishingInstructor)userService.findById(a.getInstructorId()).get());
		adventure.setInstructorBiography(a.getInstructorBiography());
		adventure.setMoreInfo(a.getMoreInfo());
		adventure.setName(a.getName());
		adventure.setPrice(a.getPrice());
		adventure.setPromoDescription(a.getPromoDescription());
		adventure.setRulesOfCancelation(a.getRulesOfCancelation());
		adventure.setRulesOfConduct(a.getRulesOfConduct());
		this.adventureRepository.save(adventure);
	}
	
	public void updateInstructorData(UserDTO instructor) {
		Optional<User> user = userService.findById(instructor.getId());
		if (user.isPresent()) {
			User i = user.get();
			i.setName(instructor.getName());
			i.setLastName(instructor.getLastName());
			i.setPhone(instructor.getPhone());
			/*Optional<Address> address = addressService.findById(i.getAddress().getId());
			if (address.isPresent()) {
				Address a = address.get();
				a.setStreet(instructor.getStreet());
				a.setCity(instructor.getCity());
				a.setCountry(instructor.getCountry());
				a.setLongitude(instructor.getLongitude());
				a.setLatitude(instructor.getLatitude());
				addressService.save(a);
			}*/
			i.getAddress().setStreet(instructor.getStreet());
			i.getAddress().setCity(instructor.getCity());
			i.getAddress().setCountry(instructor.getCountry());
			i.getAddress().setLatitude(instructor.getLatitude());
			i.getAddress().setLongitude(instructor.getLongitude());
			
			userService.save(i);
		}
	}

	public List<Adventure> getAdventuresByInstructorId(int id) {
		return adventureRepository.findByInstructorId(id);
	}
	
	public void editAdventure(AdventureDTO adventureDTO) {
		Optional<Adventure> a = adventureRepository.findById(adventureDTO.getId());
		if (a.isPresent()) {
			Adventure adventure = a.get();
			adventure.setName(adventureDTO.getName());
			adventure.getAddress().setStreet(adventureDTO.getStreet());
			adventure.getAddress().setCity(adventureDTO.getCity());
			adventure.getAddress().setCountry(adventureDTO.getCountry());
			adventure.getAddress().setLatitude(adventureDTO.getLatitude());
			adventure.getAddress().setLongitude(adventureDTO.getLongitude());
			adventure.setCapacity(adventureDTO.getCapacity());
			adventure.setDeleted(false);
			adventure.setEquipment(adventureDTO.getEquipment());
			adventure.setInstructorBiography(adventureDTO.getInstructorBiography());
			adventure.setMoreInfo(adventureDTO.getMoreInfo());
			adventure.setPrice(adventureDTO.getPrice());
			adventure.setPromoDescription(adventureDTO.getPromoDescription());
			adventure.setRulesOfCancelation(adventureDTO.getRulesOfCancelation());
			adventure.setRulesOfConduct(adventureDTO.getRulesOfConduct());
			
			adventureRepository.save(adventure);
		}
	}
	
	public List<OfferShortDTO> getAdventuresPage(int page, int perPage, String sort){
		List<OfferShortDTO> adventuresDTO = new ArrayList<OfferShortDTO>();
		Page<Adventure> adventures;
		switch(sort) {
			case "name-asc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				adventures = adventureRepository.findAll(PageRequest.of(page, perPage));
		}
		for(Adventure a: adventures)
			adventuresDTO.add(new OfferShortDTO(a));
		return adventuresDTO;
	}
	
	public Integer getTotalAdventures() {
		return adventureRepository.getTotalAdventures();
	}
	
	public List<Adventure> getAdventuresPageSearch(int page, int perPage, String sort, String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice){
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		List<Adventure> adventures;
		switch(sort) {
			case "name-asc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				adventures = adventureRepository.getAdventuresPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage));
		}
		
		return adventures;
	}
	
	
	public Integer getAdventuresPageSearchCount(String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice) {	
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		return adventureRepository.getAdventuresSearchCount(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj);	
	}
	
	private LocalDateTime convertDateString(String s) {
		String[] tokens = s.split("-");
		LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
		return retVal;
	}
	
	public Integer getFishingInstructorByOfferId(Integer id) {
		return this.adventureRepository.getFishingInstrucorByOfferId(id);
	}
	
	public List<Adventure> getAdventuresPreview(){
		return adventureRepository.findAll(PageRequest.of(0, 5)).getContent();
	}
	
	public List<Adventure> getAdventuresPreviewParam(String param){
	  String paramSql = "%" + param.toUpperCase() + "%";
	  return adventureRepository.getAdventurePreviewParam(paramSql, PageRequest.of(0, 5));
	}
}
