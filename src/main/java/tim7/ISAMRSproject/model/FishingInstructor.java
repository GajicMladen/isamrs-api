package tim7.ISAMRSproject.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class FishingInstructor extends User {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "fishingInstructor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Adventure> adventures = new HashSet<Adventure>();


	public FishingInstructor() {
	}


	public FishingInstructor(Integer id, String password, String email,String name, String lastName, String phone) {
		super(id, email, password, name, lastName, phone);
	}

	public FishingInstructor(User user) {
		super(user);
	}
}
