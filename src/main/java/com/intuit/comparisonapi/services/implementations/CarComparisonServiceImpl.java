package com.intuit.comparisonapi.services.implementations;

import com.intuit.comparisonapi.utils.MapperUtil;
import com.intuit.comparisonapi.controllers.dtos.CarResource;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarRequest;
import com.intuit.comparisonapi.controllers.dtos.SimilarCarResponse;
import com.intuit.comparisonapi.models.Car;
import com.intuit.comparisonapi.repositories.CarRepo;
import com.intuit.comparisonapi.services.CarComparisonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CarComparisonServiceImpl implements CarComparisonService {

    private CarRepo carRepo;

    @Override
    public CarResource searchCar(String documentId) {
        Car car =  carRepo.searchCar(documentId);
        CarResource carSearchResponse = new CarResource();
        MapperUtil.mapProperties(carSearchResponse, car);
        return carSearchResponse;
    }

    @Override
    public SimilarCarResponse similarCarRequest(SimilarCarRequest request) {
        Car car = new Car();
        MapperUtil.mapProperties(car, request.getSourceCarResource());
        List<Car> similarCars = carRepo.getSimilarCars(car);
        List<CarResource> similarCarResources = new ArrayList<>();
        copyCarsToCarResources(similarCars,similarCarResources);
        SimilarCarResponse similarCarResponse = new SimilarCarResponse();
        similarCarResponse.setSourceCar(request.getSourceCarResource());
        similarCarResponse.setSimilarCars(similarCarResources);
        try {
            populateEqualityMap(similarCarResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return similarCarResponse;
    }

    @Override
    public CarResource createCar(CarResource carResource) {
        Car car = new Car();
        MapperUtil.mapProperties(car, carResource);
        String id = carRepo.saveCar(car);
        carResource.setId(id);
        return carResource;
    }

    private void populateEqualityMap(SimilarCarResponse similarCarResponse) throws IllegalAccessException, NoSuchFieldException {
        Map<String, Map<String, Boolean>> equalFieldValueComparisonMap = new HashMap<>();
        CarResource sourceCar = similarCarResponse.getSourceCar();
        List<CarResource> similarCars = similarCarResponse.getSimilarCars();

        for (CarResource similarCar : similarCars) {
            Field[] fields = similarCar.getClass().getDeclaredFields();
            for(Field field: fields){
                field.setAccessible(true);
                Object similarCarFieldValue = field.get(similarCar);
                String name = field.getName();
                Field sourceCarField = sourceCar.getClass().getDeclaredField(name);
                sourceCarField.setAccessible(true);
                Object sourceCarFieldValue = sourceCarField.get(sourceCar);
                Map<String, Boolean> equalValueMap = equalFieldValueComparisonMap.getOrDefault(similarCar.getId(),
                        new HashMap<String, Boolean>());
                if (sourceCarFieldValue.equals(similarCarFieldValue)) {
                    equalValueMap.put(name,true);
                }else{
                    equalValueMap.put(name,false);
                }
                equalFieldValueComparisonMap.put(similarCar.getId(),equalValueMap);
            }
        }
        similarCarResponse.setEqualFieldsComparisonMapSourceCarVsSimilarCar(equalFieldValueComparisonMap);
    }

    private void copyCarsToCarResources(List<Car> cars, List<CarResource> carResources){
        cars.forEach(car -> {
            CarResource carResource = new CarResource();
            MapperUtil.mapProperties(carResource, car);
            carResources.add(carResource);
        });
    }
}
