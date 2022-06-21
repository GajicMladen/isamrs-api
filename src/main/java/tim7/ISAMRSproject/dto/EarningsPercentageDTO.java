package tim7.ISAMRSproject.dto;

import java.time.LocalDateTime;

import tim7.ISAMRSproject.model.EarningsPercentage;

public class EarningsPercentageDTO {
	private Long id;
	private int percentage;
	private LocalDateTime settingDate;
	
	public EarningsPercentageDTO() {
		
	}
	
	public EarningsPercentageDTO(EarningsPercentage ep) {
		this.id = ep.getId();
		this.percentage = ep.getPercentage();
		this.settingDate = ep.getDateOfSetting();
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

	public LocalDateTime getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(LocalDateTime settingDate) {
		this.settingDate = settingDate;
	}
	
	
	
}
