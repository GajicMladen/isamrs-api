package tim7.ISAMRSproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.dto.CottageDTO;
import tim7.ISAMRSproject.dto.OfferShortDTO;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.model.CottageOwner;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.repository.CottageRepository;

@Service
@Transactional
public class CottageService {

	@Autowired
	private CottageRepository cottageRepository;
	
	public List<Cottage> getAllCottages(){
		
		return cottageRepository.findAll();
	}
	public Optional<Cottage> getCottageById(Integer id){
		return cottageRepository.findById(id);
	}

	public Cottage saveCottage(Cottage cottage){
		return cottageRepository.save(cottage);
	}

	public List<Cottage> getCottagesByOwnerId(Integer ownerId){
		
		return cottageRepository.findByOwnerId(ownerId);
	}
	
	public Cottage addNewCottage(CottageDTO cottageDTO, User user) {

		Cottage cottage = new Cottage(cottageDTO);
		cottage.setCottageOwner((CottageOwner) user);
		return cottageRepository.save(cottage);

	}

	public boolean deleteCottage(Integer id){
		try{
			cottageRepository.deleteById(id);
			return true;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}

	public void editCottage(Cottage cottage,Integer id,String name,String description,float price,int capacity){

		cottage.setChanging(!cottage.isChanging());
		cottageRepository.save(cottage);

		cottageRepository.updateCottage(id, name,
				description,price,capacity);
	}
	
	public List<OfferShortDTO> getCottagesPage(int page, int perPage, String sort){
		List<OfferShortDTO> cottagesDTO = new ArrayList<OfferShortDTO>();
		Page<Cottage> cottages;
		switch(sort) {
			case "name-asc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				cottages = cottageRepository.findAll(PageRequest.of(page, perPage));
		}
		for(Cottage c: cottages)
			cottagesDTO.add(new OfferShortDTO(c));
		return cottagesDTO;
	}
	
	public int getTotalCottages() {
		return cottageRepository.getTotalCottages();
	}
	
	public List<Cottage> getCottagesPageSearch(int page, int perPage, String sort, String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice){
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		List<Cottage> cottages;
		switch(sort) {
			case "name-asc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").ascending()));
				break;
			case "name-desc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("name").descending()));
				break;
			case "rating-asc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").ascending()));
				break;
			case "rating-desc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("rating").descending()));
				break;
			case "location-asc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").ascending()));
				break;
			case "location-desc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("address.city").descending()));
				break;
			case "price-asc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").ascending()));
				break;
			case "price-desc":
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage, Sort.by("price").descending()));
				break;
			default:
				cottages = cottageRepository.getCottagesPageSearch(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj, PageRequest.of(page, perPage));
		}
		
		return cottages;
	}
	
	
	public Integer getCottagesPageSearchCount(String startDate, String endDate, String name, float minRating, String location, int capacity, int minPrice, String maxPrice) {	
		name = "%" + name.toUpperCase() + "%";
		location = "%" + location.toUpperCase() + "%";
		LocalDateTime startDateObj = convertDateString(startDate);
		LocalDateTime endDateObj = convertDateString(endDate);	
		float maxPriceObj;
		if (maxPrice.equals("null")) maxPriceObj = 99999999;
		else maxPriceObj = Float.parseFloat(maxPrice);
		
		return cottageRepository.getCottagesSearchCount(name, maxPriceObj, (float)minPrice, minRating, location, capacity, startDateObj, endDateObj);	
	}
	
	private LocalDateTime convertDateString(String s) {
		String[] tokens = s.split("-");
		LocalDateTime retVal = LocalDateTime.of(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]), 0, 0);
		return retVal;
	}
	
	public List<Cottage> getCottagesPreview(){
	  return cottageRepository.findAll(PageRequest.of(0, 5)).getContent();
	}
	  
	public List<Cottage> getCottagesPreviewParam(String param){
		String paramSql = "%" + param.toUpperCase() + "%";
		return cottageRepository.getCottagesPreviewParam(paramSql, PageRequest.of(0, 5));
	}
}
