package tim7.ISAMRSproject.model;

import tim7.ISAMRSproject.dto.BoatDTO;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Boat extends Offer {

	@Column(name = "type",nullable = true)
	private String type;
	
	@Column(name = "length",nullable = false)
	private float length;
	
	@Column(name = "motorsCount",nullable = false)
	private int motorsCount;
	
	@Column(name = "motorPower",nullable = false)
	private float motorPower;
	
	@Column(name = "maxSpeed",nullable = false)
	private float maxSpeed;
	
	@Column(name ="cancelConditions",nullable = false)
	private String cancelConditions;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "boatOwner_id")
	private BoatOwner boatOwner;
	
	
	public Boat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Boat(BoatDTO boatDTO){
		super(boatDTO.getId(), boatDTO.getName(), boatDTO.getDescription(), boatDTO.getPrice(),
				boatDTO.getCapacity(), boatDTO.getExtraFavors());
		this.cancelConditions = boatDTO.getReservationCancellationTerms();
		this.length = boatDTO.getLength();
		this.maxSpeed = boatDTO.getMaxSpeed();
		this.motorPower = boatDTO.getPowerOfEngines();
		this.motorsCount = boatDTO.getNumOfMotors();
	}
	public Boat(Integer id, String naziv, String promoOpis, float cena,
				int kapacitet) {
		super(id, naziv, promoOpis, cena, kapacitet);
	}

	public Boat(Integer id, String naziv, String promoOpis, List<String> slike, List<String> pravilaPonasanja, float cena,
				int kapacitet, String type, float length, int motorsCount, float motorPower, float maxSpeed,
				String cancelConditions) {
		super(id, naziv, promoOpis, cena, kapacitet);
		this.type = type;
		this.length = length;
		this.motorsCount = motorsCount;
		this.motorPower = motorPower;
		this.maxSpeed = maxSpeed;
		this.cancelConditions = cancelConditions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getMotorsCount() {
		return motorsCount;
	}

	public void setMotorsCount(int motorsCount) {
		this.motorsCount = motorsCount;
	}

	public float getMotorPower() {
		return motorPower;
	}

	public void setMotorPower(float motorPower) {
		this.motorPower = motorPower;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getCancelConditions() {
		return cancelConditions;
	}

	public void setCancelConditions(String cancelConditions) {
		this.cancelConditions = cancelConditions;
	}

	public BoatOwner getBoatOwner() {
		return boatOwner;
	}

	public void setBoatOwner(BoatOwner boatOwner) {
		this.boatOwner = boatOwner;
	}
}
