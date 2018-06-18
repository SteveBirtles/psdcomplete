package server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {

    public static void log(String text) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss.SSS dd/MM/yyyy");
        Date d = new Date();
        System.out.println("[" + simpleDate.format(d) + "] " + text);
    }

}
