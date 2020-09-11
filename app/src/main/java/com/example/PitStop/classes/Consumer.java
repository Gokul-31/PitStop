package com.example.PitStop.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Consumer implements Serializable {
    public String id;
    public long phone;
    public String Name;
    public String password;
    public int type=0;
    public ArrayList<BookCard> bookedCards;
    public int bookings;

    public Consumer(String id, long phone, String name, String password) {
        this.id = id;
        this.phone = phone;
        this.Name = name;
        this.password = password;
        bookedCards=new ArrayList<BookCard>();
        bookings=0;
    }

    public int getType() {
        return type;
    }

    public void setBookedCards(ArrayList<BookCard> bookedCards) {
        this.bookedCards = bookedCards;
    }
}
