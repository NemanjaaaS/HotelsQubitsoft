package org.qubitsoft;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();

        List<Reservation> reservationList = reader.loadReservations("src/main/resources/Reservations.csv");
        for (Reservation res: reservationList) {
            System.out.println(res);
        }
    }
}