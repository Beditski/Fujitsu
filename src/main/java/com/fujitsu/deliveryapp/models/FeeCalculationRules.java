package com.fujitsu.deliveryapp.models;

/**
 * Model for storing rules for calculating delivery fee.
 */
public class FeeCalculationRules {
    private final String city;
    private final String weatherStationName;
    private final String transport;

    private Double rbfCar;
    private Double rbfBike;
    private Double rbfScooter;

    private Double atefTemperature;
    private Double atefTemperatureMin;
    private Double atefFee;
    private Double atefFeeMax;

    private Double wsefSpeed;
    private Double wsefSpeedMax;
    private Double wsefFee;

    private Double wpefSnowOrSleetFee;
    private Double wpefSnowRainFee;

    private Long timestamp;

    /**
     * Constructor for FeeCalculationRules model.
     *
     * @param city
     * @param weatherStationName name of the city for the delivery.
     * @param transport          type of delivery transport.
     * @param rbfCar             regional base fee for the car.
     * @param rbfBike            regional base fee for the bike.
     * @param rbfScooter         regional base fee for scooter.
     * @param atefTemperature    air temperature boundary for adding extra fee in case of bike/scooter delivery.
     * @param atefTemperatureMin air temperature boundary for adding the highest extra fee in case of bike/scooter delivery.
     * @param atefFee            fee for low air temperature in case of bike/scooter delivery.
     * @param atefFeeMax         maximal fee for low air temperature in case of bike/scooter delivery
     * @param wsefSpeed          wind speed boundary for adding extra fee in case of bike/scooter delivery.
     * @param wsefSpeedMax       maximal allowed wind speed for bike/scooter delivery.
     * @param wsefFee            fee for wind speed in case of bike/scooter delivery.
     * @param wpefSnowOrSleetFee fee for bike/scooter delivery in snow or sleet.
     * @param wpefSnowRainFee    fee for bike/scooter delivery in rain.
     * @param timestamp          fee for delivery at specified time.
     */
    public FeeCalculationRules(String city, String weatherStationName, String transport,
                               Double rbfCar, Double rbfBike, Double rbfScooter,
                               Double atefTemperature, Double atefTemperatureMin, Double atefFee, Double atefFeeMax,
                               Double wsefSpeed, Double wsefSpeedMax, Double wsefFee,
                               Double wpefSnowOrSleetFee, Double wpefSnowRainFee,
                               Long timestamp) {
        this.city = city;
        this.weatherStationName = weatherStationName;
        this.transport = transport;
        this.rbfCar = rbfCar;
        this.rbfBike = rbfBike;
        this.rbfScooter = rbfScooter;
        this.atefTemperature = atefTemperature;
        this.atefTemperatureMin = atefTemperatureMin;
        this.atefFee = atefFee;
        this.atefFeeMax = atefFeeMax;
        this.wsefSpeed = wsefSpeed;
        this.wsefSpeedMax = wsefSpeedMax;
        this.wsefFee = wsefFee;
        this.wpefSnowOrSleetFee = wpefSnowOrSleetFee;
        this.wpefSnowRainFee = wpefSnowRainFee;
        this.timestamp = timestamp;
    }

    /**
     * Getter for city name.
     * @return city name.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Getter for weather station's name.
     * @return weather station's name.
     */
    public String getWeatherStationName() {
        return weatherStationName;
    }

    /**
     * Getter for the transport type.
     * @return transport type.
     */
    public String getTransport() {
        return transport;
    }

    /**
     * Getter for the car's RBF.
     * @return RBF car value.
     */
    public Double getRbfCar() {
        return rbfCar;
    }

    /**
     * Getter for the bike RBF.
     * @return RBF bike's value.
     */
    public Double getRbfBike() {
        return rbfBike;
    }

    /**
     * Getter for the scooter's RBF.
     * @return RBF scooter value.
     */
    public Double getRbfScooter() {
        return rbfScooter;
    }

    /**
     * Getter for ATEF temperature.
     * @return ATEF temperature.
     */
    public Double getAtefTemperature() {
        return atefTemperature;
    }

    /**
     * Getter for ATEF minimum temperature.
     * @return ATEF minimum temperature.
     */
    public Double getAtefTemperatureMin() {
        return atefTemperatureMin;
    }

