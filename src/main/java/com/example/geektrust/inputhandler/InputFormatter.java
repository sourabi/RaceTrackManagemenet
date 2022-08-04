package com.example.geektrust.inputhandler;

import com.example.geektrust.vehicle.Vehicle;
import com.example.geektrust.vehicle.VehicleType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.geektrust.inputhandler.Operation.*;

public class InputFormatter {
    private static InputFormatter inputFormatter;
    private InputFormatter() {
    }
    public static InputFormatter getInstance(){
        if(inputFormatter == null)
            inputFormatter = new InputFormatter();
        return inputFormatter;
    }

    public InputRequest formatInput(String input){
        String[] inputDetails = input.split(" ");
        final DateTimeFormatter hhmmDTF = DateTimeFormatter.ofPattern("HH:mm");
        InputRequest inputRequest = null;
        switch(Operation.valueOf(inputDetails[0])) {
            case BOOK : inputRequest = InputRequest.builder()
                    .operation(BOOK)
                    .vehicle(Vehicle.builder()
                            .vehicleType(VehicleType.valueOf(inputDetails[1]))
                            .vehicleNo(inputDetails[2])
                            .build())
                    .inputTime(InputTime.builder()
                            .timeType(TimeType.ENTRY_TIME)
                            .requestedTime(LocalTime.parse(inputDetails[3],hhmmDTF))
                            .build())
                    .build();
                break;
            case ADDITIONAL: inputRequest = InputRequest.builder()
                    .operation(ADDITIONAL)
                    .vehicle(Vehicle.builder()
                            .vehicleNo(inputDetails[1])
                            .build())
                    .inputTime(InputTime.builder()
                            .timeType(TimeType.EXIT_TIME)
                            .requestedTime(LocalTime.parse(inputDetails[2],hhmmDTF))
                            .build())
                    .build();
                break;
            case REVENUE: inputRequest = InputRequest.builder()
                    .operation(REVENUE)
                    .build();
                break;
        }
        return inputRequest;
    }
}
