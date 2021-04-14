package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author patrick
 */
class AboutButtonHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent event) {
//      Create popup dialogue box on button click
        JOptionPane.showMessageDialog(null, "Alarm clock made with Java!", "Message", JOptionPane.ERROR_MESSAGE);
    }
    
}
