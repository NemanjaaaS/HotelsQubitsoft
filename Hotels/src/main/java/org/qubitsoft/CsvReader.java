package org.qubitsoft;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CsvReader {
    /**
     * Loads reservations from a CSV file specified by the given path.
     * <p>
     * This method reads reservation data from a CSV file, creates Reservation objects,
     * and adds them to a list. The CSV file is expected to have the following format:
     * <p>
     * HotelName,ClientFirstName,ClientLastName,PhoneNumber,Email,StartDate,EndDate,RoomType
     * "HILTON","NEMANJA","STEFA","050444","nemanja@gmail,com","2024-06-01","2024-06-10","SINGLE"
     * "HILTON","NIKOLA","NIKOLIC","05555","nikola@gmail.com","2024-06-01","2024-06-10","SINGLE"
     * ...
     *
     * @param csvPath The path to the CSV file containing reservation data.
     * @return A list of Reservation objects loaded from the CSV file.
     * @throws RuntimeException If there is an error reading or parsing the CSV file.
     */
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

    /**
     * Writes a list of reservations to a CSV file specified by the given path.
     * <p>
     * This method takes a list of Reservation objects, converts them to CSV format,
     * and writes them to a CSV file. The CSV file will have the following header:
     * <p>
     * HotelName,ClientFirstName,ClientLastName,PhoneNumber,Email,StartDate,EndDate,RoomType
     *
     * @param reservationList The list of Reservation objects to be written to the CSV file.
     * @param csvPath         The path to the CSV file where reservations will be written.
     * @throws RuntimeException If there is an error writing the reservations to the CSV file.
     */
    public void writeReservation(List<Reservation> reservationList, String csvPath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            String[] header = {"Naziv hotela", "Ime klijenta", "Prezime klijenta", "Broj telefona", "email", "Datum dolaska", "Datum odlaska", "Tip sobe"};
            writer.writeNext(header);

            for (Reservation reservation : reservationList) {
                String[] data = {reservation.getHotelName().toUpperCase(), reservation.getFirstname().toUpperCase(), reservation.getLastname().toUpperCase(), reservation.getPhoneNumber(), reservation.getEmail(),
                        reservation.getStartDate().toString(), reservation.getEndDate().toString(), reservation.getRoomType().toString()};
                writer.writeNext(data);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
