package tim7.ISAMRSproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.OfferShortDTO;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.service.AdventureService;
import tim7.ISAMRSproject.service.BoatService;
import tim7.ISAMRSproject.service.CottageService;

@RestController
@RequestMapping(value = "/api/entity", produces=MediaType.APPLICATION_JSON_VALUE)
public class EntityListController {

	@Autowired
	private CottageService cottageService;
	
	@Autowired
	private BoatService boatService;
	
	@Autowired
	private AdventureService adventureService;
	
	@GetMapping(value = "/cottages/all")
	public ResponseEntity<List<OfferShortDTO>> getCottagesPage(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort){
		List<OfferShortDTO> cottagesDTO = cottageService.getCottagesPage(page, perPage, sort);
		return new ResponseEntity<>(cottagesDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/cottages/count")
	public ResponseEntity<Integer> getTotalCottages(){	
		return new ResponseEntity<>(cottageService.getTotalCottages(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/boats/all")
	public ResponseEntity<List<OfferShortDTO>> getBoatsPage(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort){
		List<OfferShortDTO> boatsDTO = boatService.getBoatsPage(page, perPage, sort);
		return new ResponseEntity<>(boatsDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/boats/count")
	public ResponseEntity<Integer> getTotalBoats(){
		Integer total = boatService.getTotalBoats();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}
	
	@GetMapping(value = "/adventures/all")
	public ResponseEntity<List<OfferShortDTO>> getAdventuresPage(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort){
		List<OfferShortDTO> adventuresDTO = adventureService.getAdventuresPage(page, perPage, sort);
		return new ResponseEntity<>(adventuresDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/adventures/count")
	public ResponseEntity<Integer> getTotalAdventures(){
		Integer total = adventureService.getTotalAdventures();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}	
	
	@GetMapping(value = "/cottages/search")
	public ResponseEntity<List<OfferShortDTO>> getCottagesPageSearch(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){		
		List<OfferShortDTO> cottagesDTO = new ArrayList<OfferShortDTO>();
		List<Cottage> cottageList = cottageService.getCottagesPageSearch(page, perPage, sort, startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice);
		
		for (Cottage c: cottageList) {
			cottagesDTO.add(new OfferShortDTO(c));
		}
		
		return new ResponseEntity<>(cottagesDTO, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/cottages/searchcount")
	public ResponseEntity<Integer> getCottagesPageSearchCount(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){
		return new ResponseEntity<>(cottageService.getCottagesPageSearchCount(startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice), HttpStatus.OK);
	}
	
	@GetMapping(value = "/boats/search")
	public ResponseEntity<List<OfferShortDTO>> getBoatsPageSearch(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){		
		List<OfferShortDTO> boatsDTO = new ArrayList<OfferShortDTO>();
		List<Boat> boatList = boatService.getBoatsPageSearch(page, perPage, sort, startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice);
		
		for (Boat c: boatList) {
			boatsDTO.add(new OfferShortDTO(c));
		}
		
		return new ResponseEntity<>(boatsDTO, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/boats/searchcount")
	public ResponseEntity<Integer> getBoatsPageSearchCount(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){
		return new ResponseEntity<>(boatService.getBoatsPageSearchCount(startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice), HttpStatus.OK);
	}
	
	@GetMapping(value = "/adventures/search")
	public ResponseEntity<List<OfferShortDTO>> getAdventuresPageSearch(@RequestParam("page") int page, @RequestParam("perPage") int perPage, @RequestParam("sort") String sort, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){		
		List<OfferShortDTO> adventuresDTO = new ArrayList<OfferShortDTO>();
		List<Adventure> adventureList = adventureService.getAdventuresPageSearch(page, perPage, sort, startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice);
		
		for (Adventure c: adventureList) {
			adventuresDTO.add(new OfferShortDTO(c));
		}
		
		return new ResponseEntity<>(adventuresDTO, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/adventures/searchcount")
	public ResponseEntity<Integer> getAdventuresPageSearchCount(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("name") String name, @RequestParam("minRating") float minRating, @RequestParam("location") String location, @RequestParam("capacity") int capacity, @RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") String maxPrice){
		return new ResponseEntity<>(adventureService.getAdventuresPageSearchCount(startDate, endDate, name, minRating, location, capacity, minPrice, maxPrice), HttpStatus.OK);
	}	
}
