package alarmclock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class DigitalView {
    
    DigitalClockPanel digitalpanel;
    
    public DigitalView(Model model) {
        JFrame frame = new JFrame();
        frame.setJMenuBar(createMenuBar());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
//        JButton aboutButton = createAboutButton();

        digitalpanel = new DigitalClockPanel(model);
        model.addObserver(digitalpanel);
        contentPane.add(digitalpanel, BorderLayout.CENTER);
//      contentPane.add(aboutButton, BorderLayout.PAGE_END);
        
        
        frame.setContentPane(contentPane);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public JButton createAboutButton() {
        JButton aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(200, 100));
        AboutButtonHandler abh = new AboutButtonHandler();
        aboutButton.addActionListener(abh);
        return aboutButton;
    }
    
    public JMenuBar createMenuBar() { 
        JMenuBar menuBar = new JMenuBar();
        JMenu clockMenu = new JMenu("Clock");
        
        JMenuItem menuAbout = new JMenuItem("About");
        JRadioButtonMenuItem analogButton = createAnalogButton();
        JRadioButtonMenuItem digitalButton = createDigitalButton();
        
        AboutButtonHandler abh = new AboutButtonHandler();
        RadioButtonHandler rbh = new RadioButtonHandler();
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(analogButton);
        buttonGroup.add(digitalButton);
        
        menuAbout.addActionListener(abh);
        analogButton.addActionListener(rbh);        
        digitalButton.addActionListener(rbh);                
        
        clockMenu.add(menuAbout);
        clockMenu.add(analogButton);
        clockMenu.add(digitalButton);
        menuBar.add(clockMenu);
        return menuBar;
    }
    
    public JRadioButtonMenuItem createAnalogButton() {
        JRadioButtonMenuItem analogButton = new JRadioButtonMenuItem("Analog", true);
        analogButton.setPreferredSize(new Dimension(70, 30));
        RadioButtonHandler rbh = new RadioButtonHandler();
        analogButton.addActionListener(rbh);
        return analogButton;
    }
    
    public JRadioButtonMenuItem createDigitalButton() {
        JRadioButtonMenuItem digitalButton = new JRadioButtonMenuItem("Digital");
        digitalButton.setPreferredSize(new Dimension(70, 30));
        RadioButtonHandler rbh = new RadioButtonHandler();
        digitalButton.addActionListener(rbh);
        return digitalButton;
    }
    
} 

