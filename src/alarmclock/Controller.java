package alarmclock;

import java.awt.event.*;
import javax.swing.Timer;

public class Controller {
    
    ActionListener listener;
    Timer timer;
    
    Model model;
    AnalogView analogView;
    DigitalView digitalView;
    
    public Controller(Model m, AnalogView av) {
        model = m;
        analogView = av;
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
        
        
    }
    
    public Controller(Model m, DigitalView dv) {
        model = m;
        digitalView = dv;
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
        
        
    }
}