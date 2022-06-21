package tim7.ISAMRSproject.dto;

public class DeletionRequestDTO {

	String deletionReason;
	
	public DeletionRequestDTO() {
		
	}
	
	public DeletionRequestDTO(String reason) {
		this.deletionReason = reason;
	}

	public String getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(String deletionReason) {
		this.deletionReason = deletionReason;
	}
}
