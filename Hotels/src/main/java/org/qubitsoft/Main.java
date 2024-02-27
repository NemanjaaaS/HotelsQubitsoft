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
        Map<RoomType,Integer> roomTypeCapacity = new HashMap<>();
        roomTypeCapacity.put(RoomType.SINGLE,1);
        roomTypeCapacity.put(RoomType.DOUBLE,1);
        roomTypeCapacity.put(RoomType.TRIPLE,2);
        Hotel hilton = new Hotel("Hilton", 4,roomTypeCapacity); // Pretpostavljamo kapacitet hotela

        Hotel hotel = new Hotel();

        while(true){
            System.out.println("Izaberite opciju:");
            System.out.println("1. Dodaj novu rezervaciju");
            System.out.println("2. Pregledaj rezervacije");
            System.out.println("3. Izlaz");

            int choice = scanner.nextInt();

            switch (choice){
                case 1:

                    System.out.print("Unesite svoje ime: ");
                    String clientFirstName = scanner.nextLine();

                    System.out.print("Unesite svoje prezime: ");
                    String clientLastName = scanner.nextLine();

                    System.out.print("Unesite broj telefona: ");
                    String phoneNumber = scanner.nextLine();

                    System.out.print("Unesite email: ");
                    String email = scanner.nextLine();

                    System.out.print("Unesite datum dolaska (YYYY-MM-DD): ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine());

                    System.out.print("Unesite datum odlaska (YYYY-MM-DD): ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine());

                    System.out.print("Unesite tip sobe (single, double, triple): ");
                    String roomTypeString = scanner.nextLine();
                    RoomType roomType = RoomType.valueOf(roomTypeString.toUpperCase());

                    // Kreiranje rezervacije

                    hotel.createReservation(clientFirstName, clientLastName, phoneNumber, email, startDate, endDate, roomType);
                    break;
                case 2:
                    System.out.println("Unesite naziv hotela: (Hilton, Marriott, InterContinental)");
                    scanner.nextLine();
                    String hotelName = scanner.nextLine();

                    System.out.println("Unesite datum dolaska (YYYY-MM-DD): ");
                    LocalDate startDateInfo = LocalDate.parse(scanner.nextLine());

                    System.out.print("Unesite datum odlaska (YYYY-MM-DD): ");
                    LocalDate endDateInfo = LocalDate.parse(scanner.nextLine());

                   hotel.loadReservationsInfo(hotelName,startDateInfo,endDateInfo);
                    break;
                default:
                    System.out.println("Unesite validnu opciju!");
            }
        }
    }
}