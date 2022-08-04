package com.example.geektrust.track;

import com.example.geektrust.vehicle.VehicleType;

import java.util.HashMap;
import java.util.Map;

import static com.example.geektrust.track.TrackType.REGULAR_TRACK;
import static com.example.geektrust.track.TrackType.VIP_TRACK;

public class TrackDetails {
    private static TrackDetails trackDetails;

    private TrackDetails() {
        noOfVehiclesAllowedMap=initNoOfVehiclesAllowed();
        pricePerHourMap=initPricePerHour();
    }

    public static TrackDetails getInstance() {
        if(trackDetails == null)
            trackDetails = new TrackDetails();
        return trackDetails;
    }

    private final Map<TrackType, Map<VehicleType,Integer>> noOfVehiclesAllowedMap;
    private final Map<TrackType, Map<VehicleType,Integer>> pricePerHourMap;

    //TODO : move to config
    private Map<TrackType, Map<VehicleType,Integer>> initNoOfVehiclesAllowed() {
        final Map<TrackType, Map<VehicleType,Integer>> noOfVehiclesAllowedMap = new HashMap<>();
        final Map<VehicleType,Integer> regularTrackMap = new HashMap<>();
        regularTrackMap.put(VehicleType.BIKE,4);
        regularTrackMap.put(VehicleType.CAR,2);
        regularTrackMap.put(VehicleType.SUV,2);

        final Map<VehicleType,Integer> vipTrackMap = new HashMap<>();
        vipTrackMap.put(VehicleType.BIKE,0);
        vipTrackMap.put(VehicleType.CAR,1);
        vipTrackMap.put(VehicleType.SUV,1);

        noOfVehiclesAllowedMap.put(REGULAR_TRACK,regularTrackMap);
        noOfVehiclesAllowedMap.put(VIP_TRACK,vipTrackMap);

        //TODO : make it immutable
        return noOfVehiclesAllowedMap;
    }

    //TODO : move to config
    private Map<TrackType, Map<VehicleType,Integer>> initPricePerHour() {
        final Map<TrackType, Map<VehicleType,Integer>> pricePerHourMap = new HashMap<>();
        final Map<VehicleType,Integer> regularTrackMap = new HashMap<>();
        regularTrackMap.put(VehicleType.BIKE,60);
        regularTrackMap.put(VehicleType.CAR,120);
        regularTrackMap.put(VehicleType.SUV,200);

        final Map<VehicleType,Integer> vipTrackMap = new HashMap<>();
        vipTrackMap.put(VehicleType.BIKE,0);
        vipTrackMap.put(VehicleType.CAR,250);
        vipTrackMap.put(VehicleType.SUV,300);

        pricePerHourMap.put(REGULAR_TRACK,regularTrackMap);
        pricePerHourMap.put(VIP_TRACK,vipTrackMap);

        //TODO : make it immutable
        return pricePerHourMap;
    }

    public int getNoOfVehiclesAllowed(final TrackType trackType, final VehicleType vehicleType) {
        return noOfVehiclesAllowedMap.get(trackType).get(vehicleType);
    }

    public int getPricePerHour(final TrackType trackType, final VehicleType vehicleType) {
        return pricePerHourMap.get(trackType).get(vehicleType);
    }
}
