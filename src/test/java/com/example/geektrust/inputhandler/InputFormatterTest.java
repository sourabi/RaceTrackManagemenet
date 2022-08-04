package com.example.geektrust.inputhandler;

import com.example.geektrust.vehicle.Vehicle;
import com.example.geektrust.vehicle.VehicleType;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.geektrust.inputhandler.Operation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputFormatterTest {
    private final InputFormatter inputFormatter = InputFormatter.getInstance();
    final DateTimeFormatter hhmmDTF = DateTimeFormatter.ofPattern("HH:mm");

    @Test
    void formatInput_BOOK_Operation() {
        final String input = "BOOK BIKE M40 14:00";
        final InputRequest expectedInputRequest = InputRequest.builder()
                .operation(BOOK)
                .vehicle(Vehicle.builder()
                        .vehicleType(VehicleType.BIKE)
                        .vehicleNo("M40")
                        .build())
                .inputTime(InputTime.builder()
                        .timeType(TimeType.ENTRY_TIME)
                        .requestedTime(LocalTime.parse("14:00",hhmmDTF))
                        .build())
                .build();

        final InputRequest actualInputRequest = inputFormatter.formatInput(input);

        assertEquals(expectedInputRequest,actualInputRequest);
    }

    @Test
    void formatInput_ADDITIONAL_Operation() {
        final String input = "ADDITIONAL M40 17:15";
        final InputRequest expectedInputRequest = InputRequest.builder()
                .operation(ADDITIONAL)
                .vehicle(Vehicle.builder()
                        .vehicleNo("M40")
                        .build())
                .inputTime(InputTime.builder()
                        .timeType(TimeType.EXIT_TIME)
                        .requestedTime(LocalTime.parse("17:15",hhmmDTF))
                        .build())
                .build();

        final InputRequest actualInputRequest = inputFormatter.formatInput(input);

        assertEquals(expectedInputRequest,actualInputRequest);
    }

    @Test
    void formatInput_REVENUE_Operation() {
        final String input = "REVENUE";
        final InputRequest expectedInputRequest = InputRequest.builder()
                .operation(REVENUE)
                .build();

        final InputRequest actualInputRequest = inputFormatter.formatInput(input);

        assertEquals(expectedInputRequest,actualInputRequest);
    }
}