package com.example.geektrust.manager;

import com.example.geektrust.vehicle.Vehicle;
import com.example.geektrust.vehicle.VehicleType;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrackManagerTest {

    private final TrackManager trackManager = TrackManager.getInstance();
    private final DateTimeFormatter hhmmDTF = DateTimeFormatter.ofPattern("HH:mm");
    private Vehicle car1,car2,car3,car4,car5,car6;
    private LocalTime entryTime1,entryTime2,entryTime3,entryTime4,entryTime5,entryTime6;
    private LocalTime exitTime1,exitTime2,exitTime6;

    @BeforeEach
    void setUp() {
        car1 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A01").build();
        entryTime1=LocalTime.parse("14:00", hhmmDTF);
        exitTime1 = LocalTime.parse("17:49",hhmmDTF);

        car2 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A02").build();
        entryTime2=LocalTime.parse("13:00", hhmmDTF);
        exitTime2 = LocalTime.parse("16:30",hhmmDTF);

        car3 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A03").build();
        entryTime3 = LocalTime.parse("15:00", hhmmDTF);

        car4 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A04").build();
        entryTime4 = LocalTime.parse("14:00", hhmmDTF);

        car5 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A05").build();
        entryTime5 = LocalTime.parse("18:00", hhmmDTF);

        car6 = Vehicle.builder().vehicleType(VehicleType.CAR).vehicleNo("A06").build();
        entryTime6 = LocalTime.parse("16:00", hhmmDTF);
        exitTime6 = LocalTime.parse("20:30",hhmmDTF);
    }

    @Order(1)
    @Test
    void bookTrackIfAvailableTest_Success() {
        assertEquals("SUCCESS",trackManager.bookTrackIfAvailable(car1,entryTime1));
    }

    @Order(2)
    @Test
    void bookTrackIfAvailableTest_RaceTrackFull() {
        assertEquals("SUCCESS",trackManager.bookTrackIfAvailable(car2,entryTime2));
        assertEquals("SUCCESS",trackManager.bookTrackIfAvailable(car3,entryTime3));
        assertEquals("RACETRACK_FULL",trackManager.bookTrackIfAvailable(car4,entryTime4));
    }

    @Order(3)
    @Test
    void bookTrackIfAvailableTest_InvalidEntryTime() {
        assertEquals("INVALID_ENTRY_TIME",trackManager.bookTrackIfAvailable(car5,entryTime5));
    }

    @Order(4)
    @Test
    void addExtraTimeIfAvailableTest_Success() {
        assertEquals("SUCCESS",trackManager.addExtraTimeIfAvailable(car1.getVehicleNo(),exitTime1));
    }

    @Order(5)
    @Test
    void addExtraTimeIfAvailable_RaceTrackFull() {
        assertEquals("SUCCESS",trackManager.bookTrackIfAvailable(car6,entryTime6));
        assertEquals("RACETRACK_FULL",trackManager.addExtraTimeIfAvailable(car2.getVehicleNo(),exitTime2));
    }

    @Order(6)
    @Test
    void addExtraTimeIfAvailableTest_InvalidExitTime() {
        assertEquals("INVALID_EXIT_TIME",trackManager.addExtraTimeIfAvailable(car6.getVehicleNo(),exitTime6));
    }

    @Order(7)
    @Test
    void calculateRevenueTest() {
        assertEquals("1130 750",trackManager.calculateRevenue());
    }
}