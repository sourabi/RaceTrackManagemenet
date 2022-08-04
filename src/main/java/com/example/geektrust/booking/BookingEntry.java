package com.example.geektrust.booking;

import com.example.geektrust.track.TrackType;
import com.example.geektrust.vehicle.Vehicle;
import lombok.*;

import java.time.LocalTime;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BookingEntry {
    private Vehicle vehicle;
    @Setter
    private TrackType trackType;
    private LocalTime entryTime;
    @Setter
    private LocalTime exitTime;
}
