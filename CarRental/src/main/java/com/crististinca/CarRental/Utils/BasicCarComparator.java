package com.crististinca.CarRental.Utils;

import com.crististinca.CarRental.model.Car;

import java.util.Comparator;

public class BasicCarComparator implements Comparator<Car> {
    @Override
    public int compare(Car o1, Car o2) {
        return o2.getIsActive().compareTo(o1.getIsActive());
    }
}
