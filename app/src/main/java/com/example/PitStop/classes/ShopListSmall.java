package com.example.PitStop.classes;

public class ShopListSmall {

    public String name;
    public String type;
    public Long phone;

    public ShopListSmall(String name,int t,Long phone){
        this.phone=phone;
        this.name=name;
        switch(t){
            case 1:
                this.type="Grocery";
                break;
            case 2:
                this.type="Bakery";
                break;
            case 3:
                this.type="Saloon";
                break;
            case 4:
                this.type="Pharmacy";
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getPhone() {
        return phone;
    }
}
