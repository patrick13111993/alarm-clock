package alarmclock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.DateFormatter;


public class AlarmDialog extends JDialog implements ActionListener, PropertyChangeListener {
                    
    private View parent;

    private JOptionPane optionPane;
    private JSpinner spinner;

    private String btnString1 = "OK";
    private String btnString2 = "Cancel";

//    Adapted from: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/DialogDemoProject/src/components/CustomDialog.java
    public AlarmDialog(Frame aFrame, View p) {
        super(aFrame, true);
        parent = p;

        setTitle("Set Alarm");
       
        Object[] options = {btnString1, btnString2};
        
//      Source: https://stackoverflow.com/questions/21960236/jspinner-time-picker-model-editing  
        Calendar calendar = Calendar.getInstance();

        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
                Date date = (Date)spinner.getValue();
                JOptionPane.showMessageDialog(
                AlarmDialog.this,
                "Alarm set for: " + dateString,
                "Alarm set",
                JOptionPane.INFORMATION_MESSAGE);
                AlarmTimer timer = new AlarmTimer(date, parent);
                parent.model.addObserver(timer);
                timer.setVisible(true);
            }
             else { //user closed dialog or clicked cancel
                setVisible(false);
            }
        }
    }
}
