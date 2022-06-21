package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.User;

public class UserDTO {

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

	private String userRole;
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public UserDTO() {
		
	}
	
	public UserDTO(User user) {
		
		this(user.getId(), user.getName(), user.getLastName(), user.getUsername(),user.getEmail(),user.getPhone(), user.getAddress().getStreet(),

			 user.getAddress().getCity(), user.getAddress().getCountry(), user.getAddress().getLongitude(), user.getAddress().getLatitude(), user.getRoles().toString());
	}
	
	public UserDTO(Integer id, String name, String lastName, String username,String email,String phone, String street, String city, String country,
			       String longitude, String latitude, String role) {

		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.street = street;
		this.city = city;
		this.country = country;
		this.longitude = longitude;
		this.latitude = latitude;

		this.userRole = role;
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
	
	
	
	
}