    /**
     * Getter for ATEF.
     * @return ATEF.
     */
    public Double getAtefFee() {
        return atefFee;
    }

    /**
     * Getter for ATEF maximum.
     * @return ATEF maximum.
     */
    public Double getAtefFeeMax() {
        return atefFeeMax;
    }

    /**
     * Getter for WSEF wind speed.
     * @return WSEF wind speed.
     */
    public Double getWsefSpeed() {
        return wsefSpeed;
    }

    /**
     * Getter for WSEF maximum wind speed.
     * @return WSEF maximum wind speed.
     */
    public Double getWsefSpeedMax() {
        return wsefSpeedMax;
    }

    /**
     * Getter for WSEF.
     * @return WSEF.
     */
    public Double getWsefFee() {
        return wsefFee;
    }

    /**
     * Getter for WPEF (snow or sleet).
     * @return extra fee for snow or sleet.
     */
    public Double getWpefSnowOrSleetFee() {
        return wpefSnowOrSleetFee;
    }

    /**
     * Getter for WPEF (rain).
     * @return rain extra fee.
     */
    public Double getWpefRainFee() {
        return wpefSnowRainFee;
    }

    /**
     * Getter for the timestamp.
     * @return timestamp.
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the car's RBF.
     * @param rbfCar RBF car value to set.
     */
    public void setRbfCar(Double rbfCar) {
        this.rbfCar = rbfCar;
    }

    /**
     * Setter for the bike's RBF.
     * @param rbfBike RBF bike value to set.
     */
    public void setRbfBike(Double rbfBike) {
        this.rbfBike = rbfBike;
    }

    /**
     * Setter for the scooter's RBF.
     * @param rbfScooter RBF scooter value to set.
     */
    public void setRbfScooter(Double rbfScooter) {
        this.rbfScooter = rbfScooter;
    }

    /**
     * Setter for ATEF temperature.
     * @param atefTemperature ATEF temperature to set.
     */
    public void setAtefTemperature(Double atefTemperature) {
        this.atefTemperature = atefTemperature;
    }

    /**
     * Setter for ATEF minimum temperature.
     * @param atefTemperatureMin ATEF minimum temperature to set.
     */
    public void setAtefTemperatureMin(Double atefTemperatureMin) {
        this.atefTemperatureMin = atefTemperatureMin;
    }

    /**
     * Setter for ATEF.
     * @param atefFee set ATEF.
     */
    public void setAtefFee(Double atefFee) {
        this.atefFee = atefFee;
    }

    /**
     * Setter for maximum ATEF.
     * @param atefFeeMax maximum ATEF.
     */
    public void setAtefFeeMax(Double atefFeeMax) {
        this.atefFeeMax = atefFeeMax;
    }

    /**
     * Setter for WSEF wind speed.
     * @param wsefSpeed set WSEF wind speed for bike and scooter deliveries.
     */
    public void setWsefSpeed(Double wsefSpeed) {
        this.wsefSpeed = wsefSpeed;
    }

    /**
     * Setter for WSEF maximum allowed wind speed.
     * @param wsefSpeedMax set maximum allowed wind speed for bike and scooter deliveries.
     */
    public void setWsefSpeedMax(Double wsefSpeedMax) {
        this.wsefSpeedMax = wsefSpeedMax;
    }

    /**
    * Setter for WSEF.
    * @param wsefFee WSEF.
    **/
    public void setWsefFee(Double wsefFee) {
        this.wsefFee = wsefFee;
    }

    /**
     * Setter for snow or sleet extra fee.
     * @param wpefSnowOrSleetFee set snow or sleet extra fee.
     */
    public void setWpefSnowOrSleetFee(Double wpefSnowOrSleetFee) {
        this.wpefSnowOrSleetFee = wpefSnowOrSleetFee;
    }

    /**
     * Setter for rain extra fee.
     * @param wpefSnowRainFee set rain extraa fee.
     */
    public void setWpefRainFee(Double wpefSnowRainFee) {
        this.wpefSnowRainFee = wpefSnowRainFee;
    }

    /**
     * Setter for timestamp.
     * @param timestamp set time stamp.
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}