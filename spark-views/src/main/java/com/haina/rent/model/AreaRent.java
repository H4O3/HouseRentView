package com.haina.rent.model;

import lombok.Data;

@Data
public class AreaRent {
    private String district;//市
    private String address;//区
    private double area;//面积
    private double areaRent;//房价
}
