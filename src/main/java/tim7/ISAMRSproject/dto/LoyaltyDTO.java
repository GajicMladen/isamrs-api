package tim7.ISAMRSproject.dto;

public class LoyaltyDTO {

	int id;
	String rankName;
	int minPoints;
	int maxPoints;
	float discountRate;
	int pointsPerReservation;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPointsPerReservation() {
		return pointsPerReservation;
	}
	public void setPointsPerReservation(int pointsPerReservation) {
		this.pointsPerReservation = pointsPerReservation;
	}
	
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public int getMinPoints() {
		return minPoints;
	}
	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}
	public int getMaxPoints() {
		return maxPoints;
	}
	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}
	public float getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(float discountRate) {
		this.discountRate = discountRate;
	}
	
	
}
