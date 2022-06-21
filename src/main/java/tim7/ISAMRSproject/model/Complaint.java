package tim7.ISAMRSproject.model;

import tim7.ISAMRSproject.dto.ComplaintDTO;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Complaint {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "text",nullable = false)
    private String text;
    
    @Column(name = "status",nullable = false)
    private ApprovalStatus status;
    
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    // flag - da li salje korisnik ili vlasnik
    // za klijenta -> true
    @Column(name="for_offer", nullable = false)
    private boolean forOffer;

    // na koga se odnosi zalba
    // Moze biti i od offera.. Kum
    // za klijenta -> id ponude
    @Column(name="offender_id", nullable = false)
    private Integer offenderId;

    // flag - da li salje korisnik ili vlasnik
    // za klijenta -> false
    @Column(name="from_owner", nullable = false)
    private boolean formOwner;
    
    // za klijenta -> false
    @Column(name="pusnih_offender", nullable = false)
    private boolean punishOffender;
    public Complaint(){}
    
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;

    public Complaint(String text) {
        this.text = text;
    }

    public Complaint(ComplaintDTO complaintDTO){

        this.id = complaintDTO.getId();
        this.formOwner = complaintDTO.isFromOwner();
        this.status = complaintDTO.getApprovalStatus();
        this.forOffer = complaintDTO.isForOffer();
        this.offenderId = complaintDTO.getOffenderId();
        this.text = complaintDTO.getText();
        this.punishOffender = complaintDTO.isPunishOffender();

    }

    public boolean isPunishOffender() {
        return punishOffender;
    }

    public void setPunishOffender(boolean punishOffender) {
        this.punishOffender = punishOffender;
    }

    public boolean isFormOwner() {
        return formOwner;
    }

    public void setFormOwner(boolean formOwner) {
        this.formOwner = formOwner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public boolean isForOffer() {
        return forOffer;
    }

    public void setForOffer(boolean forOffer) {
        this.forOffer = forOffer;
    }

    public Integer getOffenderId() {
        return offenderId;
    }

    public void setOffenderId(Integer offenderId) {
        this.offenderId = offenderId;
    }

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
    
    
}
