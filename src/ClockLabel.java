/*
 * 
 * 
 * 
 */

/**
 *
 * @author Chris Kazenske
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.text.SimpleDateFormat;

public class ClockLabel extends JLabel  { 

    String time;
    
    public ClockLabel(){
        time = createTime();
        setFont(new Font("Serif", Font.BOLD, 64));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        setText(time);
        ActionListener taskPerformed = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                time = createTime();
                setText(time);
            }
        };        
        Timer t = new Timer(1000, taskPerformed);
        t.start();
    }
    
    private String createTime(){
        SimpleDateFormat time = new SimpleDateFormat("h:mm a"); //maybe get rid of seconds??
        String stringTime = time.format(new Date());  
        return stringTime;
    }
}
