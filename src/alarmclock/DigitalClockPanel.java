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
import javax.swing.*;

/**
 *
 * @author patrick
 */
public class DigitalClockPanel extends ClockPanel {
    
    Model model;
    
    public DigitalClockPanel(Model m) {
        Model model = m;
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.white);
    }
    
    @Override
    public void paintComponent(Graphics g) {
    }
}
