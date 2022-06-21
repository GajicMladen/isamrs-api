package tim7.ISAMRSproject.dto;

import java.time.LocalDateTime;

public class DateRangeDTO {
	private LocalDateTime start;
	private LocalDateTime end;
	
	public DateRangeDTO() {
		
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	
	
}
