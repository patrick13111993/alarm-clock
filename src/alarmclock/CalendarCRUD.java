package alarmclock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
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

    public void write(Model model) {
        StringBuilder builder = new StringBuilder();
        
        int alarmcount = model.queue.length();
        String[] alarms = new String[alarmcount];
        Calendar cal = Calendar.getInstance();
        
        for(int i = 0; i < alarmcount; i++) {
            try {
                AlarmTimer timer = model.queue.getAtIndex(i);
                
                cal.setTime(timer.datetime);
                cal.add(Calendar.MINUTE, 1);
                Date alarmend = cal.getTime();
                Date alarmdate = timer.datetime;
                
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
                
                String alarmdatestring = dateFormat.format(alarmdate);
                String todaystring = dateFormat.format(model.datetime);
                String alarmendstring = dateFormat.format(alarmend);
                
                alarms[i] = "DTSTAMP:" + todaystring + "\r\n" + "DTSTART:" + alarmdatestring + "\r\nDTEND:" + alarmendstring + "\r\nSUMMARY:Alarm\r\n";
            } catch (QueueUnderflowException ex) {
                Logger.getLogger(CalendarCRUD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//              Create file
            File file = new File(builder.toString());
            JFileChooser filechooser = new JFileChooser(file, FileSystemView.getFileSystemView());

        // Source: https://stackoverflow.com/questions/61885015/how-to-allow-the-user-to-choose-save-location-and-filename
        int returnValue = filechooser.showSaveDialog(null);
        if ( returnValue == JFileChooser.APPROVE_OPTION) {
//            Set file as iCalendar format
            File fileToSave = new File(filechooser.getSelectedFile().toString() + ".ics");
            try{
                FileWriter fw = new FileWriter(fileToSave.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(calBegin);
                bw.write(version);
                bw.write(prodid);
//                Create new event for each alarm in queue
                for(int i = 0; i < alarms.length; i++) {
                    bw.write(eventBegin);
                    bw.write(alarms[i]);
                    bw.write(eventEnd);
                }
                bw.write(calEnd);

                bw.close();

                System.out.println("Done");
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void read(Model model, JFrame frame, View view) throws IOException, FileNotFoundException, ParseException {
        JFileChooser filechooser = new JFileChooser();
//        Set filetype to iCalendar files
        filechooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "iCalendar Documents (*.ics)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".ics");
                }
            }
        });
        
        int result = filechooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
//             User selected a file
            String currentLine = "";
            
            File file = filechooser.getSelectedFile();
            FileReader fr;
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

//            Baseline date for calculating priority in queue
            Calendar baselinedate = new GregorianCalendar(2020, 1, 1);
            Date baseline = baselinedate.getTime();
            
            while((currentLine = br.readLine()) != null) {
                if(currentLine.contains("DTSTART:")) {
                    
//                    Create new alarm timer object from saved event start date and add to queue if valid
                    String dateString = currentLine.replace("DTSTART:", "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
                    sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));

                    Date alarmdate = sdf.parse(dateString);

                    int priority = 0 - ((int) ((alarmdate.getTime() - baseline.getTime()) / 1000));
                    int datedifference = ((int) ((alarmdate.getTime() - view.model.datetime.getTime()) / 1000));
                    System.out.println(alarmdate.toString() + view.model.datetime.toString());
                    if(datedifference > 0) {
                        AlarmTimer timer = new AlarmTimer(alarmdate, frame, view);
                        model.queue.add(timer,priority);
                        System.out.println(dateString);
                    }
                }
            }
        }   
    }
}
