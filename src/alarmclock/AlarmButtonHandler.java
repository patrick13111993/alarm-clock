package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author patrick
 */
class AlarmButtonHandler implements ActionListener {
    
    JFrame frame;
    View parent;
    
    public AlarmButtonHandler(JFrame frame, View parent) {
        this.frame = frame;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
//      Create popup dialogue box on button click
        AlarmDialog dialog = new AlarmDialog(frame, parent);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
