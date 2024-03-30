# Fujitsu Java Programming Trial Task - REST Interface Documentation

## Setting Up the Server

To start the server, follow these steps:
1. Download and save this project in IntelliJ IDEA.
2. Choose "Edit Configuration".
3. Add a Tomcat Server configuration, selecting `apache-tomcat-10.19.1` as the version.

## API Endpoints

### Calculating Delivery Fee

**Endpoint:** `http://localhost:8080/api/fee`

This GET request requires 2 mandatory parameters: `city` and `transport`. These parameters are not case-sensitive.

**Example:** To calculate the delivery fee in the city of Tallinn by car, use the URL below:

```
http://localhost:8080/api/fee?transport=car&city=tallinn
```

Besides the mandatory parameters, there are 12 optional parameters for business rule calculations and one optional parameter for date and time. Specifying business rule parameters will use new values for this delivery fee calculation. Weather-related business rules only apply to bike and scooter deliveries. If the date and time parameter is specified, the fee will be calculated based on the weather conditions at the specified date and time, or at the closest recorded time.

**Optional Parameters:**
- `rbf_car` - Regional base fee for car delivery.
- `rbf_scooter` - Regional base fee for scooter delivery.
- `rbf_bike` - Base fee for bike delivery.
- `atef_temperature` - Air temperature threshold for the first type of extra fee.
- `atef_temperature_min` - Air temperature threshold for the second type of extra fee.
- `atef_temperature_fee` - First type of air temperature extra fee.
- `atef_temperature_fee_max` - Second type of air temperature extra fee.
- `wsef_speed` - Wind speed threshold for extra fee.
- `wsef_speed_max` - Maximum wind speed for bike or scooter delivery.
- `wsef_fee` - Extra fee for high wind speed.
- `wpef_snow_or_sleetFee` - Extra fee for snow or sleet conditions.
- `wpef_snow_rain_fee` - Extra fee for rain conditions.
- `datetime` - Date and time for weather condition check (format: yyyy-MM-dd_HH:mm).

### Requesting Fee Calculation Rules

**Endpoint:** `http://localhost:8080/api/rules`

This GET request does not require any parameters.

### Setting New Fee Calculation Rules

**Endpoint:** `http://localhost:8080/api/rules/new`

This POST request requires 1 mandatory parameter `city`, specifying the city for which business rules will be changed. It accepts the same optional parameters as the delivery fee calculation. At least one optional parameter must be specified. Business rules not specified will remain unchanged.

**Note:** If all parameters are null, all business rules will be reset to default values. If at least one parameter is not null, parameters set to null will not be reset. True parameters will trigger a reset to default values for the corresponding business rules in the specified city.





