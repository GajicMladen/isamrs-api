package tim7.ISAMRSproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.model.Address;
import tim7.ISAMRSproject.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	public Address save(Address address){
		return this.addressRepository.save(address);
	}
	
	public Optional<Address> findById(Long id) {
		return this.addressRepository.findById(id);
	}
}
