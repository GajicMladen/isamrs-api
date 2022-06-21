package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.Admin;
import tim7.ISAMRSproject.model.User;

public class AdminDTO {
	private Integer id;
	private String name;
	private String lastName;
	private String username;
	private String email;
	private String phone;
	private String street;
	private String city;
	private String country;
	private String longitude;
	private String latitude;
	private boolean isFirstLogin;
	
	public AdminDTO() {
		
	}
	
	public AdminDTO(Admin admin) {
		this.id = admin.getId();
		this.name = admin.getName();
		this.lastName = admin.getLastName();
		this.username = admin.getUsername();
		this.email = admin.getEmail();
		this.phone = admin.getPhone();
		this.street = admin.getAddress().getStreet();
		this.city = admin.getAddress().getCity();
		this.country = admin.getAddress().getCountry();
		this.latitude = admin.getAddress().getLatitude();
		this.longitude = admin.getAddress().getLongitude();
		this.isFirstLogin = admin.isFirstLogin();
	}

	public AdminDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.lastName = user.getLastName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.street = user.getAddress().getStreet();
		this.city = user.getAddress().getCity();
		this.country = user.getAddress().getCountry();
		this.latitude = user.getAddress().getLatitude();
		this.longitude = user.getAddress().getLongitude();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	
	
}
