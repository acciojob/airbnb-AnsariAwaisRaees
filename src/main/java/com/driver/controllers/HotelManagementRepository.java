package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HotelManagementRepository {

    HashMap<String, Hotel> hotelDB = new HashMap<>();
    HashMap<Integer, User> userDB = new HashMap<>();
    HashMap<String, Booking> bookingDB = new HashMap<>();

    public String addHotel(Hotel hotel) {
        String key = hotel.getHotelName();

        if (key == null)
            return "FAILURE";
        else
            hotelDB.put(key, hotel);
        return "SUCCESS";
    }

    public Integer addUser(@RequestParam User user) {
        int key = user.getaadharCardNo();

        userDB.put(key, user);
        return key;
    }

    public String getHotelWithMostFacilities() {
        int noOfFacilities = 0;
        String ans = "";

        for (String hotelName : hotelDB.keySet()){
            Hotel hotel = hotelDB.get(hotelName);

            if (hotel.getFacilities().size() > noOfFacilities){
                ans = hotelName;
                noOfFacilities = hotel.getFacilities().size();
            }
            else if (hotel.getFacilities().size() == noOfFacilities){
                if (hotelName.compareTo(ans) < 0)
                    ans = hotelName;
            }
        }

        return ans;
    }

    public int bookRoom(Booking booking) {
        UUID uuid = UUID.randomUUID();
        String bookingID = uuid.toString();
        booking.setBookingId(bookingID);

        String hotelName = booking.getHotelName();
        Hotel hotel = hotelDB.get(hotelName);
        int pricePerNight = hotel.getPricePerNight();
        int noOfRoom = booking.getNoOfRooms();
        int availableRoom = hotel.getAvailableRooms();

        if (noOfRoom > availableRoom)
            return -1;

        int amountPaid = noOfRoom * pricePerNight;
        booking.setAmountToBePaid(amountPaid);

        hotel.setAvailableRooms(availableRoom - noOfRoom);
        bookingDB.put(bookingID, booking);
        hotelDB.put(hotelName, hotel);

        return amountPaid;
    }

    public int getBookings(Integer aadhaarCard) {
        int ans = 0;

        for (String bookingID : bookingDB.keySet()){
            Booking booking = bookingDB.get(bookingID);
            if (booking.getBookingAadharCard() == aadhaarCard){
                ans++;
            }
        }
        return ans;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        Hotel hotel = hotelDB.get(hotelName);
        List<Facility> currFacilities = hotel.getFacilities();

        for (Facility facility : newFacilities){
            if (currFacilities.contains(facility)){
                continue;
            }else {
                currFacilities.add(facility);
            }
        }

        hotel.setFacilities(currFacilities);
        hotelDB.put(hotelName, hotel);

        return hotel;
    }
}
