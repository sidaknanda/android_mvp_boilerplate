package co.sn.utils;

public class StringUtils {

    public static String getTwoDigitString(String number) {
        int no = Integer.parseInt(number);
        if (no < 10) {
            return "0" + no;
        } else {
            return number;
        }
    }
}
