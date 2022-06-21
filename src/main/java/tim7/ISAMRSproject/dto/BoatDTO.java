package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.Boat;

public class BoatDTO {

    private Integer id;
    private String name;
    private String description;
    private float price;
    private int capacity;

    private String extraFavors;
    private Integer ownerId;
    private float length;
    private Integer numOfMotors;
    private float powerOfEngines;
    private float maxSpeed;
    private String reservationCancellationTerms;
    private float rating;

	public BoatDTO(){

    }

    public BoatDTO(Boat boat){
        id = boat.getId();
        name = boat.getName();
        description = boat.getPromoDescription();
        price = boat.getPrice();
        capacity = boat.getCapacity();
        ownerId = boat.getBoatOwner().getId();
        length = boat.getLength();
        numOfMotors = boat.getMotorsCount();
        powerOfEngines = boat.getMotorPower();
        maxSpeed = boat.getMaxSpeed();
        reservationCancellationTerms = boat.getCancelConditions();
        extraFavors = boat.getExtraFavors();
        rating = boat.getRating();
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Integer getNumOfMotors() {
        return numOfMotors;
    }

    public void setNumOfMotors(Integer numOfMotors) {
        this.numOfMotors = numOfMotors;
    }

    public float getPowerOfEngines() {
        return powerOfEngines;
    }

    public void setPowerOfEngines(float powerOfEngines) {
        this.powerOfEngines = powerOfEngines;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getReservationCancellationTerms() {
        return reservationCancellationTerms;
    }

    public void setReservationCancellationTerms(String reservationCancellationTerms) {
        this.reservationCancellationTerms = reservationCancellationTerms;
    }
}
