package com.example.geektrust.vehicle;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Vehicle {
    private String vehicleNo;
    private VehicleType vehicleType;
}
