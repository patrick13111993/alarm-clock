package alarmclock;

import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    ClockPanel panel;
    
    public View(Model model) {
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JButton aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(200, 100));
        panel = new ClockPanel(model);
        contentPane.add(panel, BorderLayout.PAGE_START);
        contentPane.add(aboutButton, BorderLayout.PAGE_END);
        frame.setContentPane(contentPane);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}
