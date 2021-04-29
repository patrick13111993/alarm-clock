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
import java.awt.Window;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class AlarmTimer extends JPanel implements Observer {
    
    long seconds;
    String name = "Alarm";
    Model model;
    
    public AlarmTimer(Date date, View parent) {
        model = parent.model;
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.white);
        Date now = model.datetime;
        seconds = (date.getTime()-now.getTime())/1000;
        
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        contentPane.add(this);
        
        frame.setContentPane(contentPane);
        frame.setTitle("Alarm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        seconds -=1;
        System.out.println(seconds);
        if (seconds == 0) {
            JOptionPane.showMessageDialog(this, name);
        }
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
    }
}
