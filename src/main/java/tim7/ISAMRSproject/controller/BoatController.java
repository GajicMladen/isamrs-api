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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.ISAMRSproject.dto.BoatDTO;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.service.BoatService;
import tim7.ISAMRSproject.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/boats")
public class BoatController {

    @Autowired
    private BoatService boatService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getBoat/{id}")
    public ResponseEntity<BoatDTO> getBoat(@PathVariable int id){

        Optional<Boat> boat =  boatService.getBoat(id);
        if(boat.isPresent())
            return new ResponseEntity<>(new BoatDTO(boat.get()), HttpStatus.OK);
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/owner/{id}")
    public ResponseEntity<List<BoatDTO>> getOwnerBoats(@PathVariable int id){

        List<Boat> boats = boatService.getBoatsByOwnerId(id);
        List<BoatDTO> boatsDTOS = new ArrayList<BoatDTO>();

        for (Boat boat : boats) {
            boatsDTOS.add(new BoatDTO(boat));
        }

        return new ResponseEntity<>(boatsDTOS,HttpStatus.OK);
    }

    @PostMapping(
            value = "/newBoat",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public BoatDTO addNewBoat(@RequestBody BoatDTO newOne, Principal user) {
        User u = userService.findByEmail(user.getName());
        return new BoatDTO(boatService.addNewBoat(newOne,u));
    }

    @DeleteMapping(value = "/deleteBoat/{id}")
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public boolean deleteBoat(@PathVariable Integer id){

        return boatService.deleteBoat(id);

    }

    @PutMapping(value = "/updateBoat",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_BOAT_OWNER')")
    public void updateBoat(@RequestBody BoatDTO boat){

        Boat boat1 = boatService.getBoat(boat.getId()).get();
        boatService.editBoat(boat1,boat.getId(),boat.getName(),boat.getDescription(),boat.getPrice(),boat.getCapacity());
    }
    
    @GetMapping(value = "/getBoatsPreview")
    public ResponseEntity<?> getBoatsPreview(){
    	List<BoatDTO> dtos = new ArrayList<BoatDTO>();
    	List<Boat> boats = boatService.getBoatsPreview();
		for(Boat b: boats) {
			BoatDTO dto = new BoatDTO(b);
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    	
    }
    
    @GetMapping(value = "/getBoatsPreviewParam/{param}")
    public ResponseEntity<?> getBoatsPreviewParam(@PathVariable String param){
    	List<BoatDTO> dtos = new ArrayList<BoatDTO>();
    	List<Boat> boats = boatService.getBoatsPreviewParam(param);
		for(Boat b: boats) {
			BoatDTO dto = new BoatDTO(b);
			dtos.add(dto);
		}
		return new ResponseEntity<>(dtos, HttpStatus.OK);    
    }
}
