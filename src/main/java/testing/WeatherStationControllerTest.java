package testing;

import com.fujitsu.deliveryapp.controllers.WeatherStationController;
import com.fujitsu.deliveryapp.models.WeatherStation;
import org.junit.Test;

import java.util.List;

import static org.junit .Assert.assertEquals;


public class WeatherStationControllerTest {
    @Test
    public void parseWeatherDataTest() {
        String testInfo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "            <observations timestamp=\"1711640898\">\n" +
                "\t<station>\n" +
                "        \t\t<name>Kuressaare linn</name>\n" +
                "        \t\t<wmocode></wmocode>\n" +
                "        \t\t<longitude>22.48944444411111</longitude>\n" +
                "        \t\t<latitude>58.26416666666667</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity>60</relativehumidity>\n" +
                "        \t\t<airtemperature>9.8</airtemperature>\n" +
                "        \t\t<winddirection></winddirection>\n" +
                "        \t\t<windspeed></windspeed>\n" +
                "        \t\t<windspeedmax></windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Tallinn-Harku</name>\n" +
                "        \t\t<wmocode>26038</wmocode>\n" +
                "        \t\t<longitude>24.602891666624284</longitude>\n" +
                "        \t\t<latitude>59.398122222355134</latitude>\n" +
                "        \t\t<phenomenon> </phenomenon>\n" +
                "        \t\t<visibility>35.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>996.4</airpressure>\n" +
                "        \t\t<relativehumidity>49</relativehumidity>\n" +
                "        \t\t<airtemperature>13.6</airtemperature>\n" +
                "        \t\t<winddirection>128</winddirection>\n" +
                "        \t\t<windspeed>3.8</windspeed>\n" +
                "        \t\t<windspeedmax>5.7</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex>0.4</uvindex>\n" +
                "        \t\t<sunshineduration>494</sunshineduration>\n" +
                "        \t\t<globalradiation>242</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Pakri</name>\n" +
                "        \t\t<wmocode>26029</wmocode>\n" +
                "        \t\t<longitude>24.040080555476536</longitude>\n" +
                "        \t\t<latitude>59.38950277719013</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>35.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>995.9</airpressure>\n" +
                "        \t\t<relativehumidity>46</relativehumidity>\n" +
                "        \t\t<airtemperature>14.2</airtemperature>\n" +
                "        \t\t<winddirection>105</winddirection>\n" +
                "        \t\t<windspeed>2.6</windspeed>\n" +
                "        \t\t<windspeedmax>5.5</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Kunda</name>\n" +
                "        \t\t<wmocode>26045</wmocode>\n" +
                "        \t\t<longitude>26.541397222079624</longitude>\n" +
                "        \t\t<latitude>59.52141111042325</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>50.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.5</airpressure>\n" +
                "        \t\t<relativehumidity>51</relativehumidity>\n" +
                "        \t\t<airtemperature>13</airtemperature>\n" +
                "        \t\t<winddirection>158</winddirection>\n" +
                "        \t\t<windspeed>5.3</windspeed>\n" +
                "        \t\t<windspeedmax>10.2</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>-4</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>2.4</watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Jõhvi</name>\n" +
                "        \t\t<wmocode>26046</wmocode>\n" +
                "        \t\t<longitude>27.39827499992098</longitude>\n" +
                "        \t\t<latitude>59.32902499979958</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>999.4</airpressure>\n" +
                "        \t\t<relativehumidity>51</relativehumidity>\n" +
                "        \t\t<airtemperature>12.7</airtemperature>\n" +
                "        \t\t<winddirection>173</winddirection>\n" +
                "        \t\t<windspeed>5.3</windspeed>\n" +
                "        \t\t<windspeedmax>9</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>533</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Narva</name>\n" +
                "        \t\t<wmocode>26058</wmocode>\n" +
                "        \t\t<longitude>28.10933333290948</longitude>\n" +
                "        \t\t<latitude>59.38952777723252</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>1000</airpressure>\n" +
                "        \t\t<relativehumidity>48</relativehumidity>\n" +
                "        \t\t<airtemperature>13</airtemperature>\n" +
                "        \t\t<winddirection>153</winddirection>\n" +
                "        \t\t<windspeed>4.4</windspeed>\n" +
                "        \t\t<windspeedmax>7</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation>226</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Väike-Maarja</name>\n" +
                "        \t\t<wmocode>26141</wmocode>\n" +
                "        \t\t<longitude>26.2307361108683</longitude>\n" +
                "        \t\t<latitude>59.141347221994856</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.2</airpressure>\n" +
                "        \t\t<relativehumidity>60</relativehumidity>\n" +
                "        \t\t<airtemperature>11.5</airtemperature>\n" +
                "        \t\t<winddirection>128</winddirection>\n" +
                "        \t\t<windspeed>5.4</windspeed>\n" +
                "        \t\t<windspeedmax>9.5</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Tiirikoja</name>\n" +
                "        \t\t<wmocode>26145</wmocode>\n" +
                "        \t\t<longitude>26.952113888925975</longitude>\n" +
                "        \t\t<latitude>58.86540277798971</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>999.3</airpressure>\n" +
                "        \t\t<relativehumidity>83</relativehumidity>\n" +
                "        \t\t<airtemperature>3.7</airtemperature>\n" +
                "        \t\t<winddirection></winddirection>\n" +
                "        \t\t<windspeed></windspeed>\n" +
                "        \t\t<windspeedmax></windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>600</sunshineduration>\n" +
                "        \t\t<globalradiation>217</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Jõgeva</name>\n" +
                "        \t\t<wmocode>26144</wmocode>\n" +
                "        \t\t<longitude>26.41500555568271</longitude>\n" +
                "        \t\t<latitude>58.74983611073539</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.2</airpressure>\n" +
                "        \t\t<relativehumidity>57</relativehumidity>\n" +
                "        \t\t<airtemperature>13</airtemperature>\n" +
                "        \t\t<winddirection>146</winddirection>\n" +
                "        \t\t<windspeed>3.1</windspeed>\n" +
                "        \t\t<windspeedmax>5.3</windspeedmax>\t\t<waterlevel>66</waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>658</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Tartu-Tõravere</name>\n" +
                "        \t\t<wmocode>26242</wmocode>\n" +
                "        \t\t<longitude>26.46130555576748</longitude>\n" +
                "        \t\t<latitude>58.264072222179834</latitude>\n" +
                "        \t\t<phenomenon>Test 2</phenomenon>\n" +
                "        \t\t<visibility>35.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.4</airpressure>\n" +
                "        \t\t<relativehumidity>57</relativehumidity>\n" +
                "        \t\t<airtemperature>12.8</airtemperature>\n" +
                "        \t\t<winddirection>149</winddirection>\n" +
                "        \t\t<windspeed>5.1</windspeed>\n" +
                "        \t\t<windspeedmax>8.8</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>642</sunshineduration>\n" +
                "        \t\t<globalradiation>214</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Võru</name>\n" +
                "        \t\t<wmocode>26249</wmocode>\n" +
                "        \t\t<longitude>27.01950555496306</longitude>\n" +
                "        \t\t<latitude>57.84627777702059</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.9</airpressure>\n" +
                "        \t\t<relativehumidity>50</relativehumidity>\n" +
                "        \t\t<airtemperature>13.9</airtemperature>\n" +
                "        \t\t<winddirection>153</winddirection>\n" +
                "        \t\t<windspeed>3.4</windspeed>\n" +
                "        \t\t<windspeedmax>9.9</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>646</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Valga</name>\n" +
                "        \t\t<wmocode>26247</wmocode>\n" +
                "        \t\t<longitude>26.037733333042386</longitude>\n" +
                "        \t\t<latitude>57.79004999975144</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>35.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>997.8</airpressure>\n" +
                "        \t\t<relativehumidity>54</relativehumidity>\n" +
                "        \t\t<airtemperature>13.7</airtemperature>\n" +
                "        \t\t<winddirection>149</winddirection>\n" +
                "        \t\t<windspeed>4.7</windspeed>\n" +
                "        \t\t<windspeedmax>7.6</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Viljandi</name>\n" +
                "        \t\t<wmocode>26233</wmocode>\n" +
                "        \t\t<longitude>25.60015555555622</longitude>\n" +
                "        \t\t<latitude>58.37777499979958</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>997.5</airpressure>\n" +
                "        \t\t<relativehumidity>54</relativehumidity>\n" +
                "        \t\t<airtemperature>13</airtemperature>\n" +
                "        \t\t<winddirection>137</winddirection>\n" +
                "        \t\t<windspeed>2.1</windspeed>\n" +
                "        \t\t<windspeedmax>4.3</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>625</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Türi</name>\n" +
                "        \t\t<wmocode>26135</wmocode>\n" +
                "        \t\t<longitude>25.40915555530124</longitude>\n" +
                "        \t\t<latitude>58.80870833343929</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>997.3</airpressure>\n" +
                "        \t\t<relativehumidity>55.11</relativehumidity>\n" +
                "        \t\t<airtemperature>12.6</airtemperature>\n" +
                "        \t\t<winddirection>137</winddirection>\n" +
                "        \t\t<windspeed>2.6</windspeed>\n" +
                "        \t\t<windspeedmax>5.9</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Kuusiku</name>\n" +
                "        \t\t<wmocode>26134</wmocode>\n" +
                "        \t\t<longitude>24.733944444124358</longitude>\n" +
                "        \t\t<latitude>58.973174999418106</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>996.7</airpressure>\n" +
                "        \t\t<relativehumidity>52</relativehumidity>\n" +
                "        \t\t<airtemperature>13.2</airtemperature>\n" +
                "        \t\t<winddirection>147</winddirection>\n" +
                "        \t\t<windspeed>3.8</windspeed>\n" +
                "        \t\t<windspeedmax>6.5</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>652</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Lääne-Nigula</name>\n" +
                "        \t\t<wmocode>26124</wmocode>\n" +
                "        \t\t<longitude>23.81566666709052</longitude>\n" +
                "        \t\t<latitude>58.951083333359826</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>20.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>995.7</airpressure>\n" +
                "        \t\t<relativehumidity>49</relativehumidity>\n" +
                "        \t\t<airtemperature>14</airtemperature>\n" +
                "        \t\t<winddirection>136</winddirection>\n" +
                "        \t\t<windspeed>5</windspeed>\n" +
                "        \t\t<windspeedmax>8</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>666</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Virtsu</name>\n" +
                "        \t\t<wmocode>26128</wmocode>\n" +
                "        \t\t<longitude>23.51355555534363</longitude>\n" +
                "        \t\t<latitude>58.572674999100215</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>35.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>995.3</airpressure>\n" +
                "        \t\t<relativehumidity>57.745</relativehumidity>\n" +
                "        \t\t<airtemperature>11.4</airtemperature>\n" +
                "        \t\t<winddirection>137</winddirection>\n" +
                "        \t\t<windspeed>2.7</windspeed>\n" +
                "        \t\t<windspeedmax>6</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>-1</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>1.6</watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Pärnu-Sauga</name>\n" +
                "        \t\t<wmocode>26231</wmocode>\n" +
                "        \t\t<longitude>24.46957222156615</longitude>\n" +
                "        \t\t<latitude>58.41968888825401</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity></relativehumidity>\n" +
                "        \t\t<airtemperature></airtemperature>\n" +
                "        \t\t<winddirection></winddirection>\n" +
                "        \t\t<windspeed></windspeed>\n" +
                "        \t\t<windspeedmax></windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Kihnu</name>\n" +
                "        \t\t<wmocode>26226</wmocode>\n" +
                "        \t\t<longitude>23.97016666610596</longitude>\n" +
                "        \t\t<latitude>58.0986388881317</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>50.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>995.4</airpressure>\n" +
                "        \t\t<relativehumidity>74</relativehumidity>\n" +
                "        \t\t<airtemperature>8</airtemperature>\n" +
                "        \t\t<winddirection>87</winddirection>\n" +
                "        \t\t<windspeed>2.9</windspeed>\n" +
                "        \t\t<windspeedmax>5.1</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Ruhnu</name>\n" +
                "        \t\t<wmocode>26227</wmocode>\n" +
                "        \t\t<longitude>23.258883333206178</longitude>\n" +
                "        \t\t<latitude>57.78339444411078</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>34.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>994.6</airpressure>\n" +
                "        \t\t<relativehumidity>79</relativehumidity>\n" +
                "        \t\t<airtemperature>6</airtemperature>\n" +
                "        \t\t<winddirection>107</winddirection>\n" +
                "        \t\t<windspeed>5.1</windspeed>\n" +
                "        \t\t<windspeedmax>7.4</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>4</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>3.875</watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Vilsandi</name>\n" +
                "        \t\t<wmocode>26214</wmocode>\n" +
                "        \t\t<longitude>21.81422222243415</longitude>\n" +
                "        \t\t<latitude>58.38277777711111</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>50.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>993.1</airpressure>\n" +
                "        \t\t<relativehumidity>62</relativehumidity>\n" +
                "        \t\t<airtemperature>10.7</airtemperature>\n" +
                "        \t\t<winddirection>129</winddirection>\n" +
                "        \t\t<windspeed>5.9</windspeed>\n" +
                "        \t\t<windspeedmax>9.3</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>577</sunshineduration>\n" +
                "        \t\t<globalradiation>250</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Sõrve</name>\n" +
                "        \t\t<wmocode>26218</wmocode>\n" +
                "        \t\t<longitude>22.0579083331426</longitude>\n" +
                "        \t\t<latitude>57.91359444406297</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>27.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>992.7</airpressure>\n" +
                "        \t\t<relativehumidity>83</relativehumidity>\n" +
                "        \t\t<airtemperature>5.1</airtemperature>\n" +
                "        \t\t<winddirection>102</winddirection>\n" +
                "        \t\t<windspeed>7.3</windspeed>\n" +
                "        \t\t<windspeedmax>8.5</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>567</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Ristna</name>\n" +
                "        \t\t<wmocode>26115</wmocode>\n" +
                "        \t\t<longitude>22.066300000084773</longitude>\n" +
                "        \t\t<latitude>58.92095277719588</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility>50.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>993.9</airpressure>\n" +
                "        \t\t<relativehumidity>56</relativehumidity>\n" +
                "        \t\t<airtemperature>11.3</airtemperature>\n" +
                "        \t\t<winddirection>101</winddirection>\n" +
                "        \t\t<windspeed>3</windspeed>\n" +
                "        \t\t<windspeedmax>7.3</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>-1</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>3.9</watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration>520</sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Haapsalu</name>\n" +
                "        \t\t<wmocode>26123</wmocode>\n" +
                "        \t\t<longitude>23.55527777777778</longitude>\n" +
                "        \t\t<latitude>58.945277777444446</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>995.5</airpressure>\n" +
                "        \t\t<relativehumidity>50</relativehumidity>\n" +
                "        \t\t<airtemperature>14.1</airtemperature>\n" +
                "        \t\t<winddirection>126</winddirection>\n" +
                "        \t\t<windspeed>3.6</windspeed>\n" +
                "        \t\t<windspeedmax>6.9</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex>0.2</uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation>225</globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Tooma</name>\n" +
                "        \t\t<wmocode>26147</wmocode>\n" +
                "        \t\t<longitude>26.27277777711111</longitude>\n" +
                "        \t\t<latitude>58.87277777711111</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>998.1</airpressure>\n" +
                "        \t\t<relativehumidity>56</relativehumidity>\n" +
                "        \t\t<airtemperature>13</airtemperature>\n" +
                "        \t\t<winddirection>150</winddirection>\n" +
                "        \t\t<windspeed>5.4</windspeed>\n" +
                "        \t\t<windspeedmax>7.9</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Naissaare</name>\n" +
                "        \t\t<wmocode>26034</wmocode>\n" +
                "        \t\t<longitude>24.563333333333333</longitude>\n" +
                "        \t\t<latitude>59.540833333</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity></relativehumidity>\n" +
                "        \t\t<airtemperature></airtemperature>\n" +
                "        \t\t<winddirection>142</winddirection>\n" +
                "        \t\t<windspeed>3.2</windspeed>\n" +
                "        \t\t<windspeedmax>5.5</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Vaindloo</name>\n" +
                "        \t\t<wmocode></wmocode>\n" +
                "        \t\t<longitude>26.361111111111114</longitude>\n" +
                "        \t\t<latitude>59.81694444377778</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity></relativehumidity>\n" +
                "        \t\t<airtemperature></airtemperature>\n" +
                "        \t\t<winddirection>110</winddirection>\n" +
                "        \t\t<windspeed>6.4</windspeed>\n" +
                "        \t\t<windspeedmax>7.4</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Osmussaare</name>\n" +
                "        \t\t<wmocode>26027</wmocode>\n" +
                "        \t\t<longitude>23.360555555555557</longitude>\n" +
                "        \t\t<latitude>59.303888888888885</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity></relativehumidity>\n" +
                "        \t\t<airtemperature></airtemperature>\n" +
                "        \t\t<winddirection>145</winddirection>\n" +
                "        \t\t<windspeed>4.4</windspeed>\n" +
                "        \t\t<windspeedmax>6.1</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000></waterlevel_eh2000>\n" +
                "        \t\t<watertemperature></watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Paldiski (Põhjasadam)</name>\n" +
                "        \t\t<wmocode>86100</wmocode>\n" +
                "        \t\t<longitude>24.04749999966667</longitude>\n" +
                "        \t\t<latitude>59.34972222188889</latitude>\n" +
                "        \t\t<phenomenon></phenomenon>\n" +
                "        \t\t<visibility></visibility>\n" +
                "        \t\t<precipitations></precipitations>\n" +
                "        \t\t<airpressure></airpressure>\n" +
                "        \t\t<relativehumidity></relativehumidity>\n" +
                "        \t\t<airtemperature></airtemperature>\n" +
                "        \t\t<winddirection>142</winddirection>\n" +
                "        \t\t<windspeed>4.5</windspeed>\n" +
                "        \t\t<windspeedmax>5.7</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>-4</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>3.3</watertemperature>\n" +
                "        \t\t<uvindex></uvindex>\n" +
                "        \t\t<sunshineduration></sunshineduration>\n" +
                "        \t\t<globalradiation></globalradiation>\n" +
                "        \t</station>\n" +
                "\t<station>\n" +
                "        \t\t<name>Pärnu</name>\n" +
                "        \t\t<wmocode>41803</wmocode>\n" +
                "        \t\t<longitude>24.485197221899487</longitude>\n" +
                "        \t\t<latitude>58.38456666634923</latitude>\n" +
                "        \t\t<phenomenon>Test 3</phenomenon>\n" +
                "        \t\t<visibility>32.0</visibility>\n" +
                "        \t\t<precipitations>0</precipitations>\n" +
                "        \t\t<airpressure>996.2</airpressure>\n" +
                "        \t\t<relativehumidity>50</relativehumidity>\n" +
                "        \t\t<airtemperature>14</airtemperature>\n" +
                "        \t\t<winddirection>95</winddirection>\n" +
                "        \t\t<windspeed>5.3</windspeed>\n" +
                "        \t\t<windspeedmax>8.3</windspeedmax>\t\t<waterlevel></waterlevel>\t\t<waterlevel_eh2000>-6</waterlevel_eh2000>\n" +
                "        \t\t<watertemperature>4.6</watertemperature>\n" +
                "        \t\t<uvindex>0.1</uvindex>\n" +
                "        \t\t<sunshineduration>593</sunshineduration>\n" +
                "        \t\t<globalradiation>231</globalradiation>\n" +
                "        \t</station>\n" +
                "</observations>";

        List<WeatherStation> stations = WeatherStationController.parseWeatherData(testInfo);
        assertEquals(3, stations.size());

        assertEquals(26038, stations.get(0).getWmoCode());
        assertEquals(26242, stations.get(1).getWmoCode());
        assertEquals(41803, stations.get(2).getWmoCode());

        assertEquals("Tallinn-Harku", stations.get(0).getNameOfTheStation());
        assertEquals("Tartu-Tõravere", stations.get(1).getNameOfTheStation());
        assertEquals("Pärnu", stations.get(2).getNameOfTheStation());

        assertEquals(13.6, stations.get(0).getAirTemperature(), 0.0000000001);
        assertEquals(12.8, stations.get(1).getAirTemperature(), 0.0000000001);
        assertEquals(14.0, stations.get(2).getAirTemperature(), 0.0000000001);

        assertEquals(3.8, stations.get(0).getWindSpeed(), 0.0000000001);
        assertEquals(5.1, stations.get(1).getWindSpeed(), 0.0000000001);
        assertEquals(5.3, stations.get(2).getWindSpeed(), 0.0000000001);

        assertEquals(" ", stations.get(0).getWeatherPhenomenon());
        assertEquals("Test 2", stations.get(1).getWeatherPhenomenon());
        assertEquals("Test 3", stations.get(2).getWeatherPhenomenon());

        assertEquals(1711640898, stations.get(0).getTimestamp());
        assertEquals(1711640898, stations.get(1).getTimestamp());
        assertEquals(1711640898, stations.get(2).getTimestamp());
    }
}
