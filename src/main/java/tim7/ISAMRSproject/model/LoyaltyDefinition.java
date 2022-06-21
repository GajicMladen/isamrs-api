package tim7.ISAMRSproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LoyaltyDefinition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "rank_name", nullable = false)
	private String rankName;
	
	@Column(name = "min_points", nullable = false)
	private Integer minPoints;
	
	@Column(name = "max_points", nullable = false)
	private Integer maxPoints;
	
	@Column(name = "discount_rate", nullable = false)
	private Float discountRate;
	
	@Column(name = "points_per_reservation", nullable = false)
	private int pointsPerReservation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public Integer getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Float getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Float discountRate) {
		this.discountRate = discountRate;
	}

	public int getPointsPerReservation() {
		return pointsPerReservation;
	}

	public void setPointsPerReservation(int pointsPerReservation) {
		this.pointsPerReservation = pointsPerReservation;
	}
	
	
	
}
