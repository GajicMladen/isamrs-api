package tim7.ISAMRSproject.dto;

import java.time.LocalDateTime;

public class ReservationListItemDTO {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer offerId;
    private Integer id;
    private Integer clientId;
    private float totalPrice;
    private boolean canCancel;
    private String offerName;
    private String offerAddress;
    private boolean canComplain;
    
	public boolean isCanComplain() {
		return canComplain;
	}
	public void setCanComplain(boolean canComplain) {
		this.canComplain = canComplain;
	}
	public String getOfferAddress() {
		return offerAddress;
	}
	public void setOfferAddress(String offerAddress) {
		this.offerAddress = offerAddress;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
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
	public Integer getOfferId() {
		return offerId;
	}
	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public boolean isCanCancel() {
		return canCancel;
	}
	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}
    
    
}
