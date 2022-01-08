package com.intuit.comparisonapi.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimilarCarResponse {
    private CarResource sourceCar;
    private List<CarResource> similarCars;
    private Map<String,Map<String,Boolean>> equalFieldsComparisonMapSourceCarVsSimilarCar;
}
