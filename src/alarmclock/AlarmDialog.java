package alarmclock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DateFormatter;
import queuemanager.OrderedLinkedListPriorityQueue;



public class AlarmDialog extends JDialog implements ActionListener, PropertyChangeListener {
                    
    private View parent;
    private JFrame frame;

    private JOptionPane optionPane;
    private JSpinner spinner;

    private String btnString1 = "OK";
    private String btnString2 = "Cancel";

//    Adapted from: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/DialogDemoProject/src/components/CustomDialog.java
    public AlarmDialog(JFrame aFrame, View p) {
        super(aFrame, true);
        frame = aFrame;
        parent = p;

        setTitle("Set Alarm");
       
        Object[] options = {btnString1, btnString2};
        
//      Source: https://stackoverflow.com/questions/21960236/jspinner-time-picker-model-editing  
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(parent.model.datetime);

        spinner = new JSpinner(model);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);

        spinner.setEditor(editor);


        //Create the JOptionPane.
        optionPane = new JOptionPane(spinner,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    JOptionPane.OK_CANCEL_OPTION,
                                    null,
                                    options,
                                    options[0]);
        
        optionPane.setValue(spinner.getValue());
        
        //Make this dialog display it.
        setContentPane(optionPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                spinner.requestFocusInWindow();
            }
        });

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = dateFormat.format(spinner.getValue());
                Date alarmdate = (Date)spinner.getValue();
                Calendar baselinedate = new GregorianCalendar(2020, 1, 1);
                Date baseline = baselinedate.getTime();
                
//              NB Could not find a better way to achieve this. Will probably cause an integer overflow on February 5th 2088 unless baseline date is updated. 
//              Potentially worthwhile setting an an alarm for February 4th 2088 as a reminder
                int priority = 0 - ((int) ((alarmdate.getTime()-baseline.getTime())/1000));
                
                int datedifference = ((int) ((alarmdate.getTime()-parent.model.datetime.getTime())/1000));
                
//                Create alarm only if time entered is in the future
                if(datedifference > 0) {
                
                    JOptionPane.showMessageDialog(
                        AlarmDialog.this,
                        "Alarm set for: " + dateString,
                        "Alarm set",
                        JOptionPane.INFORMATION_MESSAGE);

                    AlarmTimer timer = new AlarmTimer(alarmdate, frame, parent);
                    parent.model.addObserver(timer);
                    parent.model.queue.add(timer, priority);

    //                Following adapted from: https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
                    BufferedImage myPicture;
                    try {
    //                    Show icon on parent frame when an alarm is set
                        myPicture = ImageIO.read(new File("images/alarm.png"));
                        JButton picButton = new JButton (new ImageIcon(myPicture));
                        picButton.setPreferredSize(new Dimension(40,40));
                        EditAlarmHandler handler = new EditAlarmHandler(frame, parent);
                        picButton.addActionListener(handler);
                        
                        frame.add(picButton,BorderLayout.LINE_END);
                        frame.pack();
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                } else {
                    JOptionPane.showMessageDialog(
                    AlarmDialog.this,
                    "Error: " + dateString + " is not in the future!",
                    "Invalid input",
                    JOptionPane.INFORMATION_MESSAGE);
                }
            } else { //user closed dialog or clicked cancel
            setVisible(false);
            }
        }
    }
}


