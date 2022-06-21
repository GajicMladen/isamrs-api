package tim7.ISAMRSproject.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import tim7.ISAMRSproject.dto.AdventureDTO;

@Entity
public class Adventure extends Offer {

	@Column(nullable = false)
	private boolean deleted;
	
	@Column(nullable =  false, columnDefinition = "TEXT")
	private String instructorBiography;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String rulesOfConduct;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String equipment;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String moreInfo;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String rulesOfCancelation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instruktor_id")
	private FishingInstructor fishingInstructor;
	
	public Adventure() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Adventure(Integer id, String name, String promoDescription, float price, int capacity) {
		super(id, name, promoDescription, price, capacity);
	}

	public Adventure(Integer id, String name, String promoDescription, List<String> slike, String rulesOfConduct, String equipment,
					 String moreInfo, String rulesOfCancelation, float price, int capacity) {
		super(id, name, promoDescription, price, capacity);
		this.rulesOfConduct = rulesOfConduct;
		this.equipment = equipment;
		this.moreInfo = moreInfo;
		this.rulesOfCancelation = rulesOfCancelation;
	}
	
	public Adventure(AdventureDTO dto, User fishingInstructor) {
		super(10, dto.getName(), dto.getPromoDescription(), dto.getPrice(), dto.getCapacity());
		this.fishingInstructor = (FishingInstructor) fishingInstructor;
		this.instructorBiography = dto.getInstructorBiography();
	}
	
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getRulesOfConduct() {
		return rulesOfConduct;
	}

	public void setRulesOfConduct(String rulesOfConduct) {
		this.rulesOfConduct = rulesOfConduct;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public String getRulesOfCancelation() {
		return rulesOfCancelation;
	}

	public void setRulesOfCancelation(String rulesOfCancelation) {
		this.rulesOfCancelation = rulesOfCancelation;
	}

	public FishingInstructor getFishingInstructor() {
		return fishingInstructor;
	}

	public void setFishingInstructor(FishingInstructor fishingInstructor) {
		this.fishingInstructor = fishingInstructor;
	}

	public Adventure(String instructorBiografy) {
		super();
		this.instructorBiography = instructorBiografy;
	}

	public String getInstructorBiography() {
		return instructorBiography;
	}

	public void setInstructorBiography(String instructorBiography) {
		this.instructorBiography = instructorBiography;
	}
	
	public Integer getInstructorId() {
		return this.fishingInstructor.getId();
	}
	
	
}
