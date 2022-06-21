package tim7.ISAMRSproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tim7.ISAMRSproject.model.Client;
import tim7.ISAMRSproject.service.ClientService;

@Component
public class ScheduledTasks {
	
	@Autowired
	private ClientService clientService;

	@Scheduled(cron = "0 0 1 * * ?")
	public void resetPenalties() {
	    for(Client c: this.clientService.findAll()) {
	    	c.setPenalCount(0);
	    	clientService.save(c);
	    }
	}
}
