package tim7.ISAMRSproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class Grade {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

	@Column(name = "grade",nullable = false)
	private float grade;
	
	@Column(name= "revision" ,nullable = false)
	private String revision;
	
	@Column(name= "status",nullable = false)
	private ApprovalStatus status;

	@OneToOne(mappedBy = "grade")
    private Reservation reservation;
	
	@Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;

	public Grade(){}

	public Grade(float grade) {
		this.grade = grade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float f) {
		this.grade = f;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
