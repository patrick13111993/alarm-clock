package alarmclock;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import queuemanager.OrderedLinkedListPriorityQueue;

public class Controller {
    
    ActionListener listener;
    Timer timer;
    
    Model model;
    View view;
    
    public Controller(Model m, View v) {
        model = m;
        view = v;
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
        
    }
}