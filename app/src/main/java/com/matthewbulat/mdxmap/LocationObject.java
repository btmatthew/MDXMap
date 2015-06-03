package com.matthewbulat.mdxmap;

/**
 * Created by Matthew Bulat on 27/05/2015.
 */
public class LocationObject {
    private String roomNumber;
    private double longitude;
    private double latitude;
    private String building;
    private int floor;

    protected String getRoomNumber(){
        return roomNumber;
    }
    protected void setRoomNumber(String roomNumber){
        this.roomNumber=roomNumber;
    }
    protected double getLongitude(){
        return longitude;
    }
    protected void setLongitude(double longitude){
        this.longitude=longitude;
    }
    protected double getLatitude(){
        return latitude;
    }
    protected void setLatitude(double latitude){
        this.latitude=latitude;
    }
    protected String getBuilding(){
        return building;
    }
    protected void setBuilding(String building){
        this.building=building;
    }
    protected int getFloor(){
        return floor;
    }
    protected void setFloor(int floor){
        this.floor=floor;
    }
}
