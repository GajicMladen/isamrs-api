package tim7.ISAMRSproject.dto;

public class DeletionRequestOutDTO {
	
	String deletionReason;
	String userId;
	String name;
	String lastName;	

	public DeletionRequestOutDTO() {
		
	}
	
	public DeletionRequestOutDTO(String reason, String userId, String name, String lastName) {
		this.deletionReason = reason;
		this.userId = userId;
		this.name = name;
		this.lastName = lastName;
	}

	public String getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
}
