package org.qubitsoft;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CsvReader {
    public List<Reservation> loadReservations(String csvPath) {
        List<Reservation> reservationList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> data = reader.readAll();
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                String hotelName = row[0];
                String clientFirstname = row[1];
                String clientLastname = row[2];
                String phoneNumber = row[3];
                String email = row[4];
                LocalDate startDate = LocalDate.parse(row[5]);
                LocalDate endDate = LocalDate.parse(row[6]);
                RoomType roomType = RoomType.valueOf(row[7].toUpperCase());

                Reservation reservation = new Reservation(hotelName, clientFirstname, clientLastname, phoneNumber, email, startDate, endDate, roomType);
                reservationList.add(reservation);

            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return reservationList;
    }

}
