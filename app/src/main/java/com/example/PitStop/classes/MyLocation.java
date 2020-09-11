package com.example.PitStop.classes;

public class MyLocation {
    static Double myLat=0D;
    static Double myLong=0D;

    public static Double getMyLat() {
        return myLat;
    }

    public static void setMyLat(Double myLat) {
        MyLocation.myLat = myLat;
    }

    public static Double getMyLong() {
        return myLong;
    }

    public static void setMyLong(Double myLong) {
        MyLocation.myLong = myLong;
    }
}
