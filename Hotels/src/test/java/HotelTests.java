import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.qubitsoft.CsvReader;
import org.qubitsoft.Hotel;
import org.qubitsoft.Reservation;
import org.qubitsoft.RoomType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HotelTests {
    private static final String CSV_PATH = "D:\\GitHub\\HotelsQubitsoft\\Hotels\\src\\main\\resources\\Reservations.csv";

    @Mock
    private CsvReader readerMock;
    @InjectMocks
    private Hotel hotel = new Hotel(readerMock);
    @Mock
    private Scanner scannerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<RoomType, Integer> roomCapacityByType = new HashMap<>();
        roomCapacityByType.put(RoomType.SINGLE, 5);
        roomCapacityByType.put(RoomType.DOUBLE, 3);
        roomCapacityByType.put(RoomType.APARTMENT, 2);
        hotel = new Hotel("Hilton", 10, roomCapacityByType, readerMock);
    }

    @Test
    public void testCreateReservation() {

        String hotelName = "Hilton";
        String clientFirstname = "Nemanja";
        String clientLastname = "Stefanovic";
        String phoneNumber = "065111222";
        String email = "nemanja@gmail.com";
        LocalDate startDate = LocalDate.parse("2024-06-06");
        LocalDate endDate = LocalDate.parse("2024-06-12");
        RoomType roomType = RoomType.SINGLE;

        when(readerMock.loadReservations(Mockito.anyString())).thenReturn(new ArrayList<>());

        int numberOfReservations = readerMock.loadReservations(CSV_PATH).size();

        hotel.createReservation(hotelName, clientFirstname, clientLastname, phoneNumber, email, startDate, endDate, roomType);

        assertEquals(numberOfReservations + 1, readerMock.loadReservations(CSV_PATH).size());
    }

    @Test
    public void loadReservationInfoTest() {


        List<Reservation> sampleReservations = new ArrayList<>();
        sampleReservations.add(new Reservation("Hilton", "NEMANJA", "STEFANOVIC", "12345", "nema@gma.com", LocalDate.parse("2024-10-01"), LocalDate.parse("2024-10-05"), RoomType.SINGLE));
        sampleReservations.add(new Reservation("InterContinental", "Nikola", "Nikolic", "555444", "niko@gmail.com", LocalDate.parse("2024-10-10"), LocalDate.parse("2024-10-20"), RoomType.DOUBLE));

        when(readerMock.loadReservations(Mockito.anyString())).thenReturn(sampleReservations);

        List<Reservation> result = hotel.loadReservationsInfo("Hilton", LocalDate.parse("2024-10-01"), LocalDate.parse("2024-10-05"));

        assertEquals(sampleReservations.get(0), result.get(0));
    }

    @Test
    public void testShowUserReservationToCancel() {

        Hotel hotel1 = mock(Hotel.class);
        hotel1.showUserReservationToCancel("Nemanja","Stefanovic","12345","nema@gma.com","Hilton");
        verify(hotel1,times(1)).showUserReservationToCancel("Nemanja","Stefanovic","12345","nema@gma.com","Hilton");

    }
}
