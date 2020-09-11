package com.example.PitStop.classes;

public class TimeCard {
    private String time1;
    private String time2;
    private String am1;
    private String am2;
    private boolean booked=false;
    private Long bookedBY=0L;
    private String bookerName;

    public boolean isBooked() {
        return booked;
    }

    public TimeCard(){}

    public Long getBookedBY() {
        return bookedBY;
    }

    public TimeCard(String time1, String time2, String am1, String am2, boolean booked, Long bookedBY) {
        this.time1 = time1;
        this.time2 = time2;
        this.am1 = am1;
        this.am2 = am2;
        this.booked = booked;
        this.bookedBY = bookedBY;
        this.bookerName=null;
    }

    public String getBookerName() {
        return bookerName;
    }

    public TimeCard(int x, int y) {
        {
            booked=false;
            bookedBY=0L;
            bookerName=null;
            if (x == 0) {
                this.time1 = "12";
                this.am1 = "am";
            } else if (x == 12) {
                this.time1 = "12";
                this.am1 = "PM";
            } else if (x < 12) {
                this.time1 = ""+x;
                this.am1 = "am";
            } else if (x > 12) {
                this.time1 = ""+(x - 12);
                this.am1 = "PM";
            }
        }//x ptime1rt
        {
            if (y == 0) {
                this.time2 = "12";
                this.am2 = "am";
            } else if (y == 12) {
                this.time2 = "12";
                this.am2 = "PM";
            } else if (y < 12) {
                this.time2 = ""+y;
                this.am2 = "am";
            } else if (y > 12) {
                this.time2 = ""+(y - 12);
                this.am2 = "PM";
            }
        }//y part
    }

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    public String getAm1() {
        return am1;
    }

    public String getAm2() {
        return am2;
    }
}
