/*
 * Main for the Smart Mirror
 * 
 * 
 */

/**
 *
 * @author Chris Kazenske
 */

import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class SmartMirror implements Runnable {
    
    public final static int minBetweenCalls = 5;
    int count = 1;
    public static JFrame f;
    public static Display d;
    static WeatherData weather;
    static NewsData news;
    static StockData stock;
    public static HoverSensor hover;
    //public static HoverSensor hover;
    
    /**
     * @param args the command line arguments
     */
    public SmartMirror() {
        
    }
    
    public void run(){
        //WeatherData weather;
        //NewsData news;
        //StockData stock;
        //HoverSensor hover;
        while(true){
            try {
                weather = new WeatherData();
                news = new NewsData(count);
                stock = new StockData();
                
                d = new Display(weather, news, stock, f);
                f = d.getJFrame();
                f.setVisible(true);
                if (count < 4){
                    count++;
                }
                else{
                    count = 0;
                }               
                Thread.sleep(SmartMirror.minBetweenCalls*60000);  
            }
            catch (InterruptedException e1) {
                System.out.println("Error. Couldn't sleep."); 
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(SmartMirror.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        f = new JFrame();
        f.setUndecorated(true); //change to true when done
        new Thread(new SmartMirror()).start();  
        new Thread(hover = new HoverSensor()).start();
        
        
    
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
        // close the frame when the user presses escape
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);  
        }}; 
        f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        f.getRootPane().getActionMap().put("ESCAPE", escapeAction);
        
        f.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    //System.out.println("Right");
                    d.changeDisplayRight();
                }
                
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    //System.out.println("Left");
                    d.changeDisplayLeft();
                }
                
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    //System.out.println("Down");
                    d.changeQuote();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        
        });
        
        while(true){
            if(hover.getInputChangeStatus()){
                String in = hover.getInput();
                switch(in){
                    case "R": d.changeDisplayRight(); hover.setFalse(); f.repaint();
                        break;
                    case "L": d.changeDisplayLeft(); hover.setFalse(); f.repaint();
                        break;
                    case "D": d.changeQuote(); hover.setFalse(); f.repaint(); 
                        break;
                    //case "Something has gone wrong...:(": hover.run(); hover.setFalse();
                        //break;
                }
            }          
        }
    }
}
