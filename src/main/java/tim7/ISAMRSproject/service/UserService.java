package tim7.ISAMRSproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tim7.ISAMRSproject.dto.ChangePasswordDTO;
import tim7.ISAMRSproject.dto.UserRegisterDTO;
import tim7.ISAMRSproject.model.Address;
import tim7.ISAMRSproject.model.Admin;
import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.BoatOwner;
import tim7.ISAMRSproject.model.Client;
import tim7.ISAMRSproject.model.Cottage;
import tim7.ISAMRSproject.model.CottageOwner;
import tim7.ISAMRSproject.model.DeletionRequest;
import tim7.ISAMRSproject.model.FishingInstructor;
import tim7.ISAMRSproject.model.Offer;
import tim7.ISAMRSproject.model.RegistrationRequest;
import tim7.ISAMRSproject.model.RegistrationRequest.RegistrationRequestStatus;
import tim7.ISAMRSproject.model.Role;
import tim7.ISAMRSproject.model.User;
import tim7.ISAMRSproject.repository.AdminRepository;
import tim7.ISAMRSproject.repository.AdventureRepository;
import tim7.ISAMRSproject.repository.BoatOwnerRepository;
import tim7.ISAMRSproject.repository.BoatRepository;
import tim7.ISAMRSproject.repository.ClientRepository;
import tim7.ISAMRSproject.repository.CottageOwnerRepository;
import tim7.ISAMRSproject.repository.CottageRepository;
import tim7.ISAMRSproject.repository.DeletionRequestRepository;
import tim7.ISAMRSproject.repository.InstructorRepository;
import tim7.ISAMRSproject.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private BoatOwnerRepository boatOwnerRepository;

	@Autowired
	private CottageOwnerRepository cottageOwnerRepository;
	
	@Autowired
	private InstructorRepository instructorRepository;
	
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private DeletionRequestRepository deletionRequestRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CottageRepository cottageRepository;
	
	@Autowired
	private BoatRepository boatRepository;
	
	@Autowired
	private AdventureRepository adventureRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Transactional
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}
	
	public List<User> findAll(){
		
		return userRepository.findAll();
	}
	
	public void deleteUserById(int id) {
		this.userRepository.deleteById(id);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with email '%s'.", username));
		} else {
			return user;
		}
	}
	
	public User findByEmail(String email) throws AccessDeniedException{
		return userRepository.findByEmail(email);
	}
	
	public User save(UserRegisterDTO userRegisterDTO) {
		
		User newUser;
		if (userRegisterDTO.getRole().equals("ROLE_COTTAGE_OWNER")) {
			newUser = new CottageOwner();
		} else if (userRegisterDTO.getRole().equals("ROLE_BOAT_OWNER")) {
			newUser = new BoatOwner();
		} else if (userRegisterDTO.getRole().equals("ROLE_INSTRUCTOR")) {
			newUser = new FishingInstructor();
		} else newUser = new User();
		
		Address address = new Address();
		
		address.setCountry(userRegisterDTO.getCountry());
		address.setCity(userRegisterDTO.getTown());
		address.setStreet(userRegisterDTO.getAddress());
		address.setLatitude("0");
		address.setLongitude("0");
		
		newUser.setEmail(userRegisterDTO.getEmail());
		newUser.setName(userRegisterDTO.getName());
		newUser.setLastName(userRegisterDTO.getLastName());
		newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
		newUser.setPhone(userRegisterDTO.getPhoneNumber());
		newUser.setAddress(address);
		newUser.setDeleted(false);
		newUser.setActive(false);
		List<Role> roles = new ArrayList<Role>();
		RegistrationRequest rr = new RegistrationRequest();
		if (!userRegisterDTO.getRole().equals("ROLE_USER")) {
			roles.add(roleService.findByName(userRegisterDTO.getRole()));
			rr.setRegistrationReason(userRegisterDTO.getReason());
			rr.setRequestStatus(RegistrationRequestStatus.PENDING);
			rr.setUser(newUser);
			newUser.setRegistrationRequest(rr);		
		} else {
			roles.add(roleService.findByName("ROLE_USER"));
		}
		newUser.setRoles(roles);
		switch(userRegisterDTO.getRole()) {
			case("ROLE_USER"):
				User u = (User)this.clientRepository.save(new Client(newUser));
				return u;
			case("ROLE_BOAT_OWNER"):
				return (User)(this.boatOwnerRepository.save((BoatOwner)newUser));
			case("ROLE_COTTAGE_OWNER"):
				return (User)(this.cottageOwnerRepository.save((CottageOwner)newUser));
			case("ROLE_INSTRUCTOR"):
				return (User)(this.instructorRepository.save((FishingInstructor)(newUser)));
		}
		return null;
	}
	
	public void updateUser(User user, UserRegisterDTO dto) {
		if (!(user.getName().equals(dto.getName())))
			user.setName(dto.getName());
		if (!(user.getLastName().equals(dto.getLastName())))
			user.setLastName(dto.getLastName());
		if (!(user.getPhone().equals(dto.getPhoneNumber())))
			user.setPhone(dto.getPhoneNumber());
		
		Address dtoAddress = new Address();
		dtoAddress.setCity(dto.getTown());
		dtoAddress.setCountry(dto.getCountry());
		dtoAddress.setStreet(dto.getAddress());
		
		if (!(user.getAddress().equals(dtoAddress))) {
			dtoAddress.setUser(user);
			user.setAddress(dtoAddress);			
		}
		
		this.userRepository.save(user);
		
	}
	
	public void changeUserPassword(User user, ChangePasswordDTO changePasswordDTO) {
		user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
		this.userRepository.save(user);
	}
	
	@Transactional
	public User save(User user) {
		return this.userRepository.save(user);
	}
	
	public DeletionRequest saveDeletionRequest(DeletionRequest deletionRequest) {
		return this.deletionRequestRepository.save(deletionRequest);
	}
	
	public User saveAdmin(UserRegisterDTO userRegisterDTO) {
		Admin admin = new Admin();		
		
		Address address = new Address();		
		address.setCountry(userRegisterDTO.getCountry());
		address.setCity(userRegisterDTO.getTown());
		address.setStreet(userRegisterDTO.getAddress());
		address.setLongitude("0");
		address.setLatitude("0");
		
		admin.setAddress(address);
		address.setUser(admin);
		
		//this.addressRepository.save(address);
		admin.setEmail(userRegisterDTO.getEmail());
		admin.setName(userRegisterDTO.getName());
		admin.setLastName(userRegisterDTO.getLastName());
		admin.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
		admin.setPhone(userRegisterDTO.getPhoneNumber());
		admin.setAddress(address);
		admin.setDeleted(false);
		admin.setActive(true);
		List<Role> roles = new ArrayList<Role>();
		roles.add(roleService.findByName("ROLE_ADMIN"));
		admin.setRoles(roles);
		
		//Admin admin = new Admin(newUser);
		admin.setFirstLogin(true);
		this.adminRepository.save(admin);
		
		return (User)admin;
	}

	public User findServiceProviderByOfferId(Integer id) {
		Integer userId = this.adventureRepository.getFishingInstrucorByOfferId(id);
		if (userId == null) {
			userId = this.boatRepository.getBoatOwnerByOfferId(id);
			if (userId == null) {
				userId = this.cottageRepository.getCottageOwnerByOfferId(id);
			}
		}
		Optional<User> user = userRepository.findById(userId);
		return user.get();
	}
	
	public List<Offer> getAllSubscriptions(User u){
		List<Offer> subs = new ArrayList<Offer>();
		for(Cottage c: cottageRepository.findBySubscribers_IdEquals(u.getId()))
			subs.add(c);
			
		for(Boat b: boatRepository.findBySubscribers_IdEquals(u.getId()))
			subs.add(b);
		
		for(Adventure a: adventureRepository.findByInstructorId(u.getId()))
			subs.add(a);
		return subs;
	}

	public boolean isUserOnlyClient(User requestUser){
		return !(requestUser.hasRole("ROLE_ADMIN") || requestUser.hasRole("ROLE_COTTAGE_OWNER")
				||requestUser.hasRole("ROLE_BOAT_OWNER")||requestUser.hasRole("ROLE_INSTRUCTOR")
				||requestUser.hasRole("ROLE_SYSADMIN"));
	}
}
