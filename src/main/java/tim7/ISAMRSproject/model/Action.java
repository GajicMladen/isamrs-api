package tim7.ISAMRSproject.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Column(name = "startDate",nullable = false)
	private LocalDateTime startDate;
	
	@Column(name = "endDate",nullable = false)
	private LocalDateTime endDate;

	@Column(name ="maxPerson",nullable = false)
	private int maxPerson;
	
	@Column(name ="price" , nullable = false)
	private float price;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	public Action() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public int getMaxPerson() {
		return maxPerson;
	}

	public void setMaxPerson(int maxPerson) {
		this.maxPerson = maxPerson;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float cena) {
		this.price = cena;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	
}
