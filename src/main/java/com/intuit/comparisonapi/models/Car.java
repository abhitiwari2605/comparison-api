package com.intuit.comparisonapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private String id;
    private int carId;
    private int symboling;
    private String carName;
    private String fuelType;
    private String aspiration;
    private String doorNumber;
    private String carBody;
    private String driveWheel;
    private String engineLocation;
    private double wheelBase;
    private double carLength;
    private double carWidth;
    private double carHeight;
    private int curWeight;
    private String engineType;
    private String cylinderNumber;
    private int engineSize;
    private String fuelSystem;
    private double boreRatio;
    private double stroke;
    private int compressionRatio;
    private int horsePower;
    private int peakRPM;
    private int cityMPG;
    private int highwayMPG;
    private int price;
}
