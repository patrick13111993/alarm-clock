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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.text.DateFormatter;
import queuemanager.OrderedLinkedListPriorityQueue;
import queuemanager.QueueUnderflowException;



public class EditAlarmDialog extends JDialog implements ActionListener, PropertyChangeListener {
                    
    private View parent;
    private JFrame frame;

    private JOptionPane optionPane;
    private JSpinner spinner;
    private SpinnerDateModel model;
    private AlarmTimer timer;
//    index of currently shown alarm in queue
    int index = 0;

    private String btnString1 = "Update";
    private String btnString2 = "Next";
    private String btnString3 = "Cancel";

    public EditAlarmDialog(JFrame aFrame, View p) {
        super(aFrame, true);
        frame = aFrame;
        parent = p;

        setTitle("Edit Alarms");
        
        timer = parent.model.queue.getAtIndex(index);

        model = new SpinnerDateModel();
        model.setValue(timer.datetime);

        spinner = new JSpinner(model);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);

        spinner.setEditor(editor);
        
        Object[] options = {btnString1, btnString3};
        Object[] nextoptions = {btnString1, btnString2, btnString3};

        //Create the JOptionPane. Add a Next button for iteration through queue if multiple alarms exist
        if((index + 1) == parent.model.queue.length()) {
            optionPane = new JOptionPane(spinner,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION,
            null,
            options,
            options[0]);  
        } else {
            optionPane = new JOptionPane(spinner,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION,
            null,
            nextoptions,
            nextoptions[0]);
        }
 
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
                
//              NB Could not find a better way to achieve this. Will cause an integer overflow on February 4th 2088 unless baseline date is updated. 
//              Might be worth setting an an alarm for February 3rd 2088 as a reminder
                int priority = 0 - ((int) ((alarmdate.getTime()-baseline.getTime())/1000));
                
                int datedifference = ((int) ((alarmdate.getTime()-parent.model.datetime.getTime())/1000));
                
                if(datedifference > 0) {
                
                    parent.model.queue.removeAtIndex(index);
                    
                    JOptionPane.showMessageDialog(
                        EditAlarmDialog.this,
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
                    EditAlarmDialog.this,
                    "Error: " + dateString + " is not in the future!",
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
                }
            } else if (btnString2 == value) { 
                index++;
                if(index == parent.model.queue.length()) {
                    JOptionPane.showMessageDialog(
                    EditAlarmDialog.this,
                    "End of list",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                    index--;
                } else {
                    timer = parent.model.queue.getAtIndex(index);
                    model.setValue(timer.datetime);
                }
            } else { //user closed dialog or clicked cancel
                setVisible(false);
            }
        }
    }
}


