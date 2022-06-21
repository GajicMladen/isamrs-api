package tim7.ISAMRSproject.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Offer {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;


	@Version
	@Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version;

	@Column(name = "is_changing",columnDefinition = "boolean DEFAULT false",nullable = false)
	private boolean isChanging;


	@Column(name = "name",unique = true)
	private String name;

	@Column(name = "promoDescription",columnDefinition = "TEXT")
	private String promoDescription;
	/*
	@Column(name = "slike")
	private List<String> slike;
	
	@Column(name = "pravilaPonasanja")
	private List<String> pravilaPonasanja;
	*/
	
	@Column(name = "price")
	private float price;
	
	@Column(name = "capacity")
	private int capacity;
	
	@Column(name = "rating")
	private float rating;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address" ,referencedColumnName = "id")
	private Address address;

	@Column(name = "extra_favors" ,nullable = true, columnDefinition = "TEXT")
	private String extraFavors;

	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<FreePeriod> freePeriods = new HashSet<FreePeriod>();
	
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Reservation> reservations = new HashSet<Reservation>();
	
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Action> actions = new HashSet<Action>();

	@ManyToMany(mappedBy = "subscribedOffers")
	private Set<Client> subscribers = new HashSet<Client>();

	public boolean isChanging() {
		return isChanging;
	}

	public void setChanging(boolean changing) {
		isChanging = changing;
	}

	public Offer() {
		super();
	}

	public Offer(Integer id, String name, String promoDescription, float price, int capacity,String extraFavors) {
		super();
		this.id = id;
		this.name = name;
		this.promoDescription = promoDescription;
//		this.slike = slike;
//		this.pravilaPonasanja = pravilaPonasanja;
		this.price = price;
		this.capacity = capacity;
		this.extraFavors = extraFavors;
	}

	public Offer(Integer id, String name, String promoDescription, float price, int capacity) {
		super();
		this.id = id;
		this.name = name;
		this.promoDescription = promoDescription;
//		this.slike = slike;
//		this.pravilaPonasanja = pravilaPonasanja;
		this.price = price;
		this.capacity = capacity;
	}
	public Offer(Offer offer) {
		super();
		this.id = offer.id;
		this.name = offer.name;
		this.promoDescription = offer.promoDescription;
//		this.slike = offer.slike;
//		this.pravilaPonasanja = offer.pravilaPonasanja;
		this.price = offer.price;
		this.capacity = offer.capacity;
		this.extraFavors = offer.extraFavors;
	}
	
	public Offer(Integer id, String name, String promoDescription, Object slike, Object pravilaPonasanja, float price,
				 int capacity) {
		super();
		this.id = id;
		this.name = name;
		this.promoDescription = promoDescription;
//		this.slike = slike;
//		this.pravilaPonasanja = pravilaPonasanja;
		this.price = price;
		this.capacity = capacity;
	}

	public String getExtraFavors() {
		return extraFavors;
	}

	public void setExtraFavors(String extraFavors) {
		this.extraFavors = extraFavors;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<FreePeriod> getFreePeriods() {
		return freePeriods;
	}

	public void setFreePeriods(Set<FreePeriod> freePeriods) {
		this.freePeriods = freePeriods;
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
	public String getPromoDescription() {
		return promoDescription;
	}
	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}
	/*
	public List<String> getSlike() {
		return slike;
	}
	public void setSlike(List<String> slike) {
		this.slike = slike;
	}
	public List<String> getPravilaPonasanja() {
		return pravilaPonasanja;
	}
	public void setPravilaPonasanja(List<String> pravilaPonasanja) {
		this.pravilaPonasanja = pravilaPonasanja;
	}
	*/
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
	
	public float getRating() {
		return this.rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	/*
	
	private float izracunajProsecnuOcena() {
		
	}
	 
	 */


}
