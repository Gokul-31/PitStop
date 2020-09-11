package com.example.PitStop.classes;

public class BookCard {
    private String time1;
    private String time2;
    private String am1;
    private String am2;
    private String shopName;
    private Long shopPhone;
    int shopType;

    public Long getShopPhone() {
        return shopPhone;
    }

    public BookCard(TimeCard c, String str, Long s,int t){
        time1=c.getTime1();
        time2=c.getTime2();
        am1=c.getAm1();
        am2=c.getAm2();
        shopName=str;
        shopPhone=s;
        shopType=t;
    }

    public BookCard(){}

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    public String getAm1() {
        return am1;
    }

    public void setAm1(String AM1) {
        this.am1 = AM1;
    }

    public void setAm2(String AM2) {
        this.am2 = AM2;
    }

    public String getAm2() {
        return am2;
    }

    public String getShopName() {
        return shopName;
    }

    public int getShopType() {
        return shopType;
    }
}
