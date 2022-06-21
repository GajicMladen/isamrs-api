package tim7.ISAMRSproject.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cottage_owners")
public class CottageOwner extends User {

	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "cottageOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Cottage> cottages = new HashSet<Cottage>();

	public CottageOwner(Integer id, String password, String email,String name, String lastName, String phone) {
		super(id, email, password, name, lastName, phone);
	}
	
	public CottageOwner() {
	}
	
	public CottageOwner(User user) {
		super(user);
	}
}
