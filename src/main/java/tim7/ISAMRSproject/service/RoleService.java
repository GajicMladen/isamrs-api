package tim7.ISAMRSproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.model.Role;
import tim7.ISAMRSproject.repository.RoleRepository;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @SuppressWarnings("deprecation")
  public Role findById(Long id) {
    Role auth = this.roleRepository.getOne(id);
    return auth;
  }

  public Role findByName(String name) {
	Role roles = this.roleRepository.findByName(name);
    return roles;
  } 

}
