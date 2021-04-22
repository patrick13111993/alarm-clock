package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock {
    
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);    
        
        Controller controller = new Controller(model, view); 
    }
}
