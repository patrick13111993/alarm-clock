/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class AlarmTimer extends JPanel implements Observer {
    
    Model model;
    AlarmDialog dialog;
    long seconds;
    String name = "Alarm";
    
    public AlarmTimer(Model m, Date date, AlarmDialog d) {
        dialog = d;
        model = m;
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.white);
        Date now = model.datetime;
        seconds = (date.getTime()-now.getTime())/1000;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        String secondString = String.format("%02d", seconds);
        
        Graphics2D gg = (Graphics2D) g;
        Font clockFont = new Font("LCD", Font.BOLD, 48);
        gg.setFont(clockFont);
        gg.drawString(secondString,1,64); 
    }

    @Override
    public void update(Observable o, Object o1) {
        this.repaint();
        seconds -=1;
        if (seconds == 0) {
            JOptionPane.showMessageDialog(this, name);
            dialog.setVisible(false);
        }
    }
}
