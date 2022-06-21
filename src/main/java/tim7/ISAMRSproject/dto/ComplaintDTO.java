package tim7.ISAMRSproject.dto;

import tim7.ISAMRSproject.model.ApprovalStatus;
import tim7.ISAMRSproject.model.Complaint;

public class ComplaintDTO {

    private Integer id;
    private String text;
    private ApprovalStatus approvalStatus;
    private Integer reservationId;
    private Integer offenderId;
    private boolean forOffer;
    private boolean fromOwner;

    private boolean punishOffender;

    public ComplaintDTO(){}

    public ComplaintDTO(Complaint complaint){

        this.id = complaint.getId();
        this.approvalStatus = complaint.getStatus();
        this.forOffer = complaint.isForOffer();
        this.offenderId = complaint.getOffenderId();
        this.text = complaint.getText();
        this.reservationId = complaint.getReservation().getId();
        this.fromOwner = complaint.isFormOwner();
        this.punishOffender = complaint.isPunishOffender();
    }

    public boolean isPunishOffender() {
        return punishOffender;
    }

    public void setPunishOffender(boolean punishOffender) {
        this.punishOffender = punishOffender;
    }

    public boolean isFromOwner() {
        return fromOwner;
    }

    public void setFromOwner(boolean fromOwner) {
        this.fromOwner = fromOwner;
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

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getOffenderId() {
        return offenderId;
    }

    public void setOffenderId(Integer offenderId) {
        this.offenderId = offenderId;
    }

    public boolean isForOffer() {
        return forOffer;
    }

    public void setForOffer(boolean forOffer) {
        this.forOffer = forOffer;
    }
}
