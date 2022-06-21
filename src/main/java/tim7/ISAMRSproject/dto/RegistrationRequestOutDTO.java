package tim7.ISAMRSproject.dto;

public class RegistrationRequestOutDTO {
	
	private String registrationReason;	
	private String userId;	
	private String name;
	private String lastName;
	
	public RegistrationRequestOutDTO() {
		
	}

	public String getRegistrationReason() {
		return registrationReason;
	}

	public void setRegistrationReason(String registrationReason) {
		this.registrationReason = registrationReason;
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
