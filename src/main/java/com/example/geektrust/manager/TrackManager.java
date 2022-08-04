package com.example.geektrust.manager;

import com.example.geektrust.booking.BookingEntry;
import com.example.geektrust.track.TrackDetails;
import com.example.geektrust.track.TrackType;
import com.example.geektrust.vehicle.Vehicle;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.example.geektrust.track.TrackType.REGULAR_TRACK;
import static com.example.geektrust.track.TrackType.VIP_TRACK;


public class TrackManager implements TrackManagerI{
    private static TrackManager trackManager;
    private TrackManager() {
    }
    public static TrackManager getInstance(){
        if(trackManager == null)
            trackManager = new TrackManager();
        return trackManager;
    }
    private final TrackDetails trackDetails = TrackDetails.getInstance();
    private final int defaultBookingHours=3;
    private final DateTimeFormatter hhmmDTF = DateTimeFormatter.ofPattern("HH:mm");
    private final LocalTime openingTime = LocalTime.parse("13:00", hhmmDTF);
    private final LocalTime trackClosingTime = LocalTime.parse("20:00", hhmmDTF);
    private final LocalTime bookingClosingTime = trackClosingTime.minusHours(defaultBookingHours);
    //TODO : use db to persist bookings
    private final Map<String, BookingEntry> bookings = new HashMap<>();

    private final Predicate<LocalTime> entryTimeValidatorPredicate = (entryTime) -> (entryTime.isAfter(openingTime) || entryTime.equals(openingTime))
            && (entryTime.isBefore(bookingClosingTime) || entryTime.equals(bookingClosingTime));
    private final Predicate<LocalTime> exitTimeValidatorPredicate = (exitTime) -> exitTime.isBefore(trackClosingTime) || exitTime.equals((trackClosingTime));

    @Override
    public String bookTrackIfAvailable(final Vehicle vehicle, final LocalTime entryTime) {
        final BookingEntry newBookingEntry = BookingEntry.builder()
                                            .vehicle(vehicle)
                                            .entryTime(entryTime)
                                            .exitTime(entryTime.plusHours(defaultBookingHours))
                                            .build();
        if(entryTimeValidatorPredicate.test(entryTime)){
            final Optional<TrackType> trackTypeOptional = getTrackTypeIfAvailable(newBookingEntry);
            if(trackTypeOptional.isPresent()){
                newBookingEntry.setTrackType(trackTypeOptional.get());
                bookTrack(newBookingEntry);
                return "SUCCESS";
            } else
                return "RACETRACK_FULL";
        } else
            return "INVALID_ENTRY_TIME";
    }

    private Optional<TrackType> getTrackTypeIfAvailable(final BookingEntry newBookingEntry) {
        if (bookings.isEmpty()) {
            return Optional.of(REGULAR_TRACK);
        } else {
            int regularTrackCount = trackDetails.getNoOfVehiclesAllowed(REGULAR_TRACK, newBookingEntry.getVehicle().getVehicleType());
            int vipTrackCount = trackDetails.getNoOfVehiclesAllowed(VIP_TRACK, newBookingEntry.getVehicle().getVehicleType());
            for (final BookingEntry existingBookingEntry : bookings.values()) {
                if(isSameVehicleType(newBookingEntry,existingBookingEntry) && doesBookingTimingOverlap(newBookingEntry,existingBookingEntry)){
                    if (REGULAR_TRACK.equals(existingBookingEntry.getTrackType()))
                        regularTrackCount -= 1;
                    else if(VIP_TRACK.equals(existingBookingEntry.getTrackType()))
                        vipTrackCount -= 1;
                }
            }
            if (regularTrackCount > 0)
                return Optional.of(REGULAR_TRACK);
            else if (vipTrackCount > 0)
                return Optional.of(VIP_TRACK);
            else
                return Optional.empty();
        }
    }

