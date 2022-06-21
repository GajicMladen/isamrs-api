package tim7.ISAMRSproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;


@Entity
public class DeletionRequest {
	
	public static enum DeletionRequestStatus{
		PENDING,
		ACCEPTED,
		DECLINED
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;
    
    @Column(name= "deletion_reason", nullable = false)
	private String deletionReason;
    
	@Column(name = "deletion_request_status", nullable = false)
	private DeletionRequestStatus requestStatus;
	
	@OneToOne(mappedBy = "deletionRequest", optional = true)
    private User user;
	
	@Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;
    
	
	public DeletionRequest() {
		
	}
	
	public DeletionRequest(String reason) {
		this.deletionReason = reason;
	}
	
	public String getDeletionReason() {
		return deletionReason;
	}
	public void setDeletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
	}
	public DeletionRequestStatus getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(DeletionRequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
