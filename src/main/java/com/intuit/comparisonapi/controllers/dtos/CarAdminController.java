package com.intuit.comparisonapi.controllers.dtos;

import com.intuit.comparisonapi.services.CarComparisonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/admin/car")
@AllArgsConstructor
public class CarAdminController {

    private CarComparisonService carComparisonService;

    @PostMapping
    public CarResource createCar(@RequestBody CarResource carResource){
        return carComparisonService.createCar(carResource);
    }

    @PostMapping(path = "/bulk")
    public void createCar(@RequestBody List<CarResource> carResources) {
        carResources.forEach(carResource -> carComparisonService.createCar(carResource));
    }
}
