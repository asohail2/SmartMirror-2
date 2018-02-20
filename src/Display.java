/*
 * 
 * Creates the display for the Smart Mirror
 * 
 */

/**
 *
 * @author Chris Kazenske
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.LineBorder;


public class Display {

    private WeatherData weather;
    private NewsData news;
    private StockData stock;
    private String sunriseTime;
    private String sunsetTime;
    private String maxTemp;
    private String minTemp;
    private String precProb;
    private String updatedTime;
    private JFrame f;
    private GridBagConstraints c;
    private JPanel p1;
    private JPanel cardPanel;
    private CardLayout c1;
    private String[] quotes = {"\"In union there is strength.\"", 
        "\"The things that we share in out world are far more valuable than those which divide us.\"",
        "\"It is not in numbers, but in unity, that our greatest strength lies.\""};
    private int quoteNum = 0;
    private JLabel quoteLabel;
    private JPanel quotePanel;
    
    public Display(WeatherData i, NewsData j, StockData s, JFrame p) throws IOException{

    weather = i;
    news = j;
    stock = s;
    f = p;
    
    
    
    /*
     * First section: Current info, top left area of p1
     */    
    //displayed clock
    ClockLabel clock = new ClockLabel();

    //displayed date
    JLabel date = new JLabel(createDate());
    date.setFont(new Font("Serif", Font.BOLD, 32));
    date.setForeground(Color.WHITE);
    date.setHorizontalAlignment(SwingConstants.LEFT);
    date.setVerticalAlignment(SwingConstants.CENTER);
    
    //displayed current temp and icon
    JLabel temp = new JLabel();
    temp.setText(Integer.toString(weather.getTemp()) + "\u00B0");
    temp.setFont(new Font("Serif", Font.BOLD, 48));
    temp.setForeground(Color.WHITE);
    temp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    temp.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
    temp.setIcon(createIcon());
    temp.setIconTextGap(50);
    
    /* 
     * Second section: Forecast info
     */
    setDailyValues();
    //display Hi and Lo
    JLabel hilo = new JLabel("HI: " + maxTemp + "\u00B0" + "    " + "LO: " + minTemp + "\u00B0");
    hilo.setFont(new Font("Serif", Font.BOLD, 48));
    hilo.setForeground(Color.WHITE);
    hilo.setHorizontalAlignment(SwingConstants.RIGHT);    
    
    //display sunrise and sunset
    JLabel container = new JLabel();
    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
    JLabel uSunrise = new JLabel(sunriseTime + "     ");
    uSunrise.setIcon(new ImageIcon(getClass().getResource("/icons/sunrise.png")));
    uSunrise.setFont(new Font("Serif", Font.BOLD, 30));
    uSunrise.setForeground(Color.WHITE); 
    uSunrise.setHorizontalAlignment(SwingConstants.RIGHT); 
    JLabel uSunset = new JLabel(sunsetTime);
    uSunset.setIcon(new ImageIcon(getClass().getResource("/icons/sunset.png")));
    uSunset.setFont(new Font("Serif", Font.BOLD, 30));
    uSunset.setForeground(Color.WHITE); 
    uSunset.setHorizontalAlignment(SwingConstants.RIGHT); 
    container.add(uSunrise);
    container.add(uSunset);
    
    //display last time updated
    JLabel uTime = new JLabel(updatedTime);
    uTime.setFont(new Font("Serif", Font.BOLD, 24));
    uTime.setForeground(Color.WHITE);
    uTime.setHorizontalAlignment(SwingConstants.RIGHT);  
    
    /*
     *  Main frame
     */
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    
    /*
     * Panels
     */

    
    //p2 goes in TOP LEFT of p1 layout
    JPanel p2 = new JPanel();
    p2.setLayout(new GridLayout(6,1));
    p2.setBackground(Color.BLACK);
    //p2.setBorder(new LineBorder(Color.WHITE)); 
    p2.setAlignmentX(Component.LEFT_ALIGNMENT);
    p2.setAlignmentY(Component.TOP_ALIGNMENT);
    p2.add(date);
    p2.add(clock);
    p2.add(temp);
    
    //p3 goes in TOP RIGHT of p1 layout
    JPanel p3 = new JPanel();
    p3.setLayout(new GridLayout(6,1));
    p3.setBackground(Color.BLACK);
    //p3.setBorder(new LineBorder(Color.WHITE)); 
    p3.setAlignmentX(Component.RIGHT_ALIGNMENT);
    p3.setAlignmentY(Component.TOP_ALIGNMENT);
    p3.add(hilo);
    p3.add(container);
    p3.add(uTime);
    
    //newsPanel and stockPanel go on a card layout at the bottom 
    JPanel newsPanel = j.createPanel();
    newsPanel.setBackground(Color.BLACK);
    
    JPanel stockPanel = s.createPanel();
    stockPanel.setBackground(Color.BLACK);
    
    //helloPanel goes first on the CardLayout and displays "Hello" when the program is run
    JPanel helloPanel = new JPanel(new BorderLayout());
    JLabel helloLabel = new JLabel("Hello!");
    helloLabel.setForeground(Color.WHITE);
    helloLabel.setFont(new Font("Serif", Font.BOLD, 48));
    helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
    helloLabel.setVerticalAlignment(SwingConstants.CENTER);
    helloPanel.add(helloLabel);
    
    //quotePanel goes after stocks display and displays quotes. When the user swipes down, the quote changes.
    quotePanel = new JPanel(new BorderLayout());
    quoteLabel = new JLabel("<html>" + quotes[quoteNum] + "</html>");
    quotePanel.setBackground(Color.BLACK);
    quoteLabel.setForeground(Color.WHITE);
    quoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
    quoteLabel.setVerticalAlignment(SwingConstants.CENTER);
    quoteLabel.setFont(new Font("Serif", Font.BOLD, 36));
    quotePanel.add(quoteLabel);
    
    //p1 goes on the main frame and sets the layout
    p1 = new JPanel();
    p1.setBackground(Color.BLACK);
    p1.setBorder(new EmptyBorder(10, 10, 10, 10));
    p1.setLayout(new GridBagLayout());
    c = new GridBagConstraints();
    
    //set constraints and add p2
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.weightx = 1.0;
    c.weighty = 1.0;
    p1.add(p2,c);
    
    //set constraints and add p3
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.NORTHEAST;
    p1.add(p3,c);
    
    //set constraints and add card panel
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.CENTER;
    
    
    
    //create cardPanel to go on top of p1 and set layout to CardLayout
    c1 = new CardLayout();
    cardPanel = new JPanel();
    cardPanel.setBackground(Color.BLACK);
    //cardPanel.setBorder(new LineBorder(Color.WHITE));
    cardPanel.setPreferredSize(new Dimension(1800,1440));
    cardPanel.setLayout(c1);
    cardPanel.add(helloLabel);
    cardPanel.add(newsPanel);
    cardPanel.add(stockPanel);
    cardPanel.add(quotePanel);
    p1.add(cardPanel, c);
    
    f.add(p1);
    }
    
    public void changeDisplayRight(){
        c1.next(cardPanel);
    }
    
    public void changeDisplayLeft(){
        c1.previous(cardPanel);
    }
    
    public void changeQuote(){
        if (quoteNum < (quotes.length - 1)){
            quoteNum++;                     
        }
        else{
            quoteNum = 0;
        }
        quoteLabel.setText("<html>" + quotes[quoteNum] + "</html>");  
    }
    /*
     * Create data format
     */
    private String createDate(){
        SimpleDateFormat date = new SimpleDateFormat("EEE, MMM d, yyyy");
        String stringDate = date.format(new Date());   
        return stringDate;
    }
    
    /*
     * Create icon
    */
    private ImageIcon createIcon(){
        String s = weather.getIcon();
        String icon;
        switch (s){
            case "clear-day": icon = "/icons/Sunny-64.png";
                            break;
            case "clear-night": icon = "/icons/Sunny-64.png";
                            break; 
            case "rain": icon = "/icons/rain-64.png";
                            break;
            case "sleet": icon = "/icons/rain-64.png";
                            break; 
            case "snow": icon = "/icons/snow-64.png";
                            break;                             
            case "wind": icon = "/icons/fog-64.png";
                            break; 
            case "fog": icon = "/icons/fog-64.png";
                            break; 
            case "cloudy": icon = "/icons/Cloudy-64.png";
                            break; 
            case "partly-cloudy-day": icon = "/icons/Sunny-Interval-64.png";
                            break; 
            case "partly-cloudy-night": icon = "/icons/Sunny-Interval-64.png"; 
                            break; 
            default: icon = "/icons/default-64.png";
        }
        return new ImageIcon(getClass().getResource(icon));
    }
    /*
     * Set daily values
     */
    private void setDailyValues(){
        //set updateTime
        SimpleDateFormat t = new SimpleDateFormat("h:mm:ss a");
        updatedTime = "Last updated: " + t.format(new Date(System.currentTimeMillis()));
        
        //set maxTemp and minTemp
        maxTemp = Integer.toString(weather.getMaxTemp());
        minTemp = Integer.toString(weather.getMinTemp());
        
        //set sunriseTime and sunsetTime
        SimpleDateFormat sr = new SimpleDateFormat("h:mm a");
        sunriseTime = sr.format(new Date(weather.getSunrise()*1000));
        SimpleDateFormat ss = new SimpleDateFormat("h:mm a");
        sunsetTime = sr.format(new Date(weather.getSunset()*1000));
    }
    
    public JFrame getJFrame(){
        return f;
    }

    
   
}