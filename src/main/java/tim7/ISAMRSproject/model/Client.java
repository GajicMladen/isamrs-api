package tim7.ISAMRSproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Client extends User {
	
	private static final long serialVersionUID = 1L;

	@Column(name="penalCount")
	private int penalCount;
	
	@Column(name = "suspended")
	private boolean suspended;
	

	@OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Reservation> reservations = new HashSet<Reservation>();
	

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="subscribers",
			joinColumns = @JoinColumn(name = "client_id"),
			inverseJoinColumns = @JoinColumn(name = "offer_id")
	)
	private Set<Offer> subscribedOffers = new HashSet<Offer>();
/*
	@OneToMany(mappedBy = "klijent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Complaint> zalbae = new HashSet<Complaint>();
*/

	public Client() {
	}

	public Client(Integer id, String password, String email,String name, String lastName, String phone) {
		super(id, email, password, name, lastName, phone);
		
	}
	
	public Client(User u) {
		super(u);
		this.suspended = false;
		this.penalCount = 0;
	}

	public int getPenalCount() {
		return penalCount;
	}

	public void setPenalCount(int penalCount) {
		this.penalCount = penalCount;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<Offer> getSubscribedOffers() {
		return subscribedOffers;
	}

	public void setSubscribedOffers(Set<Offer> subscribedOffers) {
		this.subscribedOffers = subscribedOffers;
	}

	public void addSubscribedOffer(Offer offer){
		this.subscribedOffers.add(offer);
	}

	public void removeSubscribedOffer(Offer offer){
		for (Offer offerr :subscribedOffers) {
			if(offerr.getId() == offerr.getId())
			{
				subscribedOffers.remove(offerr);
				return;
			}
		}
	}

	public boolean isSubscribedToOffer(int offerId){
		for (Offer offer:subscribedOffers ) {
			if(offer.getId().equals(offerId))
				return true;
		}
		return false;
	}
}
