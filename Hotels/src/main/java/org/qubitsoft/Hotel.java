package org.qubitsoft;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Hotel {
    private String name;
    private int roomCapacity;
    private Map<RoomType, Integer> roomCapacityByType;



    private String csvPath = "Hotels/src/main/resources/Reservations.csv";

    public Hotel(String name, int roomCapacity, Map<RoomType, Integer> roomCapacityByType) {
        this.name = name;
        this.roomCapacity = roomCapacity;
        this.roomCapacityByType = roomCapacityByType;
    }
    public Hotel(){}

    public int checkCapacity(String hotelName, LocalDate startDate, LocalDate endDate) {
        CsvReader reader = new CsvReader();
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        int takenRooms = 0;
        for (Reservation reservation : reservationList) {
            if (hotelName.equals(reservation.getHotelName())) {
                takenRooms = getTakenRooms(startDate, endDate, takenRooms, reservation);
            }
        }
        return takenRooms;
    }

    public int checkCapacityForRoomType(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType) {
        CsvReader reader = new CsvReader();
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        int takenRooms = 0;
        for (Reservation reservation : reservationList) {
            if (hotelName.equals(reservation.getHotelName()) && roomType.equals(reservation.getRoomType())) {
                takenRooms = getTakenRooms(startDate, endDate, takenRooms, reservation);
            }
        }
        return takenRooms;

    }

    private int getTakenRooms(LocalDate startDate, LocalDate endDate, int takenRooms, Reservation reservation) {
        if (startDate.isAfter(reservation.getStartDate()) && startDate.isBefore(reservation.getEndDate())) {
            takenRooms++;
        } else if (endDate.isAfter(reservation.getStartDate()) && endDate.isBefore(reservation.getEndDate())) {
            takenRooms++;
        } else if (startDate.isAfter(reservation.getStartDate()) && endDate.isBefore(reservation.getEndDate())) {
            takenRooms++;
        } else if (startDate.isBefore(reservation.getStartDate()) && endDate.isAfter(reservation.getEndDate())) {
            takenRooms++;
        }
        return takenRooms;
    }

    public boolean checkReservation(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType) {
        int stayDays = startDate.until(endDate).getDays();
        int freeRooms = getRoomCapacity() - checkCapacity(hotelName, startDate, endDate);
        int freeRoomsByType = getRoomCapacityByType().getOrDefault(roomType, 0) - checkCapacityForRoomType(hotelName, startDate, endDate, roomType);

        if (stayDays < 1) {
            System.out.println("Your reservation in " + hotelName + " is declined. You need to reserve more than 1 day.");
            return true;
        } else if (freeRooms <= 0) {
            System.out.println("Your reservation in " + hotelName + " is declined. There are no free rooms.");
            return true;
        } else if (hotelName.equals("Hilton") && stayDays < 2) {
            System.out.println("Your reservation in " + hotelName + " is declined. You need to reserve more than 2 days.");
            return true;
        } else if (hotelName.equals("Marriott") && stayDays < 3) {
            System.out.println("Your reservation in" + hotelName + " is declined. You need to reserve more than 3 days.");
            return true;
        } else if (hotelName.equals("Marriott") && freeRoomsByType <= 0) {
            System.out.println("Your reservation in" + hotelName + " is declined. There are no free rooms of that type.");
            return true;
        }
        return false;
    }

    public void createReservation(String clientFirstName, String clientLastName,
                                  String phoneNumber, String email, LocalDate startDate,
                                  LocalDate endDate, RoomType roomType) {
        CsvReader reader = new CsvReader();
        if (!checkReservation(this.name, startDate, endDate, roomType)) {
            Reservation reservation = new Reservation(this.name, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);

            List<Reservation> reservationList = reader.loadReservations(csvPath);
            reservationList.add(reservation);
            reader.writeReservation(reservationList, csvPath);
            System.out.println("Reservation created successfully!");

        } else {
            System.out.println("Reservation failed!");
        }
    }

    public void loadReservationsInfo(String hotelName, LocalDate startDate, LocalDate endDate){
        CsvReader reader = new CsvReader();
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        for(Reservation reservation : reservationList){
            if(hotelName.equals(reservation.getHotelName()) && startDate.equals(reservation.getStartDate()) && endDate.equals(reservation.getEndDate())){
                System.out.println(reservation);
            }
        }
    }


    public void cancelReservation(String clientFirstName, String clientLastName, String phoneNumber, String email,String hotelName){
        List<Reservation> clientReservations = findReservations(clientFirstName,clientLastName,phoneNumber,email,hotelName);
        if(clientReservations.isEmpty()){
            System.out.println("No active reservations!");
        } else {
            System.out.println("Your active reservations: ");
            for(int i = 0; i < clientReservations.size(); i++){
                System.out.println((i+1) + ". "+ clientReservations.get(i));
            }
        }
    }

    private List<Reservation> findReservations(String clientFirstname, String clientLastname, String phoneNumber, String email, String hotelName) {
        List<Reservation> clientReservations = new ArrayList<>();
        List<Reservation> reservationList = new ArrayList<>();
        CsvReader reader = new CsvReader();
        reservationList.addAll(reader.loadReservations(csvPath));

        for (Reservation reservation : reservationList) {
            if (reservation.getFirstname().equals(clientFirstname) && reservation.getLastname().equals(clientLastname)
                    && reservation.getPhoneNumber().equals(phoneNumber) && reservation.getEmail().equals(email) && reservation.getHotelName().equals(hotelName)) {
                clientReservations.add(reservation);
            }
        }
        return clientReservations;
    }
    public String getCsvPath() {
        return csvPath;
    }

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public Map<RoomType, Integer> getRoomCapacityByType() {
        return roomCapacityByType;
    }

    public void setRoomCapacityByType(Map<RoomType, Integer> roomCapacityByType) {
        this.roomCapacityByType = roomCapacityByType;
    }
}
