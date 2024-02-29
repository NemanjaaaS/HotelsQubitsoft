package org.qubitsoft;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();

//       List<Reservation> reservationList = reader.loadReservations("src/main/resources/Reservations.csv");
//        for (Reservation res: reservationList) {
//            System.out.println(res);
//        }


        Scanner scanner = new Scanner(System.in);


        Hotel hilton = new Hotel("Hilton", 5, createRoomTypeCapacity(2,2,1));


        Hotel marriott = new Hotel("Marriott", 10, createRoomTypeCapacity(5,2,3));


        Hotel interContinental = new Hotel("Marriott", 10, createRoomTypeCapacity(7,2,1));


        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("Izaberite opciju:");
            System.out.println("1. Krairajte rezervaciju");
            System.out.println("2. Pregledaj rezervacije");
            System.out.println("3. Otkazite rezervaciju");
            System.out.println("4. Dodajte CSV fajl sa rezervacijama");
            System.out.println("5. Izlaz");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Insert hotel name: (Hilton, Marriott, InterContinental)");
                    scanner.nextLine();
                    String hotelName = scanner.nextLine();

                    System.out.print("Insert your firstname: ");
                    String clientFirstName = scanner.nextLine();

                    System.out.print("Insert your lastname: ");
                    String clientLastName = scanner.nextLine();

                    System.out.print("Insert your phone number: ");
                    String phoneNumber = scanner.nextLine();

                    System.out.print("Insert your email: ");
                    String email = scanner.nextLine();

                    System.out.print("Insert arrival date (YYYY-MM-DD): ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine());

                    System.out.print("Insert leave date (YYYY-MM-DD): ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine());

                    System.out.print("Insert room type (single, double, apartment): ");
                    String roomTypeString = scanner.nextLine();
                    RoomType roomType = RoomType.valueOf(roomTypeString.toUpperCase());

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

                    System.out.println("Insert arrival date (YYYY-MM-DD): ");
                    LocalDate startDateInfo = LocalDate.parse(scanner.nextLine());

                    System.out.print("Insert leave date (YYYY-MM-DD): ");
                    LocalDate endDateInfo = LocalDate.parse(scanner.nextLine());

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

                    System.out.println("Insert hotel name: (Hilton, Marriott, InterContinental)");

                    String hotelNameCancel = scanner.nextLine();
                    hotel.showUserReservationToCancel(clientFirstNameCancel, clientLastNameCancel, phoneNumberCancel, emailCancel, hotelNameCancel);
                    break;
                case 4:
                    System.out.println("Insert your CSV path");
                    scanner.nextLine();
                    String clientsCSVPath = scanner.nextLine();
                    hotel.insertMultipleReservations(clientsCSVPath,hilton,marriott,interContinental);
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
}