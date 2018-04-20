package org.ParkSys.enums;

public enum Price {
    smallPrice(8.00),
    middlePrice(15.00),
    bigPrice(30.00);


    private Double price;

    private Price(double carPrice) {
        this.price = carPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
