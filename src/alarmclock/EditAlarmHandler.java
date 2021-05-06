package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author patrick
 */
class EditAlarmHandler implements ActionListener {
    
    JFrame frame;
    View parent;
    
    public EditAlarmHandler(JFrame frame, View parent) {
        this.frame = frame;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        
//        open edit alarm dialogue 
        EditAlarmDialog dialog = new EditAlarmDialog(frame, parent);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
