package tim7.ISAMRSproject.Unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import tim7.ISAMRSproject.model.*;
import tim7.ISAMRSproject.repository.*;
import tim7.ISAMRSproject.service.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTests {


    @Mock
    private CottageRepository cottageRepository;

    @InjectMocks
    private CottageService cottageService;

    @Mock
    private BoatRepository boatRepository;
    @InjectMocks
    private BoatService boatService;

    @Mock
    private AdventureRepository adventureRepository;
    @InjectMocks
    private AdventureService adventureService;

    @Test
    public void testFindByOwnerIdCottage() {
        // 1.
        when(cottageRepository.findByOwnerId(1)).thenReturn(Arrays.asList(new Cottage(1,"Villa Ellena",
                "Najlepsa villa na svetu",15000,10,3,4)));

        // 2.
        List<Cottage> cottages = cottageService.getCottagesByOwnerId(1);

        // 3.
        assertThat(cottages).hasSize(1);
        assertEquals(cottages.get(0).getName(), "Villa Ellena");

        verify(cottageRepository, times(1)).findByOwnerId(1);
        verifyNoMoreInteractions(cottageRepository);
    }

    @Test
    public void testFindByOwnerIdBoat() {
        // 1.
        when(boatRepository.findByOwnerId(1)).thenReturn(Arrays.asList(new Boat(1,"Brod u magli",
                "Najlepsi brod na svetu",15000,10)));

        // 2.
        List<Boat> boats =boatService.getBoatsByOwnerId(1);

        // 3.
        assertThat(boats).hasSize(1);
        assertEquals(boats.get(0).getName(), "Brod u magli");

        verify(boatRepository, times(1)).findByOwnerId(1);
        verifyNoMoreInteractions(boatRepository);
    }
    @Test
    public void testFindByOwnerIdAdventure() {
        // 1.
        when(adventureRepository.findByInstructorId(1)).thenReturn(Arrays.asList(new Adventure(1,"Pecanje",
                "Najlepsa Drina na svetu",15000,52)));

        // 2.
        List<Adventure> adventures = adventureService.getAdventuresByInstructorId(1);

        // 3.
        assertThat(adventures).hasSize(1);
        assertEquals(adventures.get(0).getName(), "Pecanje");

        verify(adventureRepository, times(1)).findByInstructorId(1);
        verifyNoMoreInteractions(adventureRepository);
    }


    @Mock
    private Boat boatMock;
    @Test
    @Transactional
    public void testAddBoat() {
        // 1.
        when(boatRepository.save(boatMock)).thenReturn(boatMock);

        // 2.
        Boat savedBoat = boatService.saveBoat(boatMock);

        // 3.
        Assert.assertThat(savedBoat, is(equalTo(boatMock)));
    }
    @Mock
    private Cottage cottageMock;
    @Test
    @Transactional
    public void testAddCottage() {
        // 1.
        when(cottageRepository.save(cottageMock)).thenReturn(cottageMock);

        // 2.
        Cottage savedCottage = cottageService.saveCottage(cottageMock);

        // 3.
        Assert.assertThat(savedCottage, is(equalTo(cottageMock)));
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    @Rollback(true)
    public void testSaveBoatIdNUll() {
        Boat boat = new Boat();
        boat.setName("Tarzan");
        boat.setPromoDescription("ITafsn sajnvjsd trartaaaa");
        // ne navodi se index koji je po modelu obavezan (nullable = false) -> Not null constraint violation

        // 1. Definisanje ponašanja mock objekata
        when(boatRepository.save(boat)).thenThrow(DataIntegrityViolationException.class);

        // 2. Akcija
        boatService.saveBoat(boat);

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        verify(boatRepository, times(1)).save(boat);
        verifyNoMoreInteractions(boatRepository);
    }
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    @Rollback(true)
    public void testSaveCottageIdNUll() {
        Cottage c = new Cottage();
        c.setName("Cotatgee");
        c.setPromoDescription("ITafsn sajnvjsd trartaaaa");
        // ne navodi se index koji je po modelu obavezan (nullable = false) -> Not null constraint violation

        // 1. Definisanje ponašanja mock objekata
        when(cottageRepository.save(c)).thenThrow(DataIntegrityViolationException.class);

        // 2. Akcija
        cottageService.saveCottage(c);

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        verify(cottageRepository, times(1)).save(c);
        verifyNoMoreInteractions(cottageRepository);
    }

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;

    @Test
    public void testFindByIdGrade(){

        when(gradeRepository.findById(1)).thenReturn(Optional.of(new Grade(5)));

        Grade grade = gradeService.getGradeById(1).get();

        assertThat(grade.getGrade()).isEqualTo(5);
        verify(gradeRepository, times(1)).findById(1);
        verifyNoMoreInteractions(gradeRepository);
    }

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    public void testFindByIdComplaint(){

        when(complaintRepository.findById(1)).thenReturn(Optional.of(new Complaint("tekst")));

        Complaint complaint = complaintService.getComplaintById(1).get();

        assertThat(complaint.getText()).isEqualTo("tekst");
        verify(complaintRepository, times(1)).findById(1);
        verifyNoMoreInteractions(complaintRepository);
    }
}
