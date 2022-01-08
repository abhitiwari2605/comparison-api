package com.intuit.comparisonapi.controllers;

import com.intuit.comparisonapi.controllers.dtos.CarResource;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarResponse;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarRequest;
import com.intuit.comparisonapi.services.CarComparisonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/car")
@AllArgsConstructor
public class CarComparisonController {

    private CarComparisonService carComparisonService;

    @GetMapping
    public CarResource searchCar(@RequestParam String documentId) {
        return carComparisonService.searchCar(documentId);
    }

    @GetMapping(path = "/similarcars")
    public SimilarCarResponse getSimilarCar(@RequestBody SimilarCarRequest request) {
        return carComparisonService.similarCarRequest(request);
    }
}
