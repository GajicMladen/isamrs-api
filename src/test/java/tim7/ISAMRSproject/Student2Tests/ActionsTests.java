package tim7.ISAMRSproject.Student2Tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import tim7.ISAMRSproject.dto.ActionDTO;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.ClientRepository;
import tim7.ISAMRSproject.repository.CottageRepository;
import tim7.ISAMRSproject.repository.ReservationRepository;
import tim7.ISAMRSproject.service.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActionsTests {


    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private CottageService cottageService;

    @Autowired
    private ReservationServiceOwner reservationServiceOwner;

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    @Transactional
    public void testBuySameActionAtSametime() throws Throwable {



        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Startovan Thread 1");
                Client client = clientService.findClientById(9);
                Reservation res = reservationService.findById(1);
                reservationService.buyAction(client,res);
                try { Thread.sleep(3000); } catch (InterruptedException e) {}
                reservationService.saveReservation(res);

            }
        });


        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread 2 start");
                Client client = clientService.findClientById(10);
                Reservation res = reservationService.findById(1);
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                reservationService.buyAction(client,res);
                reservationService.saveReservation(res);
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
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    @Transactional
    public void testAddNewActionWhenClientReserveSameOffer() throws Throwable {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {

                Cottage cottage = cottageService.getCottageById(1).get();
                LocalDateTime startDate= LocalDateTime.now();
                LocalDateTime endDate = LocalDateTime.now().plusDays(7);
                float totalPrice =1000;
                try { Thread.sleep(1000); } catch (InterruptedException e) { }
                reservationServiceOwner.addNewActionCottage(startDate,endDate,totalPrice,cottage);
            }
        });


        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread 2 start");

                Cottage cottage = cottageService.getCottageById(1).get();
                Client client = clientService.findClientById(9);
                LocalDateTime startDate = LocalDateTime.now();
                LocalDateTime endDate = LocalDateTime.now().plusDays(7);
                Reservation newRes = reservationServiceOwner.reserveCottage(cottage,client,startDate,endDate);
                reservationServiceOwner.saveReservation(newRes);

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
    @Test(expected = ObjectOptimisticLockingFailureException.class)
    @Transactional
    public void testReserveOfferWhenOwnerDefinedAction() throws Throwable {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Startovan Thread 1");

                Cottage cottage = cottageService.getCottageById(1).get();
                Client client = clientService.findClientById(9);
                LocalDateTime startDate = LocalDateTime.now();
                LocalDateTime endDate = LocalDateTime.now().plusDays(7);
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                Reservation newRes = reservationServiceOwner.reserveCottage(cottage,client,startDate,endDate);
                reservationServiceOwner.saveReservation(newRes);

            }
        });


        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread 2 start");
                LocalDateTime startDate= LocalDateTime.now();
                LocalDateTime endDate = LocalDateTime.now().plusDays(7);
                float totalPrice =1000;
                Cottage cottage = cottageService.getCottageById(1).get();
                reservationServiceOwner.addNewActionCottage(startDate,endDate,totalPrice,cottage);
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
