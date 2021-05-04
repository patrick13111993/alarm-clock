/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import queuemanager.QueueUnderflowException;


public class AlarmTimer implements Observer {
    
    long seconds;
    String name = "Alarm";
    Model model;
    Date datetime;
    Frame frame;
    
    public AlarmTimer(Date d, Frame aFrame, View parent) {
        frame = aFrame;
        datetime = d;
        model = parent.model;
        Date now = model.datetime;
        seconds = (datetime.getTime()-now.getTime())/1000;
 
    }

    @Override
    public void update(Observable o, Object o1) {
        seconds -=1;
        if (seconds == 0) {
//            Alarm goes off
            seconds = -1;
            Toolkit.getDefaultToolkit().beep();
            try {
                model.queue.remove();
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(AlarmTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
//            Remove alarm icon when alarm goes off
            JOptionPane.showMessageDialog(frame, "Alarm", name, JOptionPane.INFORMATION_MESSAGE);
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(0,0));
            frame.add(emptyLabel,BorderLayout.LINE_END);
            frame.pack();
        }
    }
}
