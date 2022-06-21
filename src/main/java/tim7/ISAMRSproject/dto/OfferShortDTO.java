package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.Adventure;
import tim7.ISAMRSproject.model.Boat;
import tim7.ISAMRSproject.model.Cottage;

public class OfferShortDTO {
	
	private Integer id;
	private String name;
	private String description;
	private float price;
	private String ownerName;
	private String address;
	private float rating;
	
	public OfferShortDTO(Cottage cottage) {
		this.id = cottage.getId();
		this.name = cottage.getName();
		this.description = cottage.getPromoDescription();
		this.price = cottage.getPrice();
		this.ownerName = cottage.getCottageOwner().getName() + " " + cottage.getCottageOwner().getLastName();
		this.address = cottage.getAddress().getStreet() + ", " + cottage.getAddress().getCity();
		this.rating = cottage.getRating();
	}
	
	public OfferShortDTO(Boat boat) {
		this.id = boat.getId();
		this.name = boat.getName();
		this.description = boat.getPromoDescription();
		this.price = boat.getPrice();
		this.ownerName = boat.getBoatOwner().getName() + " " + boat.getBoatOwner().getLastName();
		this.address = boat.getAddress().getStreet() + ", " + boat.getAddress().getCity();
		this.rating = boat.getRating();
	}
	
	public OfferShortDTO(Adventure adventure) {
		this.id = adventure.getId();
		this.name = adventure.getName();
		this.description = adventure.getPromoDescription();
		this.price = adventure.getPrice();
		this.ownerName = adventure.getFishingInstructor().getName() + " " + adventure.getFishingInstructor().getLastName();
		this.address = adventure.getAddress().getStreet() + ", " + adventure.getAddress().getCity();
		this.rating = adventure.getRating();
	}
	
	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
