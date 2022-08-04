package com.example.geektrust.inputhandler;

import com.example.geektrust.vehicle.Vehicle;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class InputRequest {
    Operation operation;
    Vehicle vehicle;
    InputTime inputTime;
}
