package org.qubitsoft;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();

//       List<Reservation> reservationList = reader.loadReservations("src/main/resources/Reservations.csv");
//        for (Reservation res: reservationList) {
//            System.out.println(res);
//        }


        Scanner scanner = new Scanner(System.in);


        Hotel hilton = new Hotel("Hilton", 5, createRoomTypeCapacity(2, 2, 1), reader);


        Hotel marriott = new Hotel("Marriott", 10, createRoomTypeCapacity(5, 2, 3), reader);


        Hotel interContinental = new Hotel("Marriott", 10, createRoomTypeCapacity(7, 2, 1), reader);


        Hotel hotel = new Hotel(reader);

        while (true) {
            System.out.println("Choose option:");
            System.out.println("1. Create reservation");
            System.out.println("2. Show reservations");
            System.out.println("3. Cancel reservation");
            System.out.println("4. Add CSV file with reservations");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Insert hotel name: (Hilton, Marriott, InterContinental)");
                    scanner.nextLine();
                    String hotelName = scanner.nextLine();
                    if (!isValidHotelName(hotelName)) {
                        System.out.println("Invalid hotel name!");
                        break;
                    }

                    System.out.print("Insert your firstname: ");
                    String clientFirstName = scanner.nextLine();

                    System.out.print("Insert your lastname: ");
                    String clientLastName = scanner.nextLine();

                    System.out.print("Insert your phone number: ");
                    String phoneNumber = scanner.nextLine();

                    System.out.print("Insert your email: ");
                    String email = scanner.nextLine();
                    if (!isValidEmail(email)) {
                        System.out.println("Invalid email format!");
                        break;
                    }
                    LocalDate startDate;
                    try {

                        System.out.print("Insert arrival date (YYYY-MM-DD): ");
                        startDate = LocalDate.parse(scanner.nextLine());
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format! Format should be: YYYY-MM-DD");
                        break;
                    }

                    LocalDate endDate;
                    try {

                        System.out.print("Insert leave date (YYYY-MM-DD): ");
                        endDate = LocalDate.parse(scanner.nextLine());
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format! Format should be: YYYY-MM-DD");
                        break;
                    }
                    System.out.print("Insert room type (single, double, apartment): ");
                    String roomTypeString = scanner.nextLine();
                    RoomType roomType = RoomType.valueOf(roomTypeString.toUpperCase());
                    if (!isValidRoomType(roomTypeString)) {
                        System.out.println("Invalid room type!");
                        break;
                    }

                    if (hotelName.equals("Marriott")) {
                        marriott.createReservation(hotelName, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);
                    } else if (hotelName.equals("Hilton")) {
                        hilton.createReservation(hotelName, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);
                    } else {
                        interContinental.createReservation(hotelName, clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);
                    }


                    break;
                case 2:
                    System.out.println("Insert hotel name: (Hilton, Marriott, InterContinental)");
                    scanner.nextLine();
                    String hotelNameInfo = scanner.nextLine();
                    if (!isValidHotelName(hotelNameInfo)) {
                        System.out.println("Invalid hotel name!");
                        break;
                    }

                    LocalDate startDateInfo;
                    try {

                        System.out.print("Insert arrival date (YYYY-MM-DD): ");
                        startDateInfo = LocalDate.parse(scanner.nextLine());
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format! Format should be: YYYY-MM-DD");
                        break;
                    }
                    LocalDate endDateInfo;
                    try {

                        System.out.print("Insert leave date (YYYY-MM-DD): ");
                        endDateInfo = LocalDate.parse(scanner.nextLine());
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format! Format should be: YYYY-MM-DD");
                        break;
                    }

                    hotel.loadReservationsInfo(hotelNameInfo, startDateInfo, endDateInfo);
                    break;
                case 3:
                    System.out.print("Insert your firstname: ");
                    scanner.nextLine();
                    String clientFirstNameCancel = scanner.nextLine();

                    System.out.print("Insert your lastname: ");
                    String clientLastNameCancel = scanner.nextLine();

                    System.out.print("Insert your phone number: ");
                    String phoneNumberCancel = scanner.nextLine();

                    System.out.print("Insert your email: ");
                    String emailCancel = scanner.nextLine();
                    if(!isValidEmail(emailCancel)){
                        System.out.println("Invalid email format!");
                        break;
                    }

                    System.out.println("Insert hotel name: (Hilton, Marriott, InterContinental)");
                    String hotelNameCancel = scanner.nextLine();
                    if(!isValidHotelName(hotelNameCancel)){
                        System.out.println("Invalid hotel name!");
                        break;
                    }
                    hotel.showUserReservationToCancel(clientFirstNameCancel, clientLastNameCancel, phoneNumberCancel, emailCancel, hotelNameCancel);
                    break;
                case 4:
                    System.out.println("Insert your CSV path");
                    scanner.nextLine();
                    String clientsCSVPath = scanner.nextLine();
                    hotel.insertMultipleReservations(clientsCSVPath, hilton, marriott, interContinental);
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Insert valid option!");
            }
        }
    }

    private static Map<RoomType, Integer> createRoomTypeCapacity(int single, int doubleRoom, int apartment) {
        Map<RoomType, Integer> roomTypeCapacity = new HashMap<>();
        roomTypeCapacity.put(RoomType.SINGLE, single);
        roomTypeCapacity.put(RoomType.DOUBLE, doubleRoom);
        roomTypeCapacity.put(RoomType.APARTMENT, apartment);
        return roomTypeCapacity;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidRoomType(String roomType) {
        return roomType.equalsIgnoreCase("single") || roomType.equalsIgnoreCase("double") || roomType.equalsIgnoreCase("apartment");
    }

    private static boolean isValidHotelName(String hotelName) {
        return hotelName.equalsIgnoreCase("Hilton") || hotelName.equalsIgnoreCase("Marriott") || hotelName.equalsIgnoreCase("InterContinental") || hotelName.equalsIgnoreCase("");
    }
}