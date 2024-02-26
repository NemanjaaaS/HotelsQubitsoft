package org.qubitsoft;

import java.time.LocalDate;
import java.util.Map;

public class Hotel {
    private String name;
    private String roomCapacity;
    private Map<RoomType,Integer> roomCapacityByType;

    public Hotel(String name, String roomCapacity, Map<RoomType, Integer> roomCapacityByType) {
        this.name = name;
        this.roomCapacity = roomCapacity;
        this.roomCapacityByType = roomCapacityByType;
    }

    public boolean checkReservation(String hotelName, LocalDate startDate, LocalDate endDate, String roomType){
        int stayDays = startDate.until(endDate).getDays();
        int freeRoomsByType = getRoomCapacityByType().getOrDefault(roomType,0);

        if(stayDays < 1){
            System.out.println("Your reservation in "+hotelName+" is declined. You need to reserve more than 1 day.");
            return true;
        } else if (freeRoomsByType == 0) {
            System.out.println("Your reservation in "+hotelName+" is declined. There are no free rooms.");
            return true;
        } else if (hotelName.equals("Hilton") && stayDays < 2) {

        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public Map<RoomType, Integer> getRoomCapacityByType() {
        return roomCapacityByType;
    }

    public void setRoomCapacityByType(Map<RoomType, Integer> roomCapacityByType) {
        this.roomCapacityByType = roomCapacityByType;
    }
}
