package tim7.ISAMRSproject.model;


import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonIgnore
	@Column(name = "password",nullable = false)
	private String password;

	@Column(name="email",nullable = false)
	private String email;

	@Column(name ="name",nullable = false)
	private String name;
	
	@Column(name = "lastname",nullable = false)
	private String lastName;
	
	@Column(name = "phone",nullable = false)
	private String phone;

	@Column(name = "deleted",nullable = false)
	private boolean deleted;
	
	@Column(name = "active",nullable = false)
	private boolean active;
	
	@Column(name = "loyalty_points", nullable = false)
	private int loyaltyPoints;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "deletion_request", referencedColumnName = "id")
	private DeletionRequest deletionRequest;
	
	@JsonIgnore
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "registration_request", referencedColumnName = "id")
	private RegistrationRequest registrationRequest;
	
	@JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address livingAddress;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    protected List<Role> roles;
    
    @Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;
    
    public User() {
    	
    }
	
	public User(Integer id, String password, String email, String name, String lastName, String phone) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.active = false;
		this.deleted = false;
		this.loyaltyPoints = 0;
	}
	
	public User(Integer id, String password, String email, String name, String lastName, String phone, int points) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.active = false;
		this.deleted = false;
		this.loyaltyPoints = points;
	}
	
	public User(User user) {
		super();
		this.id = user.id;
		this.password = user.password;
		this.email = user.email;
		this.name = user.name;
		this.lastName = user.lastName;
		this.phone = user.phone;
		this.active = user.active;
		this.deleted = user.deleted;
		this.loyaltyPoints = user.loyaltyPoints;
		this.livingAddress = user.livingAddress;
		this.roles = user.roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getLoyaltyPoints() {
		return this.loyaltyPoints;
	}
	
	public void setLoyaltyPoints(int points) {
		this.loyaltyPoints = points;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@JsonIgnore
	public Address getAddress() {
		return livingAddress;
	}
	
	public void setAddress(Address address) {
		this.livingAddress = address;
	}
	
	@JsonIgnore
	public DeletionRequest getDeletionRequest() {
		return deletionRequest;
	}
	
	public void setDeletionRequest(DeletionRequest deletionRequest) {
		this.deletionRequest = deletionRequest;
	}
	
	@JsonIgnore
    public RegistrationRequest getRegistrationRequest() {
		return registrationRequest;
	}

	public void setRegistrationRequest(RegistrationRequest registrationRequest) {
		this.registrationRequest = registrationRequest;
	}

	public List<Role> getRoles() {
        return roles;
     }
    
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public String getRoleString() {
    return this.roles.get(0).getName();
  }
    
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return this.roles;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public boolean hasRole(String roleName){
		for (Role role: roles) {
			if(role.getName().equals(roleName))
				return true;
		}
		return false;
	}
}
