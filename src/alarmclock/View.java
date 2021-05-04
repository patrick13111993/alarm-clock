package alarmclock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import queuemanager.OrderedLinkedListPriorityQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class View {
    
    Boolean analog = true;
    Model model;
    
    public View(Model model) {
        this.model = model;
        createFrame(model);        
    }
    
    public void createFrame(Model model) {
        JFrame frame = new JFrame();
        frame.setJMenuBar(createMenuBar(frame));
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
//        JButton aboutButton = createAboutButton();
        DateDisplay datedisplay = new DateDisplay(model);
        model.addObserver(datedisplay);
        contentPane.add(datedisplay, BorderLayout.PAGE_END);
        
        if (analog) {
            AnalogClockPanel analogpanel = new AnalogClockPanel(model);
            model.addObserver(analogpanel);
            contentPane.add(analogpanel, BorderLayout.CENTER);
        } else {
            DigitalClockPanel digitalpanel = new DigitalClockPanel(model);
            model.addObserver(digitalpanel);
            contentPane.add(digitalpanel, BorderLayout.CENTER);        
        }

//        contentPane.add(aboutButton, BorderLayout.PAGE_END);
        
        frame.setContentPane(contentPane);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public JMenuBar createMenuBar(JFrame frame) { 
        JMenuBar menuBar = new JMenuBar();
        JMenu clockMenu = new JMenu("Clock");
        
        JMenuItem menuAbout = new JMenuItem("About");
        JMenuItem menuAlarm = new JMenuItem("Set Alarm");
        JMenuItem analogButton = createAnalogButton();
        JMenuItem digitalButton = createDigitalButton();
        
        AboutButtonHandler abh = new AboutButtonHandler();
        AlarmButtonHandler albh = new AlarmButtonHandler(frame, this);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(analogButton);
        buttonGroup.add(digitalButton);
        
        menuAbout.addActionListener(abh); 
        menuAlarm.addActionListener(albh);
        
        clockMenu.add(menuAbout);
        clockMenu.add(analogButton);
        clockMenu.add(digitalButton);
        clockMenu.add(menuAlarm);
        menuBar.add(clockMenu);
        return menuBar;
    }
    
    public JMenuItem createAnalogButton() {
        JMenuItem analogButton = new JMenuItem("Analog");
        analogButton.setPreferredSize(new Dimension(80, 40));
        NewClockMenuItemHandler nch = new NewClockMenuItemHandler();
        analogButton.addActionListener(nch);
        return analogButton;
    }
    
    public JMenuItem createDigitalButton() {
        JMenuItem digitalButton = new JMenuItem("Digital");
        digitalButton.setPreferredSize(new Dimension(80, 40));
        NewClockMenuItemHandler nch = new NewClockMenuItemHandler();
        digitalButton.addActionListener(nch);
        return digitalButton;
    }
    
    class NewClockMenuItemHandler implements ActionListener  {

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand()=="Analog") {
            View.this.analog = true;
        } else {
            View.this.analog = false;
        }
        View.this.createFrame(model);
        }  
    } 
    
    
//    public JButton createAboutButton() {
//        JButton aboutButton = new JButton("About");
//        aboutButton.setPreferredSize(new Dimension(200, 100));
//        AboutButtonHandler abh = new AboutButtonHandler();
//        aboutButton.addActionListener(abh);
//        return aboutButton;
//    }
}  


