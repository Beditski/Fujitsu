Fujitsu Java Programming Trial Task - REST interface Documentation

In order to start the server download download and save this project in intelij,
hoose edit configuration and then add tomcat server configuration (choose apache-tomcat-10.19.1).

Endpoint for calculating delivery fee is

```
http://localhost:8080/api/fee
```

This GET request has 2 mandatory parameters (`city` and  `transport`). For example in order to calculate delivery fee in city Tallin by car use (`city` and  `transport` are not case sensetive)

 ```
 http://localhost:8080/api/fee?transport=car&city=tallinn
 ```

 There are in total 12 diferent optional parameters for business (float value) rule calculations and one optional parameter for date and time. If business rule
 parameter are specified then new value for this business rule will be used for this delivery fee calculation. Weather related business rules are only applied 
 to the bike and scooter deliveries.If date and time parameter is specified then fee will be calculated according to the weather conditions at the specified date
 and time (or at the closest moment to specified daate-time when weather conditions were measured). 

 All non-optional parameters are

 * `rbf_car` -  regional base fee for car delivery.
 * `rbf_scooter` - regional base fee for scooter delivery.
 * `rbf_bike` - base fee for bike delivery.
 * `atef_temperature` - there two types of extra fees related to the air temperature. This parameter specifies first air temperature value. If air
  temperature is lower then this parameter first type of extra fee will be applyed.
 * `atef_temperature_min` - there two types of extra fees related to the air temperature. This parameter specifies second (lower) air temperature value. If air
  temperature is lower then this parameter second type of extra fee will be applyed.
 * `atef_temperature_fee` - first type air temperature extraa fee.
 * `atef_temperature_fee_max` - second type air temperature extraa fee.
 * `wsef_speed` - if wind speed is higher then this value, then wind speed extra fee will be added to the total fee.
 * `wsef_speed_max` - if wind speed is higher then this value, then delivery by bike or scooter is imposible.
 * `wsef_fee` - extra fee for high wind speed.
 * `wpef_snow_or_sleetFee` - extra fee for bike or scooter delivery in snow or sleet.
 * `wpef_snow_rain_fee` - extra fee for bike or scooter delivery in rain.
 * `datetime` - this is date and time parameter. Must be in form yyyy-MM-dd_HH:mm

Endpoint for requesting fee calculation rules: `http://localhost:8080/api/rules`. This GET request has no parameter.

Endpoint for setting new fee calculation rules: `http://localhost:8080/api/rules/new`. This POST request has 1 mandatory parameters `city`, which specifies the 
city for which business rules will be cahnged. There are in total 12 diferent optional parameters for business (float value) rule calculations, the same as for 
calculating delivery fee. At least on parameter must be specified. Business rules with non-specified parameters will not be chaanged.




Endpoint for setting new fee calculation rules: `http://localhost:8080/api/rules/new`. Takes 3 optional request parameters. If all parameters are nulls - all business 
rules will be reset to default values. If at least one parameter is not null, null parameters will not be reset to default values. All true parameters will cause reset
to default values for business rules in the corresponding city.







