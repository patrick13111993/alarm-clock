package alarmclock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.OrderedLinkedListPriorityQueue;
import queuemanager.QueueUnderflowException;

//Adapted from: https://stackoverflow.com/questions/31238492/writing-ics-ical-file-using-java
public class CalendarCRUD {

    private String version = "VERSION:2.0\r\n";
    private String prodid = "PRODID://18016472/uhi/software/alarmclock/2021\r\n";
    private String calBegin = "BEGIN:VCALENDAR\r\n";
    private String calEnd = "END:VCALENDAR\r\n";
    private String eventBegin = "BEGIN:VEVENT\r\n";
    private String eventEnd = "END:VEVENT\r\n";


    public void CalendarCRUD() {
    }

    public void write(String name, Model model) {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(".ics");
        
        int alarmcount = model.queue.length();
        String[] alarms = new String[alarmcount];
        Calendar cal = Calendar.getInstance();
        
        for(int i = 0; i < alarmcount; i++) {
            try {
                AlarmTimer timer = model.queue.head();
                
                cal.setTime(timer.datetime);
                cal.add(Calendar.MINUTE, 1);
                Date alarmend = cal.getTime();
                Date alarmdate = timer.datetime;
                
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
                
                String alarmdatestring = dateFormat.format(alarmdate);
                String todaystring = dateFormat.format(model.datetime);
                String alarmendstring = dateFormat.format(alarmend);
                
                alarms[i] = "DTSTAMP:" + todaystring + "\r\n" + "DTSTART:" + alarmdatestring + "\r\nDTEND:" + alarmendstring + "\r\nSUMMARY:Alarm\r\n";
                System.out.println(alarms[i]);
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(CalendarCRUD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        String testExample = "DTSTAMP:19970714T170000Z\r\nORGANIZER;CN=John Doe:MAILTO:john.doe@example.com\r\nDTSTART:19970714T170000Z\r\nDTEND:19970715T035959Z\r\nSUMMARY:Bastille Day Party\r\n";
            
    try {

            File file = new File(builder.toString());

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(calBegin);
            bw.write(version);
            bw.write(prodid);
            for(int i = 0; i < alarms.length; i++) {
                bw.write(eventBegin);
                bw.write(alarms[i]);
                bw.write(eventEnd);
            }
            bw.write(calEnd);

            bw.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
