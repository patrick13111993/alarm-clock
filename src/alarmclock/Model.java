package alarmclock;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
//import java.util.GregorianCalendar;

public class Model extends Observable {
    
//    int year = 0;
//    int month = 0;
//    int day = 0;
    Date datetime;
    int hour = 0;
    int minute = 0;
    int second = 0;
    
    int oldSecond = 0;
    
    public Model() {
        update();
    }
    
    public void update() {
        Calendar date = Calendar.getInstance();
        
        datetime = date.getTime();
        hour = date.get(Calendar.HOUR);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }        
    }
}