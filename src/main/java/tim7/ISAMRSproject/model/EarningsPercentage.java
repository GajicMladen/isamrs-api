package tim7.ISAMRSproject.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EarningsPercentage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
	@Column(name= "percentage", nullable = false)
	private int percentage;
	
	@Column(name="date_of_setting", nullable = false)
	private LocalDateTime dateOfSetting;
	
	public EarningsPercentage() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public LocalDateTime getDateOfSetting() {
		return dateOfSetting;
	}

	public void setDateOfSetting(LocalDateTime dateOfSetting) {
		this.dateOfSetting = dateOfSetting;
	}
}
