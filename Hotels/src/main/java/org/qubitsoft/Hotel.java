package org.qubitsoft;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    private int checkCapacity(String hotelName, LocalDate startDate, LocalDate endDate) {
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

    private int checkCapacityForRoomType(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType) {
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

    private boolean checkReservation(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType) {
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

    public void createReservation(String hotelName,String clientFirstName, String clientLastName,
                                  String phoneNumber, String email, LocalDate startDate,
                                  LocalDate endDate, RoomType roomType) {
        CsvReader reader = new CsvReader();
        if (!checkReservation(hotelName, startDate, endDate, roomType)) {
            Reservation reservation = new Reservation(hotelName, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);

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


    private void cancelReservation(Reservation reservationToCancel){
        CsvReader reader = new CsvReader();
        List<Reservation> allReservations = reader.loadReservations(csvPath);
        System.out.println("To cancel  "+reservationToCancel.toString());
        allReservations.remove(reservationToCancel);
        for(Reservation res : allReservations){
            if(res.equals(reservationToCancel)){
                System.out.println("Poklapaju se: ");
                System.out.println(res.getFirstname());
                System.out.println(reservationToCancel.getFirstname());
            }
            System.out.println("RES "+res);
        }
        reader.writeReservation(allReservations,csvPath);
    }


    public void showUserReservationToCancel(String clientFirstname, String clientLastname, String phoneNumber, String email, String hotelName){
        Scanner scanner = new Scanner(System.in);
        List<Reservation> clientReservations = findReservations(clientFirstname,clientLastname,phoneNumber,email,hotelName);
        if(clientReservations.isEmpty()){
            System.out.println("Nemate aktivnih rezervacija u " + hotelName + ".");
        }else{
            System.out.println("Vase rezervacije: ");
            for(int i = 0; i < clientReservations.size(); i++){
                System.out.println((i+1) + "." + clientReservations.get(i).toString());
            }

            System.out.println("Unesite redni broj rezervacije koju zelite da otkazete.");
            int selectedReservationIndex = scanner.nextInt();
            System.out.println("Izabran index "+selectedReservationIndex);

            if(selectedReservationIndex > 0 && selectedReservationIndex <= clientReservations.size()){
                Reservation selectedReservation = clientReservations.get(selectedReservationIndex-1);
                System.out.println("Izabrana rezervacija "+selectedReservation.toString());
                cancelReservation(selectedReservation);

                System.out.println("Uspesno otkazano");
            }else {
                System.out.println("Niste validan unos!");
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

    public void insertMultipleReservations(String clientCsvPath){
        CsvReader reader = new CsvReader();
        List<Reservation> clientsReservationsList = reader.loadReservations(clientCsvPath);
        List<Reservation> hotelsReservationsList = reader.loadReservations(csvPath);

        for (Reservation reservation: clientsReservationsList) {
            if(!checkReservation(reservation.getHotelName(),reservation.getStartDate(),reservation.getEndDate(),reservation.getRoomType())){
                hotelsReservationsList.add(reservation);
                System.out.println("Rezervacija za hotel " + reservation.getHotelName() +" je odobrena. Datum: " + reservation.getStartDate().toString()+ " - " + reservation.getEndDate().toString());
            }else{
                System.out.println("Odbijena rezervacija za hotel " + reservation.getHotelName() +". Datum: " + reservation.getStartDate().toString()+ " - " + reservation.getEndDate().toString());
            }
        }
        reader.writeReservation(hotelsReservationsList,csvPath);

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
