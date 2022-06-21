package tim7.ISAMRSproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tim7.ISAMRSproject.dto.CottageDTO;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.CottageService;
import tim7.ISAMRSproject.service.UserService;

//import javax.jws.soap.SOAPBinding;

@RestController
@RequestMapping(value = "api/cottages")
public class CottageController {

	@Autowired
	private CottageService cottageService;

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/all")
	public ResponseEntity<List<CottageDTO>> getAllCottages(){
		
		List<Cottage> cottages = cottageService.getAllCottages();
		List<CottageDTO> cottageDTOS = new ArrayList<CottageDTO>();
	
		for (Cottage cottage : cottages) {
			cottageDTOS.add(new CottageDTO(cottage));
		}
		
		return new ResponseEntity<>(cottageDTOS,HttpStatus.OK);
	}

	@GetMapping(value = "/getCottage/{id}")
	public ResponseEntity<CottageDTO> getCottageById(@PathVariable Integer id){

		Optional<Cottage> cottage =  cottageService.getCottageById(id);
		if(cottage.isPresent())
			return new ResponseEntity<>(new CottageDTO(cottage.get()), HttpStatus.OK);
		return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/owner/{id}")
	public ResponseEntity<List<CottageDTO>> getOwnerCottages(@PathVariable int id){
		
		List<Cottage> cottages = cottageService.getCottagesByOwnerId(id);
		List<CottageDTO> cottageDTOS = new ArrayList<CottageDTO>();
	
		for (Cottage cottage : cottages) {
			cottageDTOS.add(new CottageDTO(cottage));
		}
		
		return new ResponseEntity<>(cottageDTOS,HttpStatus.OK);
	} 
	
	@PostMapping(
			value = "/newCottage",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
	public CottageDTO addNewCottage(@RequestBody CottageDTO newOne) {
		Optional<User> user = userService.findById(newOne.getOwnerId());
		if(user.isPresent() && user.get().hasRole("ROLE_COTTAGE_OWNER"))
			return new CottageDTO(cottageService.addNewCottage(newOne,user.get()));
		return null;
	}

	@DeleteMapping(value = "/deleteCottage/{id}")
	@PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
	public boolean deleteCottage(@PathVariable Integer id){

		return cottageService.deleteCottage(id);

	}

	@PutMapping(value = "/updateCottage",consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_COTTAGE_OWNER')")
	public void updateCottage(@RequestBody CottageDTO cottage){

		Cottage cottage1 = cottageService.getCottageById(cottage.getId()).get();
		cottageService.editCottage(cottage1,cottage.getId(),cottage.getName(),cottage.getDescription(),
				cottage.getPrice(),cottage.getCapacity());
	}
	
    @GetMapping(value = "/getCottagesPreview")
    public ResponseEntity<?> getBoatsPreview(){
    	List<CottageDTO> dtos = new ArrayList<CottageDTO>();
    	List<Cottage> cottages = cottageService.getCottagesPreview();
		for(Cottage c: cottages) {
			CottageDTO dto = new CottageDTO(c);
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    	
    }
    
    @GetMapping(value = "/getCottagesPreviewParam/{param}")
    public ResponseEntity<?> getBoatsPreviewParam(@PathVariable String param){
    	List<CottageDTO> dtos = new ArrayList<CottageDTO>();
    	List<Cottage> cottages = cottageService.getCottagesPreviewParam(param);
		for(Cottage c: cottages) {
			CottageDTO dto = new CottageDTO(c);
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    	
    }

}
