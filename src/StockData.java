
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class StockData {
    private String APIKey = "&api_key=Ctznyv4hGC9n66rq_8fw";
    private int numOfStocks = 5;
    private String URL = "https://www.quandl.com/api/v3/datasets/WIKI/";
    private String[] ticker = new String[] { "AAPL", "F", "NVDA", "ADSK", "GE" };
    
    
    private double[] open = new double[numOfStocks];
    private double[] high = new double[numOfStocks];
    private double[] low = new double [numOfStocks];
    private double[] close = new double[numOfStocks];
    
    private HttpURLConnection request; //for connection
    private JsonObject[] object = new JsonObject [numOfStocks];
    private JPanel panel;
        
    public StockData(){
        parseJsonToVars();
        panel = createPanel();
    }
    
    public JPanel getPanel(){
        return this.panel;
    }
    
    /*
    The API returns nothing if you use current date, so we need to use yesterdays date.
    */
    public String createURL(int n){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdaysDate = date.format(c.getTime());
        return this.URL + this.ticker[n] + "/data.json?start_date=" + "2017-11-22" + this.APIKey;
    }
    
    public boolean connect(){
        boolean r = true;
        for(int i=0; i<5; i++){
            if(r = true){
                //System.out.println("Made it in the for loop for connect");
                try{
                    URL url = new URL(createURL(i));
                    this.request = (HttpURLConnection) url.openConnection();
                    this.request.connect();
                    this.object[i]= streamToJSON((InputStream) this.request.getContent());
                    //System.out.println("connected to index: " + i);
                }
                catch (Exception e) {
                System.out.println("Error: couldn't connect to url: " + e);
                r = false;
                }
            }
        }
        return r;
    }
    
    /*
    * Convert Input Stream to JSON Object
    */
    public JsonObject streamToJSON(InputStream is) {
        JsonParser jp = new JsonParser(); // from gson
        JsonElement jsonElem = jp.parse(new InputStreamReader(is));	
        return jsonElem.getAsJsonObject(); // may be an array, may be an object.
    }
    
    public  void parseJsonToVars(){
        if(connect()){     
            for(int i = 0; i < 5; i++){
                JsonObject obj = object[i];
                JsonObject main = obj.getAsJsonObject("dataset_data");
                JsonArray predata = main.getAsJsonArray("data");
                JsonArray data = predata.get(0).getAsJsonArray();
                this.open[i] = data.get(1).getAsDouble();
                this.high[i] = data.get(2).getAsDouble();
                this.low[i] = data.get(3).getAsDouble();
                this.close[i] = data.get(4).getAsDouble();               
            }
            
            //System.out.println(Arrays.toString(open));
            //System.out.println(Arrays.toString(high));
            //System.out.println(Arrays.toString(low));
            //System.out.println(Arrays.toString(close));
            
        }
    } 
    
    public JPanel createPanel(){
        JPanel p = new JPanel();
        //p.setLayout(new GridLayout(0,7));
        p.setLayout(new BorderLayout());
        p.setBackground(Color.BLACK);
        
        JLabel left_arrow = new JLabel("Left Arrow");
        left_arrow.setFont(new Font("Serif", Font.BOLD, 12));
        left_arrow.setForeground(Color.WHITE); 
        left_arrow.setIcon(new ImageIcon(getClass().getResource("/icons/arrow_left.png")));
               
        JLabel right_arrow = new JLabel("Right Arrow");
        right_arrow.setFont(new Font("Serif", Font.BOLD, 12));
        right_arrow.setForeground(Color.WHITE); 
        right_arrow.setIcon(new ImageIcon(getClass().getResource("/icons/arrow_right.png")));
        
        JTextArea text1 = new JTextArea();
        text1.setEditable(false);
        text1.setBackground(Color.BLACK);
        text1.setForeground(Color.WHITE);
        text1.setLineWrap(true);
        text1.setFont(new Font("Serif", Font.BOLD, 20)); 
        text1.setText(setPanelText(0));
        text1.setSize(p.getWidth() / 5, p.getHeight());
        
        JTextArea text2 = new JTextArea();
        text2.setEditable(false);
        text2.setBackground(Color.BLACK);
        text2.setForeground(Color.WHITE);
        text2.setLineWrap(true);
        text2.setFont(new Font("Serif", Font.BOLD, 20)); 
        text2.setText(setPanelText(1));
        text2.setSize(p.getWidth() / 5, p.getHeight());
        
        JTextArea text3 = new JTextArea();
        text3.setEditable(false);
        text3.setBackground(Color.BLACK);
        text3.setForeground(Color.WHITE);
        text3.setLineWrap(true);
        text3.setFont(new Font("Serif", Font.BOLD, 20)); 
        text3.setText(setPanelText(2));
        text3.setSize(p.getWidth() / 5, p.getHeight());

        JTextArea text4 = new JTextArea();
        text4.setEditable(false);
        text4.setBackground(Color.BLACK);
        text4.setForeground(Color.WHITE);
        text4.setLineWrap(true);
        text4.setFont(new Font("Serif", Font.BOLD, 20)); 
        text4.setText(setPanelText(3));
        text4.setSize(p.getWidth() / 5, p.getHeight());

        JTextArea text5 = new JTextArea();
        text5.setEditable(false);
        text5.setBackground(Color.BLACK);
        text5.setForeground(Color.WHITE);
        text5.setLineWrap(true);
        text5.setFont(new Font("Serif", Font.BOLD, 20)); 
        text5.setText(setPanelText(4));  
        text5.setSize(p.getWidth() / 5, p.getHeight());
         
        //panel for the data
        JPanel dp = new JPanel(new GridLayout(1,5));
        dp.setBackground(Color.BLACK);
        dp.add(text1);
        dp.add(text2);
        dp.add(text3);
        dp.add(text4);
        dp.add(text5);
        
        //p.add(left_arrow, BorderLayout.WEST); 
        p.add(dp, BorderLayout.CENTER);
        //p.add(right_arrow, BorderLayout.EAST);
        
        return p;
    }
    
    public String setPanelText(int n){
        return this.ticker[n] + "\n\nOPEN: " + this.open[n] + "\nHIGH " + this.high[n] + "\nLOW: " + this.low[n] + "\nCLOSE: " + this.close[n];
        
    }
}
