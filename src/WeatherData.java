/**
 * Collects weather data from the Dark Sky API 
 * @see https://darksky.net/poweredby/
 * 
 */

/**
 *
 * @author Chris Kazenske
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;

public class WeatherData {

   /*
    * Instance variables
    */
     
    // for API call:
    private final String webURL = "https://api.darksky.net/forecast/";
    private String APIkey = "26aba84df3b5199129d5f40e44aa243f";
    private String latitude = "33.818225";
    private String longitude= "-84.366463";
    private String requestURL;
    
    //for return values
    private long sunrise = 0;
    private long sunset = 0;
    private float temp = 0;
    private float maxTemp = 0;
    private float minTemp = 0;
    private String icon = "";
    private float precProbability = 0; //chance of precipitation
    
    //for connection
    private HttpURLConnection request;
   
    /* 
     * Constructor
     */
    
    public WeatherData() { 
        getJSON();
   
    }
  
   //***************Private Methods************************************************************
    
   /*
    * Connect to URL
    */ 
    private boolean connect() {
        try {
        URL url = new URL(createURL());
         this.request = (HttpURLConnection) url.openConnection();
         this.request.connect();
        } 
        catch (Exception e) {
            System.out.println("Error: couldn't connect to url: " + e);
            return false;
        }
        return true; 
   }

   /*
    * Creates Pull Request URL to retrieve data
    */
    private String createURL() {
        requestURL = webURL + APIkey + "/" + latitude + "," + longitude;
        return requestURL;
    }
   
   /*
    * Convert Input Stream to JSON Object
    */
    private JsonObject streamToJSON(InputStream is) {
        JsonParser jp = new JsonParser(); // from gson
        JsonElement jsonElem = jp.parse(new InputStreamReader(is));	
        return jsonElem.getAsJsonObject(); // may be an array, may be an object.
    }
    
   /*
    * Parse the data
    */
    private void parse(JsonObject obj) {
        
        //get currently data
        JsonObject main = obj.getAsJsonObject("currently");
        this.icon = main.get("icon").getAsString();
        this.temp = main.get("temperature").getAsFloat();
        this.precProbability = main.get("precipProbability").getAsFloat();
        
        //get daily data
        JsonObject daily = obj.getAsJsonObject("daily");
        JsonArray dailyData = daily.getAsJsonArray("data");
        this.maxTemp = dailyData.get(0).getAsJsonObject().get("temperatureMax").getAsFloat();
        this.minTemp = dailyData.get(0).getAsJsonObject().get("temperatureMin").getAsFloat();
        this.sunrise = dailyData.get(0).getAsJsonObject().get("sunriseTime").getAsLong();
        this.sunset = dailyData.get(0).getAsJsonObject().get("sunsetTime").getAsLong();
        
       /*
         Reference only
        System.out.println("Current condition: " + icon);
        System.out.println("Current temperature: " + temp);
        System.out.println("Today's high: " + maxTemp);
        System.out.println("Today's low: " + minTemp);
        System.out.println("Chance of precipitation: " + precProbability + "%");
        System.out.println("Today's sunrise: " + sunrise);
        System.out.println("Today's sunset: " + sunset);
        System.out.println("Condition icon: " + icon);
        */
    }
    
   /*
    * To be called in the constructor
    */
    private void getJSON() {
        JsonObject obj = null;
        if (connect()) {
            try {
                obj = streamToJSON((InputStream) this.request.getContent()); 
            }
            catch (IOException e) {
                System.out.println("Error: could not convert stream to json element: " + e);
            }
        }
        parse(obj);
       
    }
   
   //*************************Public Methods***************************************************
   
    /*
     * get methods
     */
    public long getSunrise() {return this.sunrise;} 
    public long getSunset() {return this.sunset;}
    public int getTemp() {return Math.round(temp);}
    public int getMaxTemp() {return Math.round(maxTemp);}
    public int getMinTemp() {return Math.round(minTemp);}     
    public String getIcon() {return this.icon;}
    public float getPrecProbability() {return this.precProbability;}

}  