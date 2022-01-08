package com.intuit.comparisonapi.controllers;

import com.intuit.comparisonapi.models.Car;
import com.intuit.comparisonapi.repositories.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "test")
public class TestController {

    @Autowired
    private CarRepo carRepo;

    @GetMapping
    public List<Car> testCarRepo(){
        return carRepo.
                getSimilarCars(
                        carRepo.searchCar("i87pLn4B0soDsR9HDB6R"));
    }
}
