package com.example.geektrust.manager;

import com.example.geektrust.vehicle.Vehicle;

import java.time.LocalTime;

public interface TrackManagerI {
    String bookTrackIfAvailable(final Vehicle vehicle, final LocalTime entryTime);
    String addExtraTimeIfAvailable(final String vehicleNo, final LocalTime exitTime);
    String calculateRevenue();
}
