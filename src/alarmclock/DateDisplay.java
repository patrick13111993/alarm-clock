/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import queuemanager.QueueUnderflowException;

/**
 *
 * @author patrick
 */
public class DateDisplay extends JPanel implements Observer {

    Model model;
    
    public DateDisplay(Model m) {
        model = m;
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.white);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(model.datetime);
        
        Graphics2D gg = (Graphics2D) g;
        Font clockFont = new Font("Sans Serif", Font.BOLD, 32);
        gg.setFont(clockFont);
        gg.drawString(today,12,64); 
        
//        Check for alarms
        if(!model.queue.isEmpty()) {
            try {
//                If alarms exists, display text stating when next alarm will go off
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Font alarmFont = new Font("Sans Serif", Font.BOLD, 12);
                gg.setFont(alarmFont);
                AlarmTimer timer = model.queue.head();
                gg.drawString("Next Alarm: " + df2.format(timer.datetime), 12, 32);
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(DateDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    @Override
    public void update(Observable o, Object o1) {
        this.repaint();
    }
    
}
