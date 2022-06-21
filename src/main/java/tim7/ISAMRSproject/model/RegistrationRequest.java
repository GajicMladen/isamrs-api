package tim7.ISAMRSproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class RegistrationRequest {
	public static enum RegistrationRequestStatus{
		PENDING,
		ACCEPTED,
		DECLINED
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;
    
    @Column(name= "registration_reason", nullable = false)
	private String RegistrationReason;
    
	@Column(name = "registration_request_status", nullable = false)
	private RegistrationRequestStatus requestStatus;
	
	@OneToOne(mappedBy = "registrationRequest", optional = true)
    private User user;
	
	@Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegistrationReason() {
		return RegistrationReason;
	}

	public void setRegistrationReason(String registrationReason) {
		RegistrationReason = registrationReason;
	}

	public RegistrationRequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RegistrationRequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	
	
	
}
