package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.Cottage;

public class CottageDTO {

	private Integer id;
	private String name;
	private String description;
	private float price;
	private int capacity;

	private int bedCount;
	private int roomCount;

	private Integer ownerId;

	private String extraFavors;
	
	private float rating;
	
	public CottageDTO() {
		
		
	}
	
	public CottageDTO(Cottage cottage) {
		
		id = cottage.getId();
		name = cottage.getName();
		description = cottage.getPromoDescription();
		price = cottage.getPrice();
		capacity = cottage.getCapacity();
		ownerId = cottage.getCottageOwnerId();
		roomCount = cottage.getRoomCount();
		bedCount = cottage.getBedCount();
		extraFavors = cottage.getExtraFavors();
		rating = cottage.getRating();
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getExtraFavors() {
		return extraFavors;
	}

	public void setExtraFavors(String extraFavors) {
		this.extraFavors = extraFavors;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer idI) {
		this.id = idI;
	}

	public int getBedCount() {
		return bedCount;
	}

	public void setBedCount(int bedCount) {
		this.bedCount = bedCount;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public Integer getOwnerId() {
		return  ownerId;
	}
	public void setOwnerId(Integer idI) {
		this.ownerId = idI;
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
	public void setDescription(String promoDescription) {
		this.description = promoDescription;
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
	
	
}
