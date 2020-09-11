package com.example.PitStop.classes;

import java.util.ArrayList;

public class Shop {
    public String id;
    public long phone;
    public String Name;
    public String password;
    public int type;
    public ArrayList<TimeCard> timeCards;
    public Double Lat;
    public Double Longi;

    public Shop(String id, long phone, String name, String password, int type, Double lat, Double aLong) {
        this.id = id;
        this.phone = phone;
        Name = name;
        this.password = password;
        this.type = type;
        this.timeCards = new ArrayList<>();
        Lat = lat;
        Longi = aLong;
    }

    public void setTimeCards(ArrayList<TimeCard> timeCards) {
        this.timeCards = timeCards;
    }
}
