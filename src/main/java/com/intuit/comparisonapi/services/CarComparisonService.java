package com.intuit.comparisonapi.services;

import com.intuit.comparisonapi.controllers.dtos.CarResource;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarResponse;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarRequest;

public interface CarComparisonService {
    CarResource searchCar(String documentId);

    SimilarCarResponse similarCarRequest(SimilarCarRequest request);

    CarResource createCar(CarResource carResource);
}
