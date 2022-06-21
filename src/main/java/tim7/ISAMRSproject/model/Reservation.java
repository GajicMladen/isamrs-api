package tim7.ISAMRSproject.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
    private Integer id;

	@Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;

	@Column(name = "startDateTime",nullable = false)
	private LocalDateTime startDateTime;

	@Column(name = "endDateTime",nullable = false)
	private LocalDateTime endDateTime;

	@Column(name = "totalPrice",nullable = false)
	private float totalPrice;
	
	@Column(name = "status" , nullable = false)
	private ReservationStatus status;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER ,cascade = CascadeType.MERGE)
	@JoinColumn(name = "client_id",nullable = true)
	private Client client;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
	@JoinColumn(name = "offer_id",nullable = false)
	private Offer offer;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "grade", referencedColumnName = "id",nullable = true)
	private Grade grade;

	@JsonIgnore
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Complaint> complaints;

	public Reservation(){

	}
	public Reservation(Integer id, LocalDateTime startDateTime, LocalDateTime endDateTime, float totalPrice, ReservationStatus status, Client client, Offer offer, Grade grade, Set<Complaint> complaints) {
		this.id = id;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.totalPrice = totalPrice;
		this.status = status;
		this.client = client;
		this.offer = offer;
		this.grade = grade;
		this.complaints = complaints;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Set<Complaint> getComplaints() {
		return complaints;
	}

	public void setComplaints(Set<Complaint> complaints) {
		this.complaints = complaints;
	}
}
