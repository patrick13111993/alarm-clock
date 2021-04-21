/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author patrick
 */
class RadioButtonHandler implements ActionListener  {

        @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println(event.getSource());
                if(event.getActionCommand()=="digital") {
                    
            }  
        } 
}

