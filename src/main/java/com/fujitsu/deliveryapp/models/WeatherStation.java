package com.fujitsu.deliveryapp.models;

public class WeatherStation {
    private final String nameOfTheStation;
    private final int wmoCode;
    private final double airTemperature;
    private final double windSpeed;
    private final String weatherPhenomenon;
    long timestamp;

    /**
     * Constructor for Weather Station class.
     * @param nameOfTheStation name of the station.
     * @param wmoCode WMO code of the station.
     * @param airTemperature air template.
     * @param windSpeed wind speed.
     * @param weatherPhenomenon weather phenomenon.
     * @param timestamp timestamp.
     */
    public WeatherStation(String nameOfTheStation, int wmoCode, double airTemperature, double windSpeed, String weatherPhenomenon, long timestamp) {
        this.nameOfTheStation = nameOfTheStation;
        this.wmoCode = wmoCode;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.timestamp = timestamp;
    }

    /**
     * Returns information about weather station at corresponding timestamp in the string format.
     * @return station's info.
     */
    @Override
    public String toString() {
        return this.wmoCode + ", " + '\'' +
                this.nameOfTheStation + '\'' + ", " +
                this.airTemperature + ", " +
                this.windSpeed + ", " + '\'' +
                this.weatherPhenomenon + '\'' + ", " +
                this.timestamp;
    }

    /*
     * All the followings methods are getters
     */
    public String getNameOfTheStation() {
        return nameOfTheStation;
    }

    public int getWmoCode() {
        return wmoCode;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
