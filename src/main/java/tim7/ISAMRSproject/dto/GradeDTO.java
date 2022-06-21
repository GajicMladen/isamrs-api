package tim7.ISAMRSproject.dto;

public class GradeDTO {
	private int id;
	private float grade;
	private String reviewText;
	
	public float getGrade() {
		return grade;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	

}
