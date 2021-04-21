package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock {
    
    public static void main(String[] args) {
        Model model = new Model();
        AnalogView analogView = new AnalogView(model);
        DigitalView digitalView = new DigitalView(model);     
        
        Controller controller = new Controller(model, analogView); 
    }
}
