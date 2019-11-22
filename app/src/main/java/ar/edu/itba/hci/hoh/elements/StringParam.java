package ar.edu.itba.hci.hoh.elements;

public class StringParam {
    public static String[] getParams(int param1) {
        String[] ret = new String[1];
        ret[0] = String.valueOf(param1);
        return ret;
    }

    public static String[] getParams(String param1) {
        String[] ret = new String[1];
        ret[0] = param1;
        return ret;
    }
}