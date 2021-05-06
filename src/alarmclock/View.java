package alarmclock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import queuemanager.OrderedLinkedListPriorityQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class View {
    
    Model model;
    Boolean analog = true;
    Boolean initialframe = true;
    CalendarCRUD crud = new CalendarCRUD();
    
    public View(Model model) {
        this.model = model;
        createFrame(model);        
    }
    
    public void createFrame(Model model) {
        JFrame frame = new JFrame();
        
//        Load Ask user to load previous alarms
        if(initialframe==true) {
            initialframe=false;
            int reply = JOptionPane.showConfirmDialog(null, "Load alarms?", "Load previously saved alarms", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (reply == JOptionPane.YES_OPTION) {
                try {
    //                open file selector
                    crud.read(model, frame, this);
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }


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
        
//        ask user to save alarm data upon application closing
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Save alarms?", "Save before exiting?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (reply == JOptionPane.YES_OPTION) {
                    crud.write(model);
                } else {
                    System.exit(0);
                }
            }
        });
        
        System.out.println(model.queue.length());
        //  Following adapted from: https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
        if(model.queue.length() > 0) {
            BufferedImage myPicture;
            try {
                //  Show icon on parent frame when an alarm is set
                myPicture = ImageIO.read(new File("images/alarm.png"));
                JButton picButton = new JButton(new ImageIcon(myPicture));
                picButton.setPreferredSize(new Dimension(40, 40));
                EditAlarmHandler handler = new EditAlarmHandler(frame, this);
                picButton.addActionListener(handler);

                frame.add(picButton, BorderLayout.LINE_END);
                frame.pack();
            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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


