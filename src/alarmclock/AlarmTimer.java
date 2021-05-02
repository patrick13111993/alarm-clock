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
import javax.swing.*;


public class AlarmTimer implements Observer {
    
    long seconds;
    String name = "Default Alarm";
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
            seconds = -1;
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(frame, "Alarm:", name, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
