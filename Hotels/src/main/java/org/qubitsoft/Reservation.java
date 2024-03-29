package org.qubitsoft;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;


public class Reservation {
    private String hotelName;

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private RoomType roomType;

    @Override
    public String toString() {
        return "Reservation{" +
                "hotelName='" + hotelName + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", roomType=" + roomType +
                '}';
    }

    public Reservation(String hotelName, String firstname, String lastname, String phoneNumber, String email, LocalDate startDate, LocalDate endDate, RoomType roomType) {
        this.hotelName = hotelName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomType = roomType;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(hotelName, that.hotelName) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(email, that.email) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && roomType == that.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelName, firstname, lastname, phoneNumber, email, startDate, endDate, roomType);
    }
}
