package tim7.ISAMRSproject.dto;

public class SubscriptionDTO {
	
	private int id;
	private String offerName;
	private String offerAddress;
	private String ownerName;
	private boolean isInstructor;
	private float price;
	private boolean subscribed;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getOfferAddress() {
		return offerAddress;
	}
	public void setOfferAddress(String offerAddress) {
		this.offerAddress = offerAddress;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public boolean isInstructor() {
		return isInstructor;
	}
	public void setInstructor(boolean isInstructor) {
		this.isInstructor = isInstructor;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public boolean isSubscribed() {
		return subscribed;
	}
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	
	
	
}
