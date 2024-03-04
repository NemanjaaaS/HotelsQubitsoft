package org.qubitsoft;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Hotel {
    private String name;
    private int roomCapacity;
    private Map<RoomType, Integer> roomCapacityByType;

    private CsvReader reader;

    private String csvPath = "Hotels/src/main/resources/Reservations.csv";


    public Hotel(String name, int roomCapacity, Map<RoomType, Integer> roomCapacityByType, CsvReader reader) {
        this.name = name;
        this.roomCapacity = roomCapacity;
        this.roomCapacityByType = roomCapacityByType;
        this.reader = reader;
    }

    public Hotel(CsvReader reader) {
        this.reader = reader;
    }

    /**
     * Checks the number of rooms already reserved for a specific hotel
     * between the given start and end dates.
     *
     * @param hotelName    The name of the hotel to check capacity for.
     * @param startDate    The start date for the reservation.
     * @param endDate      The end date for the reservation.
     * @param reservations The list of reservations from csv file
     * @return The number of rooms already reserved for the specific hotel during the given period.
     */
    private int checkCapacity(String hotelName, LocalDate startDate, LocalDate endDate, List<Reservation> reservations) {

        int takenRooms = 0;
        for (Reservation reservation : reservations) {
            if (hotelName.equalsIgnoreCase(reservation.getHotelName())) {
                takenRooms = getTakenRooms(startDate, endDate, takenRooms, reservation);
            }
        }
        return takenRooms;
    }



    /**
     * Checks the number of rooms of specific type already reserved for a specific hotel
     * between the given start and end dates.
     *
     * @param hotelName    The name of the hotel to check capacity for.
     * @param startDate    The start date for the reservation.
     * @param endDate      The end date for the reservation.
     * @param roomType     The type of room for the reservation (single, double, apartment)
     * @param reservations The list of reservations from csv file
     * @return The number of rooms already reserved for the specific hotel during the given period.
     */
    private int checkCapacityForRoomType(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType, List<Reservation> reservations) {

        int takenRooms = 0;
        for (Reservation reservation : reservations) {
            if (hotelName.equalsIgnoreCase(reservation.getHotelName()) && roomType.equals(reservation.getRoomType())) {
                takenRooms = getTakenRooms(startDate, endDate, takenRooms, reservation);
            }
        }
        return takenRooms;

    }

    /**
     * Calculates the number of rooms already taken for a reservation period
     * based on the given start and end dates and the existing reservation details.
     *
     * @param startDate   The start date for the new reservation period.
     * @param endDate     The end date for the new reservation period.
     * @param takenRooms  The current count of taken rooms to be updated.
     * @param reservation The existing reservation to compare against.
     * @return The updated count of taken rooms considering the new reservation period.
     */
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

    /**
     * Checks the availability and validate reservation based on the provided parameters.
     * <p>
     * Hilton decline reservation if stay days are less than 2 or there aren't free rooms.
     * Marriott decline reservation if stay days are less than 3 or there aren't free specific rooms (single, double, apartments).
     * InterContinental decline reservation if stay days are less than 1.
     *
     * @param hotelName    The name of the hotel for the reservation.
     * @param startDate    The start date of the reservation.
     * @param endDate      The end date of the reservation.
     * @param roomType     The type of room for the reservation.
     * @param reservations The list of reservations from csv file
     * @return {@code true} if the reservation is declined, {@code false} otherwise.
     */
    private boolean checkReservation(String hotelName, LocalDate startDate, LocalDate endDate, RoomType roomType, List<Reservation> reservations) {

        int stayDays = startDate.until(endDate).getDays();
        int freeRooms = getRoomCapacity() - checkCapacity(hotelName, startDate, endDate, reservations);
        int freeRoomsByType = getRoomCapacityByType().getOrDefault(roomType, 0) - checkCapacityForRoomType(hotelName, startDate, endDate, roomType, reservations);

        if (stayDays < 1) {
            System.out.println("Your reservation in " + hotelName + " is declined. You need to reserve more than 1 day.");
            return true;
        } else if (freeRooms <= 0) {
            System.out.println("Your reservation in " + hotelName + " is declined. There are no free rooms.");
            return true;
        } else if (hotelName.equalsIgnoreCase("Hilton") && stayDays < 2) {
            System.out.println("Your reservation in " + hotelName + " is declined. You need to reserve more than 2 days.");
            return true;
        } else if (hotelName.equalsIgnoreCase("Marriott") && stayDays < 3) {
            System.out.println("Your reservation in " + hotelName + " is declined. You need to reserve more than 3 days.");
            return true;
        } else if (hotelName.equalsIgnoreCase("Marriott") && freeRoomsByType <= 0) {
            System.out.println("Your reservation in " + hotelName + " is declined. There are no free rooms of that type.");
            return true;
        }
        return false;
    }

    /**
     * Creates a reservation for a hotel based on the provided parameters.
     *
     * @param hotelName       The name of the hotel for the reservation.
     * @param clientFirstName The first name of the client making the reservation.
     * @param clientLastName  The last name of the client making the reservation.
     * @param phoneNumber     The phone number of the client making the reservation.
     * @param email           The email address of the client making the reservation.
     * @param startDate       The start date of the reservation.
     * @param endDate         The end date of the reservation.
     * @param roomType        The type of room for the reservation.
     */
    public void createReservation(String hotelName, String clientFirstName, String clientLastName,
                                  String phoneNumber, String email, LocalDate startDate,
                                  LocalDate endDate, RoomType roomType) {
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        if (!checkReservation(hotelName, startDate, endDate, roomType, reservationList)) {
            Reservation reservation = new Reservation(hotelName, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);
            reservationList.add(reservation);
            reader.writeReservation(reservationList, csvPath);
            System.out.println("Reservation created successfully!");

        } else {
            System.out.println("Reservation failed!");
        }
    }

    /**
     * Loads and displays reservation information for a specific hotel within the given date range.
     *
     * @param hotelName The name of the hotel for which reservation information is requested.
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     */
    public List<Reservation> loadReservationsInfo(String hotelName, LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        List<Reservation> clientsReservations = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            if (hotelName.equalsIgnoreCase(reservation.getHotelName()) && startDate.equals(reservation.getStartDate()) && endDate.equals(reservation.getEndDate())) {
                clientsReservations.add(reservation);
                System.out.println(reservation);
            }
        }
        if(clientsReservations.isEmpty()) System.out.println("No reservations found!");
        return clientsReservations;
    }

    /**
     * Cancels the specified reservation by removing it from the list of reservations and updating the CSV file.
     *
     * @param reservationToCancel The reservation to be canceled.
     */
    private void cancelReservation(Reservation reservationToCancel) {
        List<Reservation> reservationList = reader.loadReservations(csvPath);
        reservationList.remove(reservationToCancel);
        reader.writeReservation(reservationList, csvPath);
    }

    /**
     * Displays the list of reservations for a specific user and prompts them to select a reservation for cancellation.
     *
     * @param clientFirstname First name of the client.
     * @param clientLastname  Last name of the client.
     * @param phoneNumber     Phone number of the client.
     * @param email           Email of the client.
     * @param hotelName       Name of the hotel.
     */
    public void showUserReservationToCancel(String clientFirstname, String clientLastname, String phoneNumber, String email, String hotelName) {
        Scanner scanner = new Scanner(System.in);
        List<Reservation> clientReservations = findReservations(clientFirstname, clientLastname, phoneNumber, email, hotelName);
        if (clientReservations.isEmpty()) {
            System.out.println("You don't have active reservations in " + hotelName + " hotel.");
        } else {
            System.out.println("Your reservations: ");
            for (int i = 0; i < clientReservations.size(); i++) {
                System.out.println((i + 1) + ". " + clientReservations.get(i).toString());
            }

            System.out.println("Enter the reservation number you want to cancel: ");
            int selectedReservationIndex = scanner.nextInt();

            if (selectedReservationIndex > 0 && selectedReservationIndex <= clientReservations.size()) {
                Reservation selectedReservation = clientReservations.get(selectedReservationIndex - 1);
                cancelReservation(selectedReservation);

                System.out.println("Cancellation successful!");
            } else {
                System.out.println("Input not valid!");
            }
        }
    }

    /**
     * Finds reservations for a specific user based on the provided details.
     *
     * @param clientFirstname First name of the client.
     * @param clientLastname  Last name of the client.
     * @param phoneNumber     Phone number of the client.
     * @param email           Email of the client.
     * @param hotelName       Name of the hotel.
     * @return List of reservations matching the provided client details and hotel name.
     */
    private List<Reservation> findReservations(String clientFirstname, String clientLastname, String phoneNumber, String email, String hotelName) {
        List<Reservation> clientReservations = new ArrayList<>();
        List<Reservation> hotelReservations = new ArrayList<>();
        hotelReservations.addAll(reader.loadReservations(csvPath));

        for (Reservation reservation : hotelReservations) {
            if (reservation.getFirstname().equalsIgnoreCase(clientFirstname) && reservation.getLastname().equalsIgnoreCase(clientLastname)
                    && reservation.getPhoneNumber().equals(phoneNumber) && reservation.getEmail().equals(email) && reservation.getHotelName().equalsIgnoreCase(hotelName)) {
                clientReservations.add(reservation);
            }
        }
        return clientReservations;
    }

    /**
     * Inserts multiple reservations into the hotel system based on client reservations.
     *
     * @param clientCsvPath    The path to the CSV file containing client reservations.
     * @param hilton           The Hilton hotel instance.
     * @param marriott         The Marriott hotel instance.
     * @param interContinental The InterContinental hotel instance.
     */
    public void insertMultipleReservations(String clientCsvPath, Hotel hilton, Hotel marriott, Hotel interContinental) {
        List<Reservation> clientsReservationsList = reader.loadReservations(clientCsvPath);
        List<Reservation> reservationList = reader.loadReservations(csvPath);

        for (Reservation reservation : clientsReservationsList) {
            setHotelProperties(reservation, hilton, marriott, interContinental);
            if (!checkReservation(reservation.getHotelName(), reservation.getStartDate(), reservation.getEndDate(), reservation.getRoomType(), reservationList)) {
                reservationList.add(reservation);
                System.out.println("Reservation for  " + reservation.getHotelName() + " hotel is approved. Date: " + reservation.getStartDate().toString() + " - " + reservation.getEndDate().toString());
            } else {
                System.out.println("Rejected hotel reservation for " + reservation.getHotelName() + ". Date: " + reservation.getStartDate().toString() + " - " + reservation.getEndDate().toString());
            }
        }
        reader.writeReservation(reservationList, csvPath);

    }

    /**
     * Sets the properties of the hotel based on the reservation.
     *
     * @param reservation      The reservation for which hotel properties are to be set.
     * @param hilton           The Hilton hotel instance.
     * @param marriott         The Marriott hotel instance.
     * @param interContinental The InterContinental hotel instance.
     */
    private void setHotelProperties(Reservation reservation, Hotel hilton, Hotel marriott, Hotel interContinental) {
        if (reservation.getHotelName().equalsIgnoreCase(hilton.getName())) {
            setName(hilton.getName());
            setRoomCapacity(hilton.getRoomCapacity());
            setRoomCapacityByType(hilton.getRoomCapacityByType());
        } else if (reservation.getHotelName().equalsIgnoreCase(marriott.getName())) {
            setName(marriott.getName());
            setRoomCapacity(marriott.getRoomCapacity());
            setRoomCapacityByType(marriott.getRoomCapacityByType());
        } else {
            setName(interContinental.getName());
            setRoomCapacity(interContinental.getRoomCapacity());
            setRoomCapacityByType(interContinental.getRoomCapacityByType());
        }
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
