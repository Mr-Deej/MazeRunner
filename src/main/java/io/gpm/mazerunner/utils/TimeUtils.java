package io.gpm.mazerunner.utils;

/***
 * @author George
 * @since 20-Jun-19
 */
public class TimeUtils {

    public static String convertTime(int time) {
        int sec = Integer.parseInt(String.valueOf(time), 10);
        int remainder = sec % 86400;
        int days = (int) Math.floor(sec / 86400);
        int hours = (int) Math.floor(remainder / 3600);
        int minutes = (int) Math.floor((remainder / 60) - (hours * 60));
        int seconds = (int) Math.floor((remainder % 3600) - (minutes * 60));

        if(days > 0)
            return days + "d " + hours + "h " + "m " + seconds + "s";
        else if(hours > 0)
            return hours + "h " + minutes + "m " + seconds + "s";
        else if(minutes > 0)
            return minutes + "m " + seconds + "s";
        else
            return seconds + "s";
    }
}
