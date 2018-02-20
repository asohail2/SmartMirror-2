/*
 * Collects new data from the New API
 * @see https://newsapi.org/
 * 
 */

/**
 *
 * @author kazec
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import com.google.gson.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;

public class NewsData {
    
   /*
    * Instance variables
    */
     
    // for API call:
    private final String newsURL = "https://newsapi.org/v1/articles";
    private String APIkey = "0c1d30d2a71a421989eec5b3af214a1c";
    private String cnnSource = "cnn";
    private String espnSource = "espn";
    private String techSource = "techcrunch";
    private String entSource = "entertainment-weekly";
    private String requestURL;
    private int count;
    private int numOfArticles;
    private int filled = 0;
    
    //for connection
    private HttpURLConnection request;
    
    //for return values
    private String[] title = new String[5];
    private String[] description = new String[5];
    
    public NewsData(int c){
        count = c;
        getJSON();
    }
    
   /*
    * Creates request URL
    */
    private String createURL() {
        int currentCount = count;
        String source = "";
        switch (currentCount){
            case 1: source = this.cnnSource;
            break;
            case 2: source = this.espnSource;
            break;
            case 3: source = this.techSource;
            break;
            case 4: source = this.entSource;
            break;
            default: source = this.cnnSource;
        }
        requestURL = newsURL + "?source=" + source + "&apiKey=" + APIkey;
        return requestURL;
    }
    
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
    * Convert Input Stream to JSON Object
    */
    private JsonObject streamToJSON(InputStream is) {
        JsonParser jp = new JsonParser(); // from gson
        JsonElement jsonElem = jp.parse(new InputStreamReader(is));	
        return jsonElem.getAsJsonObject(); // may be an array, may be an object.
    }
 
   /*
    * Parse the JsonObject and set title and description instance variables
    */
    private void parse(JsonObject obj) {
        //System.out.println(obj); //for reference. Delete when done
        JsonArray articles = obj.getAsJsonArray("articles");
        numOfArticles = articles.size();
        if (numOfArticles >= 5){
            for(int i = 0; i < 5; i++){
                this.title[i] = articles.get(i).getAsJsonObject().get("title").toString();
                this.description[i] = articles.get(i).getAsJsonObject().get("description").toString();
                this.filled = 5;
            }
        }
        else{
            for(int i = 0; i < numOfArticles; i++){
                this.title[i] = articles.get(i).getAsJsonObject().get("title").getAsString();
                this.description[i] = articles.get(i).getAsJsonObject().get("description").getAsString();  
                filled++;
        }

        }
               
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
    public String[] getTitle() {return this.title;}
    public String[] getDescription() {return this.description;}
    
    public JPanel createPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p , BoxLayout.Y_AXIS));
        for(int i = 0; i < filled; i++){
            //title label
            JTextArea l1 = new JTextArea(); 
            l1.setEditable(false);
            l1.setBackground(Color.BLACK);
            l1.setForeground(Color.WHITE);
            l1.setLineWrap(true);
            l1.setFont(new Font("Serif", Font.BOLD, 20));
            l1.setText(title[i]);
            p.add(l1);
            //description label
            JTextArea l2 = new JTextArea();
            l2.setEditable(false);
            l2.setForeground(Color.WHITE);
            l2.setBackground(Color.BLACK);
            l2.setLineWrap(true);
            l2.setFont(new Font("Serif", Font.ITALIC, 10));
            l2.setText("       " + description[i] + "\n");
            p.add(l2);
        }
        return p;
    }
}

    

