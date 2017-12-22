package com.example.lenovo_.hedeftantm;

/**
 * Created by Lenovo- on 17.12.2017.
 */

public class KonumObject {
    private double lati,longi;

    private KonumObject(double lati,double longi){
        this.lati=lati;
        this.longi=longi;

    }

    public double getLati(){return  lati;}
    public double getLongi(){return longi;}
}
