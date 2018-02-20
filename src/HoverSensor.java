import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HoverSensor implements Runnable {
    
    private String command = "/home/pi/hover_raspberrypi-master/Hover_example.py";
    private String input = "";
    private Scanner in;
    private boolean inputChange = false;
    private ProcessBuilder pb;
    private Process p;
    
    public HoverSensor() throws IOException{

    }
    
    public void restart() throws IOException{
        ProcessBuilder pb = new ProcessBuilder("sudo", "python", this.command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        this.in = new Scanner(p.getInputStream());  
        while(in.hasNextLine()){
            input = in.nextLine();
            System.out.println(input);
            this.inputChange = true;           
        }
    }
    
    public boolean getInputChangeStatus(){
        return inputChange;
    }
    
    public void setFalse(){
        this.inputChange = false;
    }
    
    public String getInput(){
        return this.input;
    }

    @Override
    public void run() {
        try {
            pb = new ProcessBuilder("sudo", "python", this.command);
            pb.redirectErrorStream(true);
            p = pb.start();
            this.in = new Scanner(p.getInputStream());
            while(in.hasNextLine()){              
                input = in.nextLine();
                System.out.println(input); 
                if(input.equals("R") || input.equals("L") || input.equals("D")){   
                    this.inputChange = true;
                }              
            }
        } catch (IOException ex) {
            Logger.getLogger(HoverSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
