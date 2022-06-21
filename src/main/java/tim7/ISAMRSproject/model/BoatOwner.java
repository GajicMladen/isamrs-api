package tim7.ISAMRSproject.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class BoatOwner extends User {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "boatOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Boat> boats = new HashSet<Boat>();


	public BoatOwner() {
	}

	public BoatOwner(Integer id, String password, String email,String name, String lastName, String phone) {
		super(id, email, password, name, lastName, phone);
	}
	
	public BoatOwner(User user) {
		super(user);
	}

}