    private boolean isSameVehicleType(final BookingEntry newBookingEntry, final BookingEntry existingBookingEntry) {
        return newBookingEntry.getVehicle().getVehicleType().equals(existingBookingEntry.getVehicle().getVehicleType());
    }
    private boolean doesBookingTimingOverlap(final BookingEntry newBookingEntry, final BookingEntry existingBookingEntry) {
        final boolean sameEntryTime = newBookingEntry.getEntryTime().equals(existingBookingEntry.getEntryTime());
        final boolean entryTimeOverlap = (sameEntryTime || newBookingEntry.getEntryTime().isAfter(existingBookingEntry.getEntryTime()) ) && newBookingEntry.getEntryTime().isBefore(existingBookingEntry.getExitTime());
        final boolean exitTimeOverlap = newBookingEntry.getExitTime().isAfter(existingBookingEntry.getEntryTime()) && newBookingEntry.getEntryTime().isBefore(existingBookingEntry.getExitTime());
        return entryTimeOverlap || exitTimeOverlap;
    }
    private void bookTrack(BookingEntry newBookingEntry) {
        bookings.put(newBookingEntry.getVehicle().getVehicleNo(), newBookingEntry);
    }

    @Override
    public String addExtraTimeIfAvailable(final String vehicleNo, final LocalTime exitTime) {
        final BookingEntry extraTimeRequestedBookingEntry = bookings.get(vehicleNo);
        if(exitTimeValidatorPredicate.test(exitTime)) {
                if(checkExtraTimeTrackAvailability(extraTimeRequestedBookingEntry,exitTime)){
                    extraTimeRequestedBookingEntry.setExitTime(exitTime);
                    bookExtraTime(extraTimeRequestedBookingEntry);
                    return "SUCCESS";
                } else
                    return "RACETRACK_FULL";
        } else
            return "INVALID_EXIT_TIME";
    }

    private boolean checkExtraTimeTrackAvailability(final BookingEntry extraTimeRequestedBookingEntry, final LocalTime exitTime){
        int regularTrackCount = trackDetails.getNoOfVehiclesAllowed(REGULAR_TRACK, extraTimeRequestedBookingEntry.getVehicle().getVehicleType());
        int vipTrackCount = trackDetails.getNoOfVehiclesAllowed(VIP_TRACK, extraTimeRequestedBookingEntry.getVehicle().getVehicleType());
        for (BookingEntry existingBookingEntry : bookings.values()) {
            if(isSameVehicleType(extraTimeRequestedBookingEntry,existingBookingEntry) && doesRequestedExtraTimeClash(existingBookingEntry,exitTime)) {
                if (REGULAR_TRACK.equals(existingBookingEntry.getTrackType()))
                    regularTrackCount -= 1;
                else
                    vipTrackCount -= 1;
            }
        }
        if (regularTrackCount > 0 || vipTrackCount > 0)
            return true;
        else
            return false;
    }

    private boolean doesRequestedExtraTimeClash(final BookingEntry existingBookingEntry, final LocalTime exitTime) {
        return exitTime.isAfter(existingBookingEntry.getEntryTime()) && exitTime.isBefore(existingBookingEntry.getExitTime());
    }

    private void bookExtraTime(final BookingEntry newBookingEntry) {
        bookings.put(newBookingEntry.getVehicle().getVehicleNo(), newBookingEntry);
    }

    @Override
    public String calculateRevenue() {
        int regularTrackRevenue=0, vipTrackRevenue=0;
        for(BookingEntry bookingEntry : bookings.values()) {
            if(REGULAR_TRACK.equals(bookingEntry.getTrackType())){
                regularTrackRevenue+=calculatePrice(bookingEntry);
            } else {
                vipTrackRevenue+=calculatePrice(bookingEntry);
            }

        }
        return regularTrackRevenue+" "+vipTrackRevenue;
    }

    private int calculatePrice(final BookingEntry bookingEntry) {
        int basePrice = trackDetails.getPricePerHour(bookingEntry.getTrackType(), bookingEntry.getVehicle().getVehicleType()) * defaultBookingHours;
        int extraTime = calculateExtraTime(bookingEntry.getEntryTime(), bookingEntry.getExitTime());
        int extraPrice=0;
        if(extraTime>0) {
            int additionalFee = 50;
            extraPrice = additionalFee * extraTime;
        }
        return basePrice+extraPrice;
    }

    private int calculateExtraTime(final LocalTime entryTime, final LocalTime exitTime) {
        long duration = ChronoUnit.MINUTES.between(entryTime, exitTime);
        long extraMinutes = duration - (defaultBookingHours*60);
        int extraTime=0;
        if (extraMinutes > 15) {
            extraTime += extraMinutes/60;
            extraMinutes %= 60;
            if(extraMinutes > 0)
                extraTime++;
        }
        return extraTime;
    }

}
