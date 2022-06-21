package tim7.ISAMRSproject.Student1Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.ClientRepository;
import tim7.ISAMRSproject.repository.ReservationRepository;
import tim7.ISAMRSproject.service.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationTests {


    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CottageService cottageService;
    @Autowired
    private ClientService clientService;
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testMultipleReservationsFromOwnerAndClientAtSametime() throws Throwable {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Startovan Thread 1");
                Client client = clientService.findClientById(9);
                Cottage cottage = cottageService.getCottageById(1).get();
                String startDate = "18-6-2022";
                String endDate = "19-6-2022";

                Reservation newRes = reservationService.createReservationFromData(startDate,endDate,cottage,1500,"cottage",client);
                reservationService.reserveFreePeriods(newRes);
                reservationService.saveReservation(newRes,client,10);
                System.out.println(newRes.getTotalPrice());
            }
        });


        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread 2 start");
                Client client = clientService.findClientById(10);
                Cottage cottage = cottageService.getCottageById(1).get();
                String startDate = "18-6-2022";
                String endDate = "19-6-2022";

                Reservation newRes = reservationService.createReservationFromData(startDate,endDate,cottage,1500,"cottage",client);
                reservationService.reserveFreePeriods(newRes);
                reservationService.saveReservation(newRes,client,10);
                System.out.println(newRes.getTotalPrice());
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
