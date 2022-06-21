package tim7.ISAMRSproject.dto;

public class ChangePasswordDTO {
	
	private String newPassword;
	private String confirmNewPassword;
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	
	public boolean validate() {
		return newPassword.length() >= 8 && newPassword.length() <= 30 && newPassword.equals(confirmNewPassword);
	}
	
}
