package tim7.ISAMRSproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import tim7.ISAMRSproject.dto.UserRegisterDTO;

@Entity
public class Admin extends User {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "isFirstLogin",nullable = false)
	private boolean isFirstLogin;

	public Admin() {
	}

	public Admin(Integer id, String password, String email,String name, String lastName, String phone) {
		super(id, email, password, name, lastName, phone);
	}
	
	public Admin(UserRegisterDTO dto) {
		super(dto.getId().intValue(), dto.getEmail(), dto.getPassword(), dto.getName(), dto.getLastName(), dto.getPhoneNumber());
	}
	
	public Admin(User user) {
		super(user);
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	
	

}
