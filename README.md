Fujitsu Java Programming Trial Task - REST interface Documentation


 Ctrl + A = beginning of line		 In order to start the server download download and save this project in intelij, 
 Ctrl + E = end of line		 choose edit configuration and then add tomcat server configuration (choose apache-tomcat-10.19.1).


 `~` is the variable for the home directory		 Endpoint for calculating delivery fee is


 ```		 ```
 echo ~		 http://localhost:8080/api/fee
 ```		 ```


 Tab completion: tab twice to display all of the available options.		 GET request has 2 mandatory parameters (`city` and  `transport`). For example in order to calculate delivery fee in city Tallin by car use (`city` and  `transport` are not case sensetive)

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
 * `datetime` - this is date and time parameter. Must be in form 
