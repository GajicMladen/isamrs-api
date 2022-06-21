package tim7.ISAMRSproject.Student3Tests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.ISAMRSproject.model.ApprovalStatus;
import tim7.ISAMRSproject.model.Complaint;
import tim7.ISAMRSproject.model.Grade;
import tim7.ISAMRSproject.service.ComplaintService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplaintTest {
	
	@Autowired
	private ComplaintService complaintService;
	
	@Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testOptimisticLockingScenario() throws Throwable {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Startovan Thread 1");
                Complaint complaint = complaintService.getComplaintById(1).get();
                complaint.setStatus(ApprovalStatus.ACCEPT);
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                complaintService.save(complaint);;

            }
        });


        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread 2 start");
                Complaint complaint = complaintService.getComplaintById(1).get();
                complaint.setStatus(ApprovalStatus.DENIED);
                complaintService.save(complaint);
            }
        });

        try {
            future1.get(); // podize ExecutionException za bilo koji izuzetak iz prvog child threada
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas ObjectOptimisticLockingFailureException
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
