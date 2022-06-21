package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.Adventure;

public class AdventureDTO {
	
	private Integer id;
	private String name;
	private String promoDescription;
	private float price;
	private int capacity;
	private String equipment;
	private String rulesOfConduct;
	private String rulesOfCancelation;
	private String moreInfo;
	private String street;
	private String city;
	private String country;
	private String latitude;
	private String longitude;
	private String instructorName;
	private String instructorSurname;
	private Integer instructorId;
	private String instructorBiography;
	private float rating; 
	private boolean deleted;
	

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getRulesOfConduct() {
		return rulesOfConduct;
	}

	public void setRulesOfConduct(String rulesOfConduct) {
		this.rulesOfConduct = rulesOfConduct;
	}

	public String getRulesOfCancelation() {
		return rulesOfCancelation;
	}

	public void setRulesOfCancelation(String rulesOfCancelation) {
		this.rulesOfCancelation = rulesOfCancelation;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public AdventureDTO() {
	
	}
	
	public AdventureDTO(Adventure adventure) {
		this.id = adventure.getId();
		this.name = adventure.getName();
		this.promoDescription = adventure.getPromoDescription();
		this.price = adventure.getPrice();
		this.capacity = adventure.getCapacity();
		this.instructorId = adventure.getInstructorId();
		this.instructorBiography = adventure.getInstructorBiography();
		this.equipment = adventure.getEquipment();
		this.moreInfo = adventure.getMoreInfo();
		this.rulesOfCancelation = adventure.getRulesOfCancelation();
		this.rulesOfConduct = adventure.getRulesOfConduct();
		this.street = adventure.getAddress().getStreet();
		this.city = adventure.getAddress().getCity();
		this.country = adventure.getAddress().getCountry();
		this.latitude = adventure.getAddress().getLatitude();
		this.longitude = adventure.getAddress().getLongitude();
		this.instructorName = adventure.getFishingInstructor().getName();
		this.instructorSurname = adventure.getFishingInstructor().getLastName();
		this.instructorId = adventure.getFishingInstructor().getId();
		this.deleted = adventure.isDeleted();
		this.rating = adventure.getRating();
		
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

	public String getPromoDescription() {
		return promoDescription;
	}

	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Integer getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(Integer instructorId) {
		this.instructorId = instructorId;
	}

	public String getInstructorBiography() {
		return instructorBiography;
	}

	public void setInstructorBiography(String instructorBiography) {
		this.instructorBiography = instructorBiography;
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getInstructorSurname() {
		return instructorSurname;
	}

	public void setInstructorSurname(String instructorSurname) {
		this.instructorSurname = instructorSurname;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
	
	
	
}
