package com.intuit.comparisonapi.repositories;

import com.intuit.comparisonapi.models.Car;

import java.util.List;

public interface CarRepo {

    String saveCar(Car car);

    Car searchCar(String id);

    List<Car> getSimilarCars(Car car);
}