/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclock;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 *
 * @author patrick
 */
public class DigitalClockPanel extends ClockPanel {
    
    Model model;
    
    public DigitalClockPanel(Model m) {
        model = m;
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.white);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
//        String[] timeArray = {String.valueOf(model.hour), String.valueOf(model.minute), String.valueOf(model.second)};
//        for (String number : timeArray) {
//            if(number.length() == 1) {
//                number = "0" + number;
//            }
//        }
        String hour = String.format("%02d", model.hour);
        String minute = String.format("%02d", model.minute);
        String second = String.format("%02d", model.second);
        
        Graphics2D gg = (Graphics2D) g;
        Font clockFont = new Font("LCD", Font.BOLD, 48);
        gg.setFont(clockFont);
        String time = (hour + ":" + minute + ":" + second);
        gg.drawString(time,1,64); 
    }

    @Override
    public void update(Observable o, Object o1) {
        this.repaint();
    }
}
